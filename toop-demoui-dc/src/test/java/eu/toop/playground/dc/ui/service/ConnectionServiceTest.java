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

import com.helger.commons.io.resource.ClassPathResource;
import eu.toop.edm.EDMRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 25/5/2020. */
public class ConnectionServiceTest {

  private ConnectionService service = new ConnectionService();

  @Before
  public void setUp() {}

  @Ignore
  @Test
  public void testSubmitThroughInfrastructure() throws Exception {
    EDMRequest request =
        // EDMRequest.reader().read(new ClassPathResource("examples/TOOPRequest.xml"));
        EDMRequest.reader().read(new ClassPathResource("examples/concRequestLP.xml"));
    Assert.assertNotNull(request);

    service.submit(request);
  }
}
