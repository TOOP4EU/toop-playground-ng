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
