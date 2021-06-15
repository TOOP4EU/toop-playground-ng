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
package eu.toop.playground;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import eu.toop.playground.dp.model.Attachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.request.IEDMRequestPayloadConcepts;
import eu.toop.playground.dp.DPException;
import eu.toop.playground.dp.datasource.Datasource;
import eu.toop.playground.dp.datasource.RegisteredOrganizationDataSource;
import eu.toop.playground.dp.model.EDMResponseWithAttachment;
import eu.toop.playground.dp.model.GBMDataset;
import eu.toop.playground.dp.service.ToopDP;

import static org.junit.Assert.*;

public class MiniDPTest {

  ToopDP miniDP;

  @Before
  public void setUp() throws Exception {
      try{
          Files.walk(Paths.get("./datasets"))
                  .sorted(Comparator.reverseOrder())
                  .map(Path::toFile)
                  .forEach(File::delete);
      }catch (NoSuchFileException e){
          // No dataset directory to cleanup, ignore
      }

    miniDP = new ToopDP();
  }

  @Test
  public void testMergeConcepts() throws IOException {
    EDMRequest request =
        EDMRequest.reader().read(ClassPathResource.getInputStream("Concept Request_NP.xml"));
    Datasource<GBMDataset> dataSource = new RegisteredOrganizationDataSource();
    if (request.getPayloadProvider() instanceof IEDMRequestPayloadConcepts) {
      IEDMRequestPayloadConcepts concepts =
          (IEDMRequestPayloadConcepts) request.getPayloadProvider();
      List<ConceptPojo> x =
          miniDP.mergeConceptValues(
              concepts.getAllConcepts(),
              dataSource.findResponse(request).get(0).getConceptPojoList());
      assertNotNull(x);
      //            System.out.println(x);
    }
  }

  @Test
  public void testResponseGeneration() throws DPException, IOException {
    EDMRequest request =
        EDMRequest.reader().read(ClassPathResource.getInputStream("Concept Request_NP.xml"));
    byte[] responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
    assertNotNull(responseBytes);
    //        System.out.println(new String(responseBytes));
  }

  @Test
  public void testDocumentRefRequest() throws DPException, IOException {
    EDMRequest request =
        EDMRequest.reader()
            .read(ClassPathResource.getInputStream("Document_Request_ObjectRef.xml"));
    byte[] responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
    assertNotNull(responseBytes);
    System.out.println(new String(responseBytes));
  }

  @Test
  public void testDocumentIDRequest() throws DPException, IOException {
    EDMRequest request =
        EDMRequest.reader().read(ClassPathResource.getInputStream("Document_Request_GetByID.xml"));
    byte[] responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
    assertNotNull(responseBytes);
    System.out.println(new String(responseBytes));
  }

  @Test
  public void testDocumentIDRequest_NoDataSubject() throws DPException, IOException {
    EDMRequest request =
            EDMRequest.reader().read(ClassPathResource.getInputStream("Document_Request_GetByID_NoDataSubject.xml"));
    byte[] responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
    assertNotNull(responseBytes);
    System.out.println(new String(responseBytes));
  }

  @Test
  public void testDocumentIDRequestAttachment() throws DPException, IOException {
    EDMRequest request =
            EDMRequest.reader().read(ClassPathResource.getInputStream("Document_Request_GetByID.xml"));
    EDMResponseWithAttachment x = miniDP.createEDMResponseWithAttachmentsFromRequest(request.getWriter().getAsBytes());
    assertNotNull(x.getEdmResponse());
    assertTrue(x.getAttachment().isPresent());
    for (Attachment attachment : x.getAllAttachments()) {
      assertNotEquals(new byte[0], attachment.getAttachment());
    }
//    byte[] fileBytes = Files.readAllBytes(x.getAttachedFile().get().toPath());
//    Files.write(Paths.get("./example.jpg"), fileBytes);
    System.out.println(x.getEdmResponse().getWriter().getAsString());
  }

  @Test
  public void testDocumentResponseGeneration() throws IOException, DPException {
    EDMRequest request =
        EDMRequest.reader().read(ClassPathResource.getInputStream("Document Request_LP.xml"));
    byte[] responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
    assertNotNull(responseBytes);
    //        System.out.println(new String(responseBytes));
  }

  @Test
  public void testMinimumDataset() throws IOException, DPException {
    EDMRequest request =
        EDMRequest.reader().read(ClassPathResource.getInputStream("Document Request_min.xml"));
    byte[] responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
    assertNotNull(responseBytes);
//            System.out.println(new String(responseBytes));
  }

  @Test
  public void testMultipleAttachments() throws DPException, IOException {
    EDMRequest request =
            EDMRequest.reader().read(ClassPathResource.getInputStream("Document_Request_NP_LP_DE.xml"));
    EDMResponseWithAttachment x = miniDP.createEDMResponseWithAttachmentsFromRequest(request.getWriter().getAsBytes());
    assertNotNull(x.getEdmResponse());
    assertFalse(x.getAllAttachments().isEmpty());
    assertEquals(6, x.getAllAttachments().size());
    for (Attachment attachment : x.getAllAttachments()) {
      assertNotEquals(new byte[0], attachment.getAttachment());
    }
//    byte[] fileBytes = Files.readAllBytes(x.getAttachedFile().get().toPath());
//    Files.write(Paths.get("./example.jpg"), fileBytes);
    System.out.println(x.getEdmResponse().getWriter().getAsString());
  }

  @Test
  public void testBogusRequest() throws IOException {

    try {
      EDMResponse response =
          miniDP.createEDMResponseFromRequest(
              EDMRequest.reader().read(ClassPathResource.getInputStream("bogus.xml")));
      fail();
    } catch (DPException e) {
      assertNotNull(e.getEdmErrorResponse());
      //            System.out.println(e.getEdmErrorResponse().getWriter().getAsString());
    }
  }

  @Test
  public void testNullRequest() throws IOException {
    byte[] responseBytes = new byte[0];
    try {
      responseBytes = miniDP.createXMLResponseFromRequest(null);
      fail();
    } catch (DPException e) {
      assertNotNull(e.getEdmErrorResponse());
      //            System.out.println(e.getEdmErrorResponse().getWriter().getAsString());
    }
  }

  @Test
  public void testNotExistingID() throws IOException {
    EDMRequest request =
        EDMRequest.reader()
            .read(ClassPathResource.getInputStream("Concept Request_LP_BogusID.xml"));
    byte[] responseBytes = new byte[0];
    try {
      responseBytes = miniDP.createXMLResponseFromRequest(request.getWriter().getAsBytes());
      fail();
    } catch (DPException e) {
      assertNotNull(e.getEdmErrorResponse());
      //            System.out.println(e.getEdmErrorResponse().getWriter().toString());
    }
  }

  @After
  public void tearDown() throws Exception {
    Files.walk(Paths.get("./datasets"))
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::delete);
  }
}
