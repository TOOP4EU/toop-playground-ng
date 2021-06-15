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
package eu.toop.playground.dc.ui.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 1/6/2020. */
@JsonPropertyOrder({
  "participantId",
  "datasetId",
  "distributionFormat",
  "distributionConforms",
  "accessServiceConforms",
  "doctypeId"
})
public class DSDDatasetResponseDto {

  private DSDIDTypeDto participantId;
  private String datasetId;
  private String distributionFormat;
  private String distributionConforms;
  private String accessServiceConforms;
  private DSDIDTypeDto doctypeId;

  @JsonCreator
  public DSDDatasetResponseDto(
      @JsonProperty("participant-id") DSDIDTypeDto participantId,
      @JsonProperty("dataset-id") String datasetId,
      @JsonProperty("distribution-format") String distributionFormat,
      @JsonProperty("distribution-conforms") String distributionConforms,
      @JsonProperty("accessservice-conforms") String accessServiceConforms,
      @JsonProperty("doctype-id") DSDIDTypeDto doctypeId) {
    this.participantId = participantId;
    this.datasetId = datasetId;
    this.distributionFormat = distributionFormat;
    this.distributionConforms = distributionConforms;
    this.accessServiceConforms = accessServiceConforms;
    this.doctypeId = doctypeId;
  }

  public DSDIDTypeDto getParticipantId() {
    return participantId;
  }

  public String getDatasetId() {
    return datasetId;
  }

  public String getDistributionFormat() {
    return distributionFormat;
  }

  public String getDistributionConforms() {
    return distributionConforms;
  }

  public String getAccessServiceConforms() {
    return accessServiceConforms;
  }

  public DSDIDTypeDto getDoctypeId() {
    return doctypeId;
  }

  @Override
  public String toString() {
    return "DSDDatasetResponseDto{"
        + "participantId="
        + participantId
        + ", datasetId='"
        + datasetId
        + '\''
        + ", distributionFormat='"
        + distributionFormat
        + '\''
        + ", distributionConforms='"
        + distributionConforms
        + '\''
        + ", accessServiceConforms='"
        + accessServiceConforms
        + '\''
        + ", doctypeId="
        + doctypeId
        + '}';
  }
}
