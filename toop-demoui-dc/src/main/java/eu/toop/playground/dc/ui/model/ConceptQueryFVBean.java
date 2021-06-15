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

import javax.validation.constraints.NotEmpty;
import javax.xml.namespace.QName;

import eu.toop.edm.CToopEDM;
/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ConceptQueryFVBean {
  private String procedure;
  @NotEmpty(message = "This field is required")
  private String datasetIdentifier;
  @NotEmpty(message = "This field is required")
  private String dataProviderCountry;
  private String consentToken = "MTExMDEwMTAxMDEwMTAwMDExMTAxMDE=";
  private String specificationIdentifier = CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V21;
  private List<QName> conceptRequestList;

  public String getDataProviderCountry() {
    return dataProviderCountry;
  }

  public void setDataProviderCountry(final String dataProviderCountry) {
    this.dataProviderCountry = dataProviderCountry;
  }

  public List<QName> getConceptRequestList() {
    return conceptRequestList;
  }

  public void setConceptRequestList(final List<QName> conceptRequestList) {
    this.conceptRequestList = conceptRequestList;
  }

  public String getProcedure() {
    return procedure;
  }

  public void setProcedure(final String procedure) {
    this.procedure = procedure;
  }

  public String getDatasetIdentifier() {
    return datasetIdentifier;
  }

  public void setDatasetIdentifier(final String datasetIdentifier) {
    this.datasetIdentifier = datasetIdentifier;
  }

  public String getConsentToken() {
    return consentToken;
  }

  public void setConsentToken(final String consentToken) {
    this.consentToken = consentToken;
  }

  public String getSpecificationIdentifier() {
    return specificationIdentifier;
  }

  public void setSpecificationIdentifier(final String specificationIdentifier) {
    this.specificationIdentifier = specificationIdentifier;
  }
}
