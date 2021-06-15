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

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class NaturalPersonFVBean {
    @NotEmpty(message = "This field is required")
    private String personID;
    @NotEmpty(message = "This field is required")
    private String personIDScheme;
    @NotEmpty(message = "This field is required")
    private String personFamilyName;
    @NotEmpty(message = "This field is required")
    private String personGivenName;
    private String personGenderCode;
    private String personBirthName;
    @NotNull(message = "This field is required")
    private LocalDate personBirthDate;
    private String personPlaceOfBirthAddressPostName;
    private boolean hasErrors = true;

    private AddressBean address;

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getPersonFamilyName() {
        return personFamilyName;
    }

    public void setPersonFamilyName(String personFamilyName) {
        this.personFamilyName = personFamilyName;
    }

    public String getPersonGivenName() {
        return personGivenName;
    }

    public void setPersonGivenName(String personGivenName) {
        this.personGivenName = personGivenName;
    }

    public String getPersonGenderCode() {
        return personGenderCode;
    }

    public void setPersonGenderCode(String personGenderCode) {
        this.personGenderCode = personGenderCode;
    }

    public String getPersonBirthName() {
        return personBirthName;
    }

    public void setPersonBirthName(String personBirthName) {
        this.personBirthName = personBirthName;
    }

    public LocalDate getPersonBirthDate() {
        return personBirthDate;
    }

    public void setPersonBirthDate(LocalDate personBirthDate) {
        this.personBirthDate = personBirthDate;
    }

    public String getPersonPlaceOfBirthAddressPostName() {
        return personPlaceOfBirthAddressPostName;
    }

    public void setPersonPlaceOfBirthAddressPostName(String personPlaceOfBirthAddressPostName) {
        this.personPlaceOfBirthAddressPostName = personPlaceOfBirthAddressPostName;
    }

    public String getPersonIDScheme() {
        return personIDScheme;
    }

    public void setPersonIDScheme(String personIDScheme) {
        this.personIDScheme = personIDScheme;
    }
}
