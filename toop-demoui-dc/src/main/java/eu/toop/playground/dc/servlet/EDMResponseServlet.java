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
package eu.toop.playground.dc.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.toop.playground.dc.util.MessageDumper;
import eu.toop.playground.dc.util.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.io.stream.StreamHelper;

import eu.toop.connector.api.rest.TCIncomingMessage;
import eu.toop.connector.api.rest.TCIncomingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.IEDMTopLevelObject;
import eu.toop.edm.xml.EDMPayloadDeterminator;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.playground.dc.ui.model.Attachment;
import eu.toop.playground.dc.ui.model.EDMResponseWithAttachment;
import eu.toop.playground.dc.ui.model.enums.EMimeType;
import eu.toop.playground.dc.ui.model.enums.EResponseType;
import eu.toop.playground.dc.ui.service.BroadcasterService;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 25/5/2020. */
@WebServlet(name = "EDMResponseSimServlet", urlPatterns = "/to-dc")
public class EDMResponseServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(EDMResponseServlet.class);

  @Override
  protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
      throws IOException {

    byte[] bytes = StreamHelper.getAllBytes(httpRequest.getInputStream());
    MessageDumper.dumpMessage(bytes, MessageType.INCOMING);
    LOGGER.info("Freedonia DC Servlet received a TC incoming message.");
    final String sIncomingMessage = new String(bytes, StandardCharsets.UTF_8);
    // Print message
    LOGGER.debug("Freedonia DC is printing the TC incoming message:\n{}", sIncomingMessage);

    // Read incoming message
    final TCIncomingMessage tcIncomingMessage = TCRestJAXB.incomingMessage().read(sIncomingMessage);

    if (tcIncomingMessage == null) {
      ToopKafkaClient.send(
          EErrorLevel.ERROR, "Freedonia DC is unable to read TC incoming message...");
      LOGGER.debug("Freedonia DC is unable to read TC incoming message...");
    }

    ToopKafkaClient.send(
        EErrorLevel.INFO, "Freedonia DC Servlet read the TC incoming message successfully.");
    LOGGER.debug("Freedonia DC Servlet read the TC incoming message successfully.");

    // Read incoming message's metadata
    LOGGER.debug("Freedonia DC Servlet is trying to read the TC incoming message's metadata.");
    ToopKafkaClient.send(
        EErrorLevel.INFO,
        "Freedonia DC Servlet is trying to read the TC incoming message's metadata.");
    final TCIncomingMetadata metadata = tcIncomingMessage.getMetadata();
    if (metadata == null) {
      ToopKafkaClient.send(
          EErrorLevel.ERROR,
          "Freedonia DC Servlet is unable to read TC incoming message's metadata...");
      LOGGER.debug("Freedonia DC Servlet is unable to read TC incoming message's metadata...");
    }

    ToopKafkaClient.send(
        EErrorLevel.INFO,
        "Freedonia DC Servlet read the TC incoming message's metadata successfully.");
    LOGGER.debug("Freedonia DC Servlet read the TC incoming message's metadata successfully.");

    LOGGER.debug("Number of payloads: {}", tcIncomingMessage.getPayloadCount());

    switch (extractResponseType(tcIncomingMessage)) {
      case EDM_RESPONSE_OR_ERROR_RESPONSE:
        parseResponse(tcIncomingMessage);
        break;
      case EDM_RESPONSE_WITH_ATTACHMENT:
        parseResponseWithAttachments(tcIncomingMessage);
        break;
    }
  }

  /**
   * Parse the EDM response or EDM error response from the TC incoming message.
   *
   * @param incomingMessage
   */
  private void parseResponse(TCIncomingMessage incomingMessage) {
    // Assuming that their is only one payload, which represent's the response.
    if (incomingMessage.getPayloadCount() == 1
        && incomingMessage
            .getPayloadAtIndex(0)
            .getMimeType()
            .equals(EMimeType.RESPONSE_TYPE.getValue())) {

      TCPayload tcPayload = incomingMessage.getPayloadAtIndex(0);

      LOGGER.debug(
          "Freedonia DC Servlet read a payload with Content ID: {}", tcPayload.getContentID());
      LOGGER.debug("Freedonia DC Servlet read a payload  Mime Type: {}", tcPayload.getMimeType());

      InputStream bais = new ByteArrayInputStream(tcPayload.getValue());
      IEDMTopLevelObject aTLO = EDMPayloadDeterminator.parseAndFind(bais);

      if (aTLO != null && aTLO instanceof EDMResponse) {
        final EDMResponse edmResponse = (EDMResponse) aTLO;
        ToopKafkaClient.send(EErrorLevel.INFO, "Freedonia DC Servlet read an EDMResponse payload.");
        // Broadcasting EDM Response back to listeners
        broadcast(edmResponse);
        LOGGER.debug(
            "Freedonia DC Servlet read an EDMResponse payload:\n {}",
            edmResponse.getWriter().getAsString());
      } else if (aTLO != null && aTLO instanceof EDMErrorResponse) {
        final EDMErrorResponse edmErrorResponse = (EDMErrorResponse) aTLO;
        ToopKafkaClient.send(
            EErrorLevel.INFO, "Freedonia DC Servlet read an EDMErrorResponse payload.");
        // Broadcasting EDM Error Response back to listeners
        broadcast(edmErrorResponse);
        LOGGER.debug(
            "Freedonia DC Servlet read an EDMErrorResponse payload:\n {}",
            edmErrorResponse.getWriter().getAsString());
      } else {
        // Unable to parse TLO value
        ToopKafkaClient.send(EErrorLevel.ERROR, "Freedonia DC is unable to parse payload value...");
        LOGGER.error("Freedonia DC is unable to parse payload value...");
      }
    }
  }

  /**
   * Parse the EDM response alongside with the attachments from the TC incoming message.
   *
   * @param incomingMessage
   */
  public void parseResponseWithAttachments(TCIncomingMessage incomingMessage) {
    EDMResponse edmResponse = null;
    List<Attachment> attachmentList = new ArrayList<>();

    for (TCPayload tcPayload : incomingMessage.getPayload()) {
      LOGGER.debug(
          "Freedonia DC Servlet read a payload with Content ID: {}", tcPayload.getContentID());
      LOGGER.debug("Freedonia DC Servlet read a payload  Mime Type: {}", tcPayload.getMimeType());
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Freedonia DC Servlet read a payload witn Conent ID: " + tcPayload.getContentID());
      ToopKafkaClient.send(
          EErrorLevel.INFO,
          "Freedonia DC Servlet read a payload  Mime Type: " + tcPayload.getMimeType());

      // if payload is an EDM model response
      if (tcPayload.getMimeType().equals(EMimeType.RESPONSE_TYPE.getValue())) {
        InputStream bais = new ByteArrayInputStream(tcPayload.getValue());
        IEDMTopLevelObject aTLO = EDMPayloadDeterminator.parseAndFind(bais);

        if (aTLO != null && aTLO instanceof EDMResponse) {
          edmResponse = (EDMResponse) aTLO;
          ToopKafkaClient.send(
              EErrorLevel.INFO, "Freedonia DC Servlet read an EDMResponse payload.");
          LOGGER.debug(
              "Freedonia DC Servlet read an EDMResponse payload:\n {}",
              edmResponse.getWriter().getAsString());
        } else {
          // Unable to parse TLO value
          ToopKafkaClient.send(
              EErrorLevel.ERROR, "Freedonia DC is unable to parse payload value...");
          LOGGER.error("Freedonia DC is unable to parse payload value...");
        }
      } else { // Payload is an Attachment
        attachmentList.add(
            new Attachment(
                tcPayload.getValue(), tcPayload.getContentID(), tcPayload.getMimeType()));
      }
    }

    if (edmResponse != null && !attachmentList.isEmpty()) {
      EDMResponseWithAttachment edmResponseWithAttachment =
          new EDMResponseWithAttachment(edmResponse);
      edmResponseWithAttachment.getAttachmentList().addAll(attachmentList);
      // Broadcasting EDM response alongside with the attachments back to listeners.
      broadcast(edmResponseWithAttachment);
    }
  }

  /**
   * A payload list with more than one payloads is considered as a response with attachments.
   *
   * @param incomingMessage
   * @return
   */
  private EResponseType extractResponseType(TCIncomingMessage incomingMessage) {
    return incomingMessage.getPayloadCount() > 1
        ? EResponseType.EDM_RESPONSE_WITH_ATTACHMENT
        : EResponseType.EDM_RESPONSE_OR_ERROR_RESPONSE;
  }

  /**
   * Broadcast back to UI the EDM response alongside with the attachments.
   *
   * @param response
   */
  private void broadcast(EDMResponseWithAttachment response) {
    BroadcasterService.INSTANCE.broadcast(response);
  }

  /**
   * Broadcast back to UI the EDM error response.
   *
   * @param response
   */
  private void broadcast(EDMErrorResponse response) {
    BroadcasterService.INSTANCE.broadcast(response);
  }

  /**
   * Broadcast back to UI the EDM Response.
   *
   * @param response
   */
  private void broadcast(EDMResponse response) {
    BroadcasterService.INSTANCE.broadcast(response);
  }
}
