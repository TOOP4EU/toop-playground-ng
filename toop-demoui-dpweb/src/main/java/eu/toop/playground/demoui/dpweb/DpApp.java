/**
 * Copyright 2021 - TOOP Project
 *
 * This file and its contents are licensed under the EUPL, Version 1.2
 * or – as soon they will be approved by the European Commission – subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *       https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.toop.playground.demoui.dpweb;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.MimeTypeDeterminator;
import com.typesafe.config.ConfigFactory;

import eu.toop.connector.api.me.EMEProtocol;
import eu.toop.connector.api.me.incoming.IIncomingEDMResponse;
import eu.toop.connector.api.me.incoming.IMEIncomingTransportMetadata;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.incoming.MEIncomingTransportMetadata;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.rest.TCIncomingMessage;
import eu.toop.connector.api.rest.TCIncomingMetadata;
import eu.toop.connector.api.rest.TCOutgoingMessage;
import eu.toop.connector.api.rest.TCOutgoingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.IEDMTopLevelObject;
import eu.toop.edm.xml.EDMPayloadDeterminator;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.kafkaclient.ToopKafkaSettings;
import eu.toop.playground.dp.DPException;
import eu.toop.playground.dp.model.EDMResponseWithAttachment;
import eu.toop.playground.dp.service.ToopDP;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class DpApp {
  public static final DpConfig APPCONFIG =
      new DpConfig(
          ConfigFactory.load()
              .withFallback(ConfigFactory.parseFile(Paths.get("demoui-dp.conf").toFile()))
              .withFallback(ConfigFactory.parseResources("demoui-dp.conf"))
              .resolve());
  private static final Logger LOGGER = LoggerFactory.getLogger(DpApp.class);
  private static final int MAX_RETRIES = 10;
  private static ToopDP miniDP =
      new ToopDP(APPCONFIG.getDp().getDatasetGBM(), APPCONFIG.getDp().getDatasetDocument());

  public static void main(String[] args) throws IOException {
    if (APPCONFIG.getDp().getKafka().enabled) {
      ToopKafkaSettings.setKafkaEnabled(true);
      ToopKafkaSettings.defaultProperties()
          .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, APPCONFIG.getDp().getKafka().url);
      ToopKafkaSettings.setKafkaTopic(APPCONFIG.getDp().getKafka().topic);
    } else ToopKafkaSettings.setKafkaEnabled(false);

    Javalin.create(
            javalinConfig -> {
              if (APPCONFIG.getServer().cors) javalinConfig.enableCorsForAllOrigins();
            })
        .start(APPCONFIG.getServer().getPort())
        .get("/", DpApp::index)
        .post(
            APPCONFIG.getDp().getDirect().getReceive(),
            context -> {
              context.contentType(CMimeType.APPLICATION_XML.getAsString());
              byte[] responseBytes;
              try {
                responseBytes = miniDP.createXMLResponseFromRequest(context.bodyAsBytes());
              } catch (DPException e) {
                responseBytes = e.getEdmErrorResponse().getWriter().getAsBytes();
              }
              LOGGER.info(
                  "Sent response with status {}",
                  sendResponse(responseBytes, APPCONFIG.getDp().getDirect().getSubmit()));
            })
        .post(
            APPCONFIG.getDp().getToop().getReceive(),
            context -> {
              final String incomingMessage = context.body();
              final TCIncomingMessage tcIncomingMessage =
                  TCRestJAXB.incomingMessage().read(incomingMessage);

              LOGGER.info(
                  "Sent response with status {}",
                  createAndSendTCOutgoingMessageFromIncoming(tcIncomingMessage).name());
            })
        .get("/datasets", DpApp::displayDatasets)
        .get(
            "/reload",
            context -> {
              miniDP =
                  new ToopDP(
                      APPCONFIG.getDp().getDatasetGBM(), APPCONFIG.getDp().getDatasetDocument());
              displayDatasets(context);
            });
  }

  private static void index(Context ctx) {
    ctx.render("/templates/index.vm", model("config", APPCONFIG));
  }

  private static void displayDatasets(Context ctx) {
    ctx.render(
        "/templates/datasets.vm",
        model(
            "conceptDatasets",
                (miniDP
                    .getRegisteredOrganizationDatasource()
                    .getDatasets()
                    .values()),
            "documentDatasets",
            miniDP
                    .getDocumentDatasource()
                    .getDatasets()
                    .values()));
  }

  private static StatusType createAndSendTCOutgoingMessageFromIncoming(
      TCIncomingMessage tcIncomingMessage) throws IOException {
    final TCIncomingMetadata metadata = tcIncomingMessage.getMetadata();
    if (metadata != null) {
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Elonia DP Received Incoming message with sender " + metadata.getSenderID());
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Elonia DP Received Incoming message with receiver " + metadata.getReceiverID());
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Elonia DP Received Incoming message with docTypeID " + metadata.getDocTypeID());
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Elonia DP Received Incoming message with payloadType " + metadata.getPayloadType());
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Elonia DP Received Incoming message with processID " + metadata.getProcessID());
    } else {
      ToopKafkaClient.send(
          EErrorLevel.ERROR, "Elonia DP Received Incoming message with no metadata");
      throw new IllegalStateException("Elonia DP Received Incoming message with no metadata");
    }
    TCPayload tcPayload = tcIncomingMessage.getPayloadAtIndex(0);
    LOGGER.info("DP Received Payload Content ID: " + tcPayload.getContentID());
    LOGGER.info("DP Received Payload Mime Type: " + tcPayload.getMimeType());

    if (tcPayload.getValue() != null) {
      IEDMTopLevelObject aTLO =
          EDMPayloadDeterminator.parseAndFind(new ByteArrayInputStream(tcPayload.getValue()));

      if (aTLO instanceof EDMResponse) {
        final EDMResponse edmResponse = (EDMResponse) aTLO;
        ToopKafkaClient.send(EErrorLevel.WARN, "Elonia DP Received an unexpected EDMResponse");
        LOGGER.info("DP Received Payload:\n {}", edmResponse.getWriter().getAsString());
        LOGGER.info("Sent no response.");
        return StatusType.NOT_OK;
      }
      if (aTLO instanceof EDMErrorResponse) {
        final EDMErrorResponse edmErrorResponse = (EDMErrorResponse) aTLO;
        ToopKafkaClient.send(
            EErrorLevel.ERROR, "Elonia DP Received an unexpected EDMErrorResponse");
        ToopKafkaClient.send(
            EErrorLevel.ERROR,
            "DP Received Payload:\n" + edmErrorResponse.getWriter().getAsString());
        LOGGER.info("Sent no response.");
        return StatusType.NOT_OK;
      }
      if (aTLO instanceof EDMRequest) {
        final EDMRequest edmRequest = (EDMRequest) aTLO;

        String requestID = "[" + edmRequest.getRequestID() + "] ";
        ToopKafkaClient.send(EErrorLevel.INFO, requestID + "Elonia DP Received an EDMRequest");

        LOGGER.info("DP Received Payload:\n" + edmRequest.getWriter().getAsString());

        MEIncomingTransportMetadata meIncomingTransportMetadata =
            MEIncomingTransportMetadata.create(metadata);
        TCOutgoingMessage tcOutgoingMessage;

        final MEIncomingTransportMetadata aMetadataInverse =
            new MEIncomingTransportMetadata(
                meIncomingTransportMetadata.getReceiverID(),
                    meIncomingTransportMetadata.getSenderID(),
                meIncomingTransportMetadata.getDocumentTypeID(),
                    meIncomingTransportMetadata.getProcessID());
        try {
          EDMResponseWithAttachment edmResponse =
              miniDP.createEDMResponseWithAttachmentsFromRequest(edmRequest);

          ToopKafkaClient.send(
              EErrorLevel.INFO, requestID + "DP created an EDMResponse successfully");

          List<MEPayload> attachments;
          if (!edmResponse.getAllAttachments().isEmpty()) {
            attachments =
                edmResponse.getAllAttachments().stream()
                    .map(
                        attachment -> {
                          byte[] fileBytes = attachment.getAttachment();

                          return MEPayload.builder()
                              .data(fileBytes)
                              .mimeType(
                                  MimeTypeDeterminator.getInstance()
                                      .getMimeTypeFromBytes(fileBytes))
                              .contentID(attachment.getAttachedFileCid())
                              .build();
                        })
                    .collect(Collectors.toList());
          } else attachments = new ArrayList<>();

          IncomingEDMResponse incomingEDMResponse =
              new IncomingEDMResponse(
                  edmResponse.getEdmResponse(),
                  tcPayload.getContentID(),
                  attachments,
                  aMetadataInverse);

          tcOutgoingMessage =
              createTCOutgoingMessageFromIncomingResponse(
                  incomingEDMResponse.getMetadata(), incomingEDMResponse);
          ToopKafkaClient.send(
              EErrorLevel.INFO, requestID + "DP created an OutgoingMessage successfully");

          StatusType messageSubmissionStatus =
              sendResponse(
                  TCRestJAXB.outgoingMessage().getAsBytes(tcOutgoingMessage),
                  APPCONFIG.getDp().getToop().getSubmit() + "/response");

          EErrorLevel errorLevel = messageSubmissionStatus.getErrorLevel();

          ToopKafkaClient.send(
              errorLevel,
              requestID
                  + "Elonia DP pushed response to connector with status "
                  + messageSubmissionStatus.name());

          return messageSubmissionStatus;

        } catch (DPException e) {
          EDMErrorResponse edmErrorResponse = e.getEdmErrorResponse();

          ToopKafkaClient.send(
              EErrorLevel.ERROR,
              requestID
                  + "Elonia DP failed to create an EDMResponse,"
                  + " sending back an EDMErrorResponse to DC");
          ToopKafkaClient.send(
              EErrorLevel.ERROR, requestID + "Error was: \"" + e.getMessage() + "\"");

          IncomingEDMErrorResponse incomingEDMResponse =
              new IncomingEDMErrorResponse(
                  edmErrorResponse, tcPayload.getContentID(), aMetadataInverse);

          tcOutgoingMessage =
              createTCOutgoingMessageFromIncomingResponse(
                  incomingEDMResponse.getMetadata(), incomingEDMResponse);

          ToopKafkaClient.send(
              EErrorLevel.INFO, requestID + "Elonia DP created an OutgoingMessage successfully");
          StatusType messageSubmissionStatus =
              sendResponse(
                  TCRestJAXB.outgoingMessage().getAsBytes(tcOutgoingMessage),
                  APPCONFIG.getDp().getToop().getSubmit() + "/error");

          EErrorLevel errorLevel = messageSubmissionStatus.getErrorLevel();

          ToopKafkaClient.send(
              errorLevel,
              requestID
                  + "Elonia DP pushed response to connector with status "
                  + messageSubmissionStatus.name());

          return messageSubmissionStatus;
        }
      }
    }
    return StatusType.NOT_OK;
  }

  private static TCOutgoingMessage createTCOutgoingMessageFromIncomingResponse(
      IMEIncomingTransportMetadata metadata, IIncomingEDMResponse incomingResponse) {
    final TCOutgoingMessage aOM = new TCOutgoingMessage();
    {
      final TCOutgoingMetadata aMetadata = new TCOutgoingMetadata();
      aMetadata.setReceiverID(
          TCRestJAXB.createTCID(
              metadata.getReceiverID().getScheme(), metadata.getReceiverID().getValue()));
      aMetadata.setSenderID(
          TCRestJAXB.createTCID(
              metadata.getSenderID().getScheme(), metadata.getSenderID().getValue()));
      aMetadata.setDocTypeID(
          TCRestJAXB.createTCID("toop-doctypeid-qns", "QueryResponse::toop-edm:v2.1"));
      aMetadata.setProcessID(
          TCRestJAXB.createTCID(
              metadata.getProcessID().getScheme(), metadata.getProcessID().getValue()));
      aMetadata.setTransportProtocol(EMEProtocol.AS4.getTransportProfileID());
      aOM.setMetadata(aMetadata);
    }

    {
      final TCPayload aPayload = new TCPayload();
      final List<TCPayload> filePayloads = new ArrayList<>();
      byte[] payload = null;

      if (incomingResponse instanceof IncomingEDMResponse) {
        payload = ((IncomingEDMResponse) incomingResponse).getResponse().getWriter().getAsBytes();

        filePayloads.addAll(
            ((IncomingEDMResponse) incomingResponse)
                .attachments().values().stream()
                    .map(
                        m -> {
                          TCPayload attachedFilePayload = new TCPayload();
                          attachedFilePayload.setContentID(m.getContentID());
                          attachedFilePayload.setMimeType(m.getMimeTypeString());
                          attachedFilePayload.setValue(m.getData().bytes());
                          return attachedFilePayload;
                        })
                    .collect(Collectors.toList()));

        aPayload.setContentID(
            ((IncomingEDMResponse) incomingResponse).getResponse().getRequestID() + "@elonia-dev");
      }
      if (incomingResponse instanceof IncomingEDMErrorResponse) {
        payload =
            ((IncomingEDMErrorResponse) incomingResponse)
                .getErrorResponse()
                .getWriter()
                .getAsBytes();
        aPayload.setContentID(
            ((IncomingEDMErrorResponse) incomingResponse).getErrorResponse().getRequestID()
                + "@elonia-dev");
      }

      aPayload.setValue(payload);
      aPayload.setMimeType(CMimeType.APPLICATION_XML.getAsString());
      aOM.addPayload(aPayload);

      if (!filePayloads.isEmpty()) {
        filePayloads.forEach(aOM::addPayload);
      }
    }
    return aOM;
  }

  private static StatusType sendResponse(byte[] responseMessage, String url) throws IOException {
    Objects.requireNonNull(responseMessage, "The supplied Response must not be null!");
    StatusType returnStatus = StatusType.NOT_OK;
    for (int i = 0; i < MAX_RETRIES; i++) {
      try (CloseableHttpClient client = HttpClients.createDefault()) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CMimeType.APPLICATION_XML.getAsString());
        ByteArrayEntity myEntity = new ByteArrayEntity(responseMessage);

        httpPost.setEntity(myEntity);
        CloseableHttpResponse response = client.execute(httpPost);
        String responseBody =
                new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                        .lines()
                        .parallel()
                        .collect(Collectors.joining("\n"));
        int statusCode = response.getStatusLine().getStatusCode();
        LOGGER.info("DP got response when sending message:\n {}", responseBody);
        if (statusCode != 200) {
          ToopKafkaClient.send(EErrorLevel.ERROR, "Response code connector:\n " + statusCode);
          ToopKafkaClient.send(EErrorLevel.ERROR, "Response from connector:\n " + responseBody);
          returnStatus = StatusType.NOT_OK;
          continue;
        }
        returnStatus = StatusType.OK;
        break;
      }
    }
    return returnStatus;
  }
}
