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
