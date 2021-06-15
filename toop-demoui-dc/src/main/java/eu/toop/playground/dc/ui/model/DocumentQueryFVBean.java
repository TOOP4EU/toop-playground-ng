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

import javax.validation.constraints.NotEmpty;

import eu.toop.edm.CToopEDM;
import eu.toop.edm.model.DistributionPojo;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DocumentQueryFVBean {
    private String procedure;
    @NotEmpty(message = "This field is required")
    private String datasetIdentifier;
    @NotEmpty(message = "This field is required")
    private String dataProviderCountry;
    private String consentToken = "MTExMDEwMTAxMDEwMTAwMDExMTAxMDE=";
    private String specificationIdentifier = CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V21;
    private String documentID;
    private List<DistributionPojo> distributionList;

    public List<DistributionPojo> getDistributionList() {
        return distributionList;
    }

    public void setDistributionList(final List<DistributionPojo> distributionList) {
        this.distributionList = distributionList;
    }

    public String getDataProviderCountry() {
        return dataProviderCountry;
    }

    public void setDataProviderCountry(final String dataProviderCountry) {
        this.dataProviderCountry = dataProviderCountry;
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

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
