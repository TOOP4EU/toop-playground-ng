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

import eu.toop.edm.EDMResponse;
import eu.toop.edm.model.DatasetPojo;
/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DocumentResponseFVBean {
    private String dataProviderID;
    private String dataProviderName;
    private String requestID;
    private DatasetPojo dataset;
    private List<DatasetPojo> datasetList;
    private EDMResponse response;
    private EDMResponseWithAttachment responseWithAttachment;

    public EDMResponseWithAttachment getResponseWithAttachment() {
        return responseWithAttachment;
    }

    public void setResponseWithAttachment(EDMResponseWithAttachment responseWithAttachment) {
        this.response = responseWithAttachment.getEdmResponse();
        this.responseWithAttachment = responseWithAttachment;
    }

    public EDMResponse getResponse() {
        return response;
    }

    public void setResponse(EDMResponse response) {
        this.responseWithAttachment = null;
        this.response = response;
    }

    public List<DatasetPojo> getDatasetList() {
        return datasetList;
    }

    public void setDatasetList(List<DatasetPojo> datasetList) {
        this.datasetList = datasetList;
    }

    public DatasetPojo getDataset() {
        return dataset;
    }

    public void setDataset(DatasetPojo dataset) {
        this.dataset = dataset;
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
