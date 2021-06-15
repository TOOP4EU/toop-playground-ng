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

import javax.validation.constraints.NotEmpty;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class LegalPersonFVBean {
    @NotEmpty(message = "This field is required")
    private String legalEntityLegalID;
    @NotEmpty(message = "This field is required")
    private String legalEntityLegalIDScheme;
    private String legalEntityIDScheme;
    private String legalEntityID;
    @NotEmpty(message = "This field is required")
    private String legalEntityName;
    private AddressBean address;

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getLegalEntityLegalID() {
        return legalEntityLegalID;
    }

    public void setLegalEntityLegalID(String legalEntityLegalID) {
        this.legalEntityLegalID = legalEntityLegalID;
    }

    public String getLegalEntityID() {
        return legalEntityID;
    }

    public void setLegalEntityID(String legalEntityID) {
        this.legalEntityID = legalEntityID;
    }

    public String getLegalEntityName() {
        return legalEntityName;
    }

    public void setLegalEntityName(String legalEntityName) {
        this.legalEntityName = legalEntityName;
    }

    public String getLegalEntityLegalIDScheme() {
        return legalEntityLegalIDScheme;
    }

    public void setLegalEntityLegalIDScheme(String legalEntityLegalIDScheme) {
        this.legalEntityLegalIDScheme = legalEntityLegalIDScheme;
    }

    public String getLegalEntityIDScheme() {
        return legalEntityIDScheme;
    }

    public void setLegalEntityIDScheme(String legalEntityIDScheme) {
        this.legalEntityIDScheme = legalEntityIDScheme;
    }
}
