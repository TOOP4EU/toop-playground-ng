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

import eu.toop.edm.model.ConceptPojo;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ConceptResponseFVBean {

  private String dataProviderID;
  private String dataProviderName;
  private String requestID;
  private List<ConceptPojo> concepts;
  private ConceptPojo rootConcept;
  private List<ConceptPojo> allConcepts;

  public List<ConceptPojo> getAllConcepts() {
    return allConcepts;
  }

  public void setAllConcepts(List<ConceptPojo> allConcepts) {
    this.allConcepts = allConcepts;
  }

  public ConceptPojo getRootConcept() {
    return rootConcept;
  }

  public void setRootConcept(ConceptPojo rootConcept) {
    this.rootConcept = rootConcept;
  }

  public List<ConceptPojo> getConcepts() {
    return concepts;
  }

  public void setConcepts(List<ConceptPojo> concepts) {
    this.concepts = concepts;
  }

  public String getDataProviderID() {
    return dataProviderID;
  }

  public void setDataProviderID(String dataProviderID) {
    this.dataProviderID = dataProviderID;
  }

  public String getDataProviderName() {
    return dataProviderName;
  }

  public void setDataProviderName(String dataProviderName) {
    this.dataProviderName = dataProviderName;
  }

  public String getRequestID() {
    return requestID;
  }

  public void setRequestID(String requestID) {
    this.requestID = requestID;
  }
}
