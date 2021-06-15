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
package eu.toop.playground.dc.ui.util.enums;

import eu.toop.playground.dc.config.enums.DCConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 18/5/2020. */
public class DCUIConfigTest {

  @Ignore
  @Test
  public void testGetSimSubmitRequestURL() {
    String actual = DCConfig.INSTANCE.getSubmitRequestURL();
    String expected = "http://localhost:8081/api/user/submit/request";
    Assert.assertEquals(expected, actual);
  }

  @Ignore
  @Test
  public void testGetDSDEndpointURL() {
    String actual = DCConfig.INSTANCE.getDSDEndpointURL();
    String expected = "http://localhost:8081/api/dsd/dp";
    Assert.assertEquals(expected, actual);
  }

  @Ignore
  @Test
  public void testGetFreedoniaDCSenderIdScheme() {
    String actual = DCConfig.INSTANCE.getDefaultFreedoniaDCSenderIdScheme();
    String expected = "iso6523-actorid-upis";
    Assert.assertEquals(expected, actual);
  }

  @Ignore
  @Test
  public void testGetFreedoniaDCSenderIdValue() {
    String actual = DCConfig.INSTANCE.getDefaultFreedoniaDCSenderIdValue();
    String expected = "9999:freedonia-dev";
    Assert.assertEquals(expected, actual);
  }
}
