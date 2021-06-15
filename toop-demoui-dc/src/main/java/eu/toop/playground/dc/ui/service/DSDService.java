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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.toop.playground.dc.config.enums.DCConfig;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;
import eu.toop.playground.dc.ui.model.dto.DSDIDTypeDto;
import eu.toop.playground.dc.ui.model.dto.DSDResponseDto;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 * @author Konstantinos Raptis [kraptis at unipi.gr]
 */
public class DSDService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DSDService.class.getName());

  /**
   * Query the DSD by doctype, and then transform the result to "something simpler"
   *
   * @param identifier The dataset type identifier.
   * @param country The country id (ISO-2A)..
   * @return
   */
  public List<DSDDatasetResponseDto> getDatasets(final String identifier, final String country)
      throws IOException {
    return DCConfig.INSTANCE.useMockDSD()
        ? getMockDatasets(identifier)
        : getSimDatasets(identifier, country).getDatasetDtoList();
  }

  /**
   * Query the DSD by doctype + country
   *
   * @param identifier The dataset type identifier.
   * @return
   */
  public List<DSDDatasetResponseDto> getDatasets(final String identifier) throws IOException {
    return DCConfig.INSTANCE.useMockDSD()
        ? getMockDatasets(identifier)
        : getSimDatasets(identifier).getDatasetDtoList();
  }

  /**
   * @param identifier The dataset type identifier.
   * @return
   * @throws IOException
   */
  DSDResponseDto getSimDatasets(final String identifier) throws IOException {
    return getSimDatasets(identifier, null);
  }

  /**
   * @param identifier The dataset type identifier.
   * @param country The country id (ISO-2A).
   * @return
   * @throws IOException
   */
  DSDResponseDto getSimDatasets(final String identifier, @Nullable final String country) throws IOException {

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

      final String url =
          Objects.isNull(country)
              ? DCConfig.INSTANCE.getDSDEndpointURL() + "/" + identifier
              : DCConfig.INSTANCE.getDSDEndpointURL()
                  + "/"
                  + identifier
                  + "/by-country/"
                  + country;

      final HttpGet httpGet = new HttpGet(url);
      httpGet.setHeader("Accept", "application/json");

      final ResponseHandler<DSDResponseDto> responseHandler =
          httpResponse -> {
            final int status = httpResponse.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
              final HttpEntity entity = httpResponse.getEntity();

              if (entity != null) {
                LOGGER.debug("Entity is not null, parsing json...");

                final ObjectMapper mapper = new ObjectMapper();
                final DSDResponseDto dsdResponseDto =
                    mapper.readerFor(DSDResponseDto.class).readValue(entity.getContent());

                /* Print JSON as String */
                // String sJSON =
                //     mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dsdResponseDto);
                // LOGGER.debug(sJSON);
                return dsdResponseDto;

              } else {
                LOGGER.debug("The DSD Response has no Entity.");
                throw new IOException("The DSD Response has no Entity.");
              }

            } else {
              throw new ClientProtocolException(
                  "Unable to get requested datasets. Unexpected response status: "
                      + status
                      + " from "
                      + DCConfig.INSTANCE.getDSDEndpointURL());
            }
          };

      return httpClient.execute(httpGet, responseHandler);
    }
  }

  /**
   * @param identifier The dataset type identifier.
   * @return
   */
  private List<DSDDatasetResponseDto> getMockDatasets(final String identifier) {
    final DSDResponseDto mock1 = createSimpleDSDExample();
    final DSDResponseDto mock2 = createSimpleDSDExample();

    final List<DSDDatasetResponseDto> dsdDatasetResponseDtoList = new ArrayList<>();
    dsdDatasetResponseDtoList.addAll(mock1.getDatasetDtoList());
    dsdDatasetResponseDtoList.addAll(mock2.getDatasetDtoList());

    return dsdDatasetResponseDtoList;
  }

  /**
   * @param identifier The dataset type identifier.
   * @return
   */
  private List<DSDDatasetResponseDto> getConceptDataset(final String identifier) {
    return getMockDatasets(identifier);
  }

  private DSDResponseDto createSimpleDSDExample() {
    final DSDIDTypeDto participantId = new DSDIDTypeDto("iso6523-actorid-upis", "9999:elonia");
    final DSDIDTypeDto doctypeId =
        new DSDIDTypeDto(
            "toop-doctypeid-qns",
            "RegisteredOrganization::REGISTERED_ORGANIZATION_TYPE::CONCEPT##CCCEV::toop-edm:v2.1");

    final DSDDatasetResponseDto dsdDatasetResponseDto =
        new DSDDatasetResponseDto(
            participantId,
            "RegisteredOrganization",
            "CONCEPT",
            "CCCEV",
            "toop-edm:v2.1",
            doctypeId);

    final List<DSDDatasetResponseDto> dsdDatasetResponseDtoList = new ArrayList<>();
    dsdDatasetResponseDtoList.add(dsdDatasetResponseDto);

    final DSDResponseDto dsdResponseDto =
        new DSDResponseDto(true, dsdDatasetResponseDtoList, new Date(), 100);

    return dsdResponseDto;
  }

  public DSDResponseDto createConceptDSDExample() {
    return createSimpleDSDExample();
  }
}
