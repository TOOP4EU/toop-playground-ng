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
