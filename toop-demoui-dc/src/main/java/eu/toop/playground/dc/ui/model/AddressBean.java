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
