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
package eu.toop.playground.dc.ui.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 1/6/2020. */
@JsonPropertyOrder({"success", "datasetDtoList", "issueDateTime", "duration"})
public class DSDResponseDto {

  private boolean success;
  private List<DSDDatasetResponseDto> datasetDtoList;
  private Date issueDateTime;
  private long duration;

  public DSDResponseDto(
      @JsonProperty("success") boolean success,
      @JsonProperty("dataset-responses") List<DSDDatasetResponseDto> datasetDtoList,
      @JsonProperty("invocationDateTime") Date issueDateTime,
      @JsonProperty("invocationDurationMillis") long duration) {
    this.success = success;
    this.datasetDtoList = datasetDtoList;
    this.issueDateTime = issueDateTime;
    this.duration = duration;
  }

  public List<DSDDatasetResponseDto> getDatasetDtoList() {
    if (datasetDtoList == null) {
      datasetDtoList = new ArrayList<>();
    }
    return datasetDtoList;
  }

  public Date getIssueDateTime() {
    return issueDateTime;
  }

  /**
   * The invocation duration in milliseconds.
   *
   * @return
   */
  public long getDuration() {
    return duration;
  }

  public boolean isSuccess() {
    return success;
  }

  @Override
  public String toString() {
    return "DSDResponseDto{"
        + "success="
        + success
        + ", datasetDtoList="
        + datasetDtoList
        + ", issueDateTime="
        + issueDateTime
        + ", duration="
        + duration
        + '}';
  }
}
