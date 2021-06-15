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
package eu.toop.playground.dc.ui.model;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class AddressBean {
  private String addressFullAddress;
  private String addressLocatorDesignator;
  private String addressThoroughfare;
  private String addressPostName;
  private String addressAdminUnitFirstline;
  private String addressPostCode;

  public String getAddressFullAddress() {
    return addressFullAddress;
  }

  public void setAddressFullAddress(String addressFullAddress) {
    this.addressFullAddress = addressFullAddress;
  }

  public String getAddressLocatorDesignator() {
    return addressLocatorDesignator;
  }

  public void setAddressLocatorDesignator(String addressLocatorDesignator) {
    this.addressLocatorDesignator = addressLocatorDesignator;
  }

  public String getAddressThoroughfare() {
    return addressThoroughfare;
  }

  public void setAddressThoroughfare(String addressThoroughfare) {
    this.addressThoroughfare = addressThoroughfare;
  }

  public String getAddressPostName() {
    return addressPostName;
  }

  public void setAddressPostName(String addressPostName) {
    this.addressPostName = addressPostName;
  }

  public String getAddressAdminUnitFirstline() {
    return addressAdminUnitFirstline;
  }

  public void setAddressAdminUnitFirstline(String addressAdminUnitFirstline) {
    this.addressAdminUnitFirstline = addressAdminUnitFirstline;
  }

  public String getAddressPostCode() {
    return addressPostCode;
  }

  public void setAddressPostCode(String addressPostCode) {
    this.addressPostCode = addressPostCode;
  }
}
