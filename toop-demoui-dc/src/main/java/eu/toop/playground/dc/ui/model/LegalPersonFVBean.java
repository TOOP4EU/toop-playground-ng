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
