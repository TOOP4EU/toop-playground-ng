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
package eu.toop.playground.dc.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.IEDMTopLevelObject;
import eu.toop.edm.xml.EDMPayloadDeterminator;
import eu.toop.playground.dc.ui.service.BroadcasterService;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 18/5/2020. */
@WebServlet(name = "EDMResponseDirectServlet", urlPatterns = "/from-dp")
public class EDMResponseDirectServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(EDMResponseDirectServlet.class);

  @Override
  protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
      throws IOException {
    BufferedInputStream bis = new BufferedInputStream(httpRequest.getInputStream());
    IEDMTopLevelObject aTLO = EDMPayloadDeterminator.parseAndFind(bis);

    if (aTLO != null && aTLO instanceof EDMResponse) {
      EDMResponse edmResponse = (EDMResponse) aTLO;

      LOGGER.debug("EDMResponse {} successfully retrieved.", edmResponse.getRequestID());
      // Broadcast EDMResponse back to the listeners
      BroadcasterService.INSTANCE.broadcast(edmResponse);
      httpResponse.setStatus(HttpStatus.SC_OK);

    } else if (aTLO != null && aTLO instanceof EDMErrorResponse) {
      EDMErrorResponse edmErrorResponse = (EDMErrorResponse) aTLO;

      LOGGER.debug("EDMErrorResponse {} successfully retrieved.", edmErrorResponse.getRequestID());
      // Broadcast EDMErrorResponse back to the listeners
      BroadcasterService.INSTANCE.broadcast(edmErrorResponse);
      httpResponse.setStatus(HttpStatus.SC_OK);

    } else {
      LOGGER.debug("Unable to parse input, returning BAD REQUEST:400");
      httpResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
    }
  }
}
