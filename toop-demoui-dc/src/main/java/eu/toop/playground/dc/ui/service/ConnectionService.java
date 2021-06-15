/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
 *
 * This work is licensed under the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.playground.dc.ui.service;

import java.io.IOException;
import java.io.InputStream;

import elemental.json.Json;
import eu.toop.playground.dc.ui.model.exceptions.EvaluationException;
import eu.toop.playground.dc.util.MessageDumper;
import eu.toop.playground.dc.util.MessageType;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.mime.CMimeType;

import eu.toop.connector.api.me.EMEProtocol;
import eu.toop.connector.api.rest.TCOutgoingMessage;
import eu.toop.connector.api.rest.TCOutgoingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.edm.EDMRequest;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.playground.dc.config.enums.DCConfig;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;
import eu.toop.playground.dc.ui.model.dto.DSDIDTypeDto;

import javax.validation.ValidationException;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 18/5/2020. */
public class ConnectionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class.getName());

  public ConnectionService() {}

  public void submit(EDMRequest request, DSDDatasetResponseDto dsdDatasetResponseDto)
      throws IOException, EvaluationException {
    if (DCConfig.INSTANCE.useDirectSubmit()) {
      submitDirect(request);
    } else {
      submitThroughInfrastructure(request, dsdDatasetResponseDto);
    }
  }

  public void submit(EDMRequest request) throws IOException, EvaluationException {
    if (DCConfig.INSTANCE.useDirectSubmit()) {
      submitDirect(request);
    } else {
      submitThroughInfrastructure(request);
    }
  }

  /**
   * Submit an {@link eu.toop.edm.EDMRequest} directly to Elonia. Only used for testing purposes.
   *
   * @param request The {@link eu.toop.edm.EDMRequest}.
   * @return Http response status code.
   * @throws IOException
   */
  private void submitDirect(EDMRequest request) throws IOException {

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(DCConfig.INSTANCE.getSubmitRequestURL());
      httpPost.setHeader("Content-Type", "application/xml");
      httpPost.setEntity(new InputStreamEntity(request.getWriter().getAsInputStream()));
      httpClient.execute(httpPost);
      //      CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
      //      httpResponse.getStatusLine().getStatusCode();
    }
  }

  /**
   * Submit an {@link eu.toop.edm.EDMRequest}, by using TOOP Connector NG API. Currently sending to
   * Simulator Mock DP.
   *
   * @param request The {@link eu.toop.edm.EDMRequest}.
   * @return Http response status code.
   * @throws IOException
   */
  private void submitThroughInfrastructure(EDMRequest request)
      throws IOException, EvaluationException {

    DSDDatasetResponseDto datasetResponseDto =
        new DSDDatasetResponseDto(
            new DSDIDTypeDto(
                DCConfig.INSTANCE.getDefaultEloniaDevParticipantIdScheme(),
                DCConfig.INSTANCE.getDefaultEloniaDevParticipantIdValue()),
            null,
            null,
            null,
            null,
            null);

    submitThroughInfrastructure(request, datasetResponseDto);
  }

  /**
   * Submit an {@link eu.toop.edm.EDMRequest}, by using TOOP Connector NG API. Currently sending to
   * Simulator Mock DP.
   *
   * @param request The {@link eu.toop.edm.EDMRequest}.
   * @return Http response status code.
   * @throws IOException
   */
  private void submitThroughInfrastructure(
      EDMRequest request, DSDDatasetResponseDto dsdDatasetResponseDto)
      throws IOException, EvaluationException {

    LOGGER.info("Sending EDMRequest through infrastructure...");
    final TCOutgoingMessage outgoingMessage = new TCOutgoingMessage();

    // Add metadata on outgoing message
    final TCOutgoingMetadata metadata = new TCOutgoingMetadata();
    metadata.setSenderID(
        TCRestJAXB.createTCID(
            DCConfig.INSTANCE.getDefaultFreedoniaDCSenderIdScheme(),
            DCConfig.INSTANCE.getDefaultFreedoniaDCSenderIdValue()));
    metadata.setReceiverID(
        TCRestJAXB.createTCID(
            dsdDatasetResponseDto.getParticipantId().getScheme(),
            dsdDatasetResponseDto.getParticipantId().getValue()));
    metadata.setDocTypeID(
        TCRestJAXB.createTCID(
            dsdDatasetResponseDto.getDoctypeId().getScheme(),
            dsdDatasetResponseDto.getDoctypeId().getValue()));
    switch (request.getQueryDefinition()) {
      case CONCEPT:
        metadata.setProcessID(
            TCRestJAXB.createTCID("toop-procid-agreement", "urn:eu.toop.process.dataquery"));
        break;
      case DOCUMENT_BY_DISTRIBUTION:
      case DOCUMENT_BY_ID:
        metadata.setProcessID(
            TCRestJAXB.createTCID("toop-procid-agreement", "urn:eu.toop.process.documentquery"));
        break;
    }
    metadata.setTransportProtocol(EMEProtocol.AS4.getTransportProfileID());
    outgoingMessage.setMetadata(metadata);

    // -----------------------------------------------------------------------------------------------------------------
    // Kafka client logs for metadata
    ToopKafkaClient.send(
        EErrorLevel.INFO, "Freedonia DC Sending an EDMRequest through infrastructure...");
    ToopKafkaClient.send(
        EErrorLevel.INFO,
        "Freedonia DC Sending an EDMRequest with sender " + metadata.getSenderID().getValue());
    ToopKafkaClient.send(
        EErrorLevel.INFO,
        "Freedonia DC Sending an EDMRequest with receiver " + metadata.getReceiverID().getValue());
    ToopKafkaClient.send(
        EErrorLevel.INFO,
        "Freedonia DC Sending an EDMRequest with docTypeID " + metadata.getDocTypeID().getValue());
    // -----------------------------------------------------------------------------------------------------------------

    // Add payload on outgoing message
    final TCPayload payload = new TCPayload();
    payload.setValue(request.getWriter().getAsBytes());
    payload.setMimeType(CMimeType.APPLICATION_XML.getAsString());
    payload.setContentID(String.format("%s@%s", request.getRequestID(), "freedonia-dev"));
    outgoingMessage.addPayload(payload);
    MessageDumper.dumpMessage(TCRestJAXB.outgoingMessage().getAsBytes(outgoingMessage), MessageType.OUTGOING);

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(DCConfig.INSTANCE.getSubmitRequestURL());
      httpPost.setEntity(
          new ByteArrayEntity(TCRestJAXB.outgoingMessage().getAsBytes(outgoingMessage)));

      CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
      int status = httpResponse.getStatusLine().getStatusCode();
      //       the json response
      JsonNode sJSON =
          new ObjectMapper().readTree(httpResponse.getEntity().getContent());

      MessageDumper.dumpMessage(sJSON.toPrettyString(), MessageType.INCOMING);

      if (status == HttpStatus.SC_NO_CONTENT || status == HttpStatus.SC_OK) {
        LOGGER.info("Freedonia DC sent an EDMRequest successfully.");
        ToopKafkaClient.send(EErrorLevel.INFO, "Freedonia DC sent an EDMRequest successfully.");
      } else {
        ToopKafkaClient.send(
            EErrorLevel.ERROR,
            "Freedonia DC failed to send an EDMRequest with status code: " + status);
        throw new ClientProtocolException(
            "Unable to send request to the connector. Unexpected response status: "
                + status
                + " from: "
                + DCConfig.INSTANCE.getSubmitRequestURL());
      }

      evaluateTCResponse(sJSON);
    }
  }

  /**
   * Evaluating the given TC Response.
   *
   * @param root The TC Response JSON.
   * @throws EvaluationException Depending on the error, a different message will be thrown.
   * @throws IOException An IOException will be thrown in the case where the XML path is wrong.
   */
  private void evaluateTCResponse(JsonNode root)
      throws IOException, EvaluationException {
    LOGGER.debug("Evaluating TC Response JSON...");
//    ObjectMapper mapper = new ObjectMapper();
//    JsonNode root = mapper.readTree(dsdResponseStream);
    // Validation results
    boolean validationResult = root.get("validation-results").get("success").asBoolean();
    LOGGER.debug("The validation results success status is= {}", validationResult);
    if (!validationResult) {
      ToopKafkaClient.send(
          EErrorLevel.ERROR, "The created EDM Request failed to pass the validation...");
      throw new EvaluationException("The created EDM Request failed to pass the validation...");
    }
    // Lookup results
    boolean lookupResult = root.get("lookup-results").get("success").asBoolean();
    LOGGER.debug("The lookup results success status is= {}", lookupResult);
    if (!lookupResult) {
      ToopKafkaClient.send(EErrorLevel.ERROR, "DP Routing Metadata was not found in SMP...");
      throw new EvaluationException("DP Routing Metadata was not found in SMP...");
    }
    // Sending results
    boolean sendingResult = root.get("sending-results").get("success").asBoolean();
    LOGGER.debug("The sending result success status is= {}", sendingResult);
    if (!sendingResult) {
      ToopKafkaClient.send(EErrorLevel.ERROR, "The created EDM Request failed to send to DP...");
      throw new EvaluationException("The created EDM Request failed to send to DP...");
    }
  }
}
