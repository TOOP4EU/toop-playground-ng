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
