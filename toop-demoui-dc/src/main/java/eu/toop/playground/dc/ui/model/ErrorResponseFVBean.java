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

import java.util.List;

import eu.toop.edm.error.EDMExceptionPojo;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ErrorResponseFVBean {

  private String errorProviderID;
  private String requestID;
  private String errorProviderName;
  private String responseStatus;
  private List<EDMExceptionPojo> exceptionList;

  public String getRequestID() {
    return requestID;
  }

  public void setRequestID(String requestID) {
    this.requestID = requestID;
  }

  public String getErrorProviderID() {
    return errorProviderID;
  }

  public void setErrorProviderID(String errorProviderID) {
    this.errorProviderID = errorProviderID;
  }

  public String getErrorProviderName() {
    return errorProviderName;
  }

  public void setErrorProviderName(String errorProviderName) {
    this.errorProviderName = errorProviderName;
  }

  public String getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
  }

  public List<EDMExceptionPojo> getExceptionList() {
    return exceptionList;
  }

  public void setExceptionList(List<EDMExceptionPojo> exceptionList) {
    this.exceptionList = exceptionList;
  }
}
