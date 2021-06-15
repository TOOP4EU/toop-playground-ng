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
package eu.toop.playground.dc.ui;

import com.helger.commons.io.resource.ClassPathResource;
import eu.toop.connector.api.rest.TCIncomingMessage;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.playground.dc.servlet.EDMResponseServlet;
import org.junit.Test;

import java.io.InputStream;

public class AttachmentProcessingTest {
    InputStream s = new ClassPathResource("SKResponse_withAttachment.xml").getInputStream();
    EDMResponseServlet servlet = new EDMResponseServlet();

  @Test
  public void testAttachmentServlet() {
      final TCIncomingMessage tcIncomingMessage = TCRestJAXB.incomingMessage().read(s);
      servlet.parseResponseWithAttachments(tcIncomingMessage);

  }
}
