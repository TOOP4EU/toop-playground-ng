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

import java.util.Objects;

import javax.validation.constraints.NotNull;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;
import eu.toop.playground.dc.ui.model.enums.EResponseStatus;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 18/5/2020. */
public class ResultBean {

  private EDMRequest request;
  private EDMResponse response;
  private EDMErrorResponse errorResponse;
  private EDMResponseWithAttachment responseWithAttachment;
  private EResponseStatus status;
  private String dpIdentifier;
  private DSDDatasetResponseDto dsdDTO;

  public DSDDatasetResponseDto getDsdDTO() {
    return dsdDTO;
  }

  public void setDsdDTO(DSDDatasetResponseDto dsdDTO) {
    this.dsdDTO = dsdDTO;
  }

  public ResultBean(@NotNull EDMRequest request) {
    this.request = request;
    this.status = EResponseStatus.PENDING;
  }

  public ResultBean(
      @NotNull EDMRequest request, @NotNull DSDDatasetResponseDto dsdDatasetResponseDto) {
    this.request = request;
    this.status = EResponseStatus.PENDING;
    this.dpIdentifier = dsdDatasetResponseDto.getParticipantId().getValue();
    this.dsdDTO = dsdDatasetResponseDto;
  }

  public String getDpIdentifier() {
    return dpIdentifier;
  }

  public void setDpIdentifier(String dpIdentifier) {
    this.dpIdentifier = dpIdentifier;
  }

  public EDMRequest getRequest() {
    return request;
  }

  public void setRequest(EDMRequest request) {
    this.request = request;
  }

  public EDMResponse getResponse() {
    return response;
  }

  public void setResponse(EDMResponse response) {
    this.response = response;
  }

  public EResponseStatus getStatus() {
    return status;
  }

  public void setStatus(EResponseStatus status) {
    this.status = status;
  }

  public void setErrorResponse(EDMErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
  }

  public EDMErrorResponse getErrorResponse() {
    return errorResponse;
  }

  public void setResponseWithAttachment(EDMResponseWithAttachment responseWithAttachment) {
    this.responseWithAttachment = responseWithAttachment;
  }

  public EDMResponseWithAttachment getResponseWithAttachment() {
    return responseWithAttachment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    final ResultBean other = (ResultBean) o;

    return Objects.equals(this.request.getRequestID(), other.getRequest().getRequestID());
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 29 * hash + Objects.hashCode(this.request.getRequestID());
    return hash;
  }
}
