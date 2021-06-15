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
package eu.toop.playground.dc.ui.util;

import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.grid.Grid;

import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptValuePojo;
import eu.toop.edm.model.PersonPojo;
import eu.toop.playground.dc.ui.component.*;
import eu.toop.playground.dc.ui.model.AddressBean;
import eu.toop.playground.dc.ui.model.LegalPersonFVBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class Utilities {

    public static boolean authorizedRepresentativeExistsAndNotEmpty(final NaturalPersonFVBean authorizedRepresentative) {
        if (NPExists(authorizedRepresentative)) {
            return authorizedRepresentative.getPersonID() != null;
        } else {
            return false;
        }
    }

    public static boolean legalPersonExistsAndNotEmpty(final LegalPersonFVBean legalPerson) {
        if (LPExists(legalPerson)) {
            return legalPerson.getLegalEntityID() != null;
        } else {
            return false;
        }
    }

    public static boolean naturalPersonExistsAndNotEmpty(final NaturalPersonFVBean naturalPerson) {
        if (NPExists(naturalPerson)) {
            return naturalPerson.getPersonID() != null;
        } else {
            return false;
        }
    }

    public static boolean legalPersonComponentExists(final LegalPersonComponent legalPersonComponent) {
        return legalPersonComponent != null;
    }

    public static boolean naturalPersonComponentExists(final NaturalPersonComponent naturalPersonComponent) {
        return naturalPersonComponent != null;
    }

    public static boolean conceptQueryComponentExists(final ConceptQueryComponent conceptQueryComponent) {
        return conceptQueryComponent != null;
    }

    public static boolean documentQueryComponentExists(final DocumentQueryComponent documentQueryComponent) {
        return documentQueryComponent != null;
    }

    public static boolean naturalPersonAddressExists(final AddressBean naturalPersonAddressFVBean) {
        return naturalPersonAddressFVBean != null && naturalPersonAddressFVBean.getAddressFullAddress().equals("");
    }

    public static boolean legalPersonAddressExists(final AddressBean addressBean) {
        return addressBean != null && addressBean.getAddressFullAddress().equals("");
    }

    public static boolean dataProviderDetailsComponentExists(
            final DataProviderDetailsComponent dataProviderDetailsComponent) {
        return dataProviderDetailsComponent != null;
    }

    public static boolean conceptResponseComponentExists(final ConceptResponseComponent conceptResponseComponent) {
        return conceptResponseComponent != null;
    }

    public static boolean errorResponseComponentExists(final ErrorResponseComponent errorResponseComponent) {
        return errorResponseComponent != null;
    }

    public static boolean documentResponseComponentExists(final DocumentResponseComponent documentResponseComponent) {
        return documentResponseComponent != null;
    }

    public static boolean datasetGridExists(Grid<DSDDatasetResponseDto> grid) {
        return grid != null;
    }

    public static boolean personPojoExists(PersonPojo personPojo) {
        return personPojo != null;
    }

    public static boolean businessPojoExists(BusinessPojo businessPojo) {
        return businessPojo != null;
    }

    public static boolean topLevelSlotComponentExists(TopLevelSlotComponent topLevelSlotComponent) {
        return topLevelSlotComponent != null;
    }


    public static boolean NPExists(final NaturalPersonFVBean naturalPerson) {
        return naturalPerson != null;
    }

    public static boolean LPExists(final LegalPersonFVBean legalPerson) {
        return legalPerson != null;
    }

    public static boolean NPAddressExists(final AddressBean addressFVBean) {
        return addressFVBean != null;
    }

    public static boolean LPAddressExists(final AddressBean addressFVBean) {
        return addressFVBean != null;
    }

    public static void printNaturalPersonDetails(final NaturalPersonFVBean naturalPersonFVBean) {
        if (naturalPersonExistsAndNotEmpty(naturalPersonFVBean)) {
            System.out.println("==================== NATURAL PERSON DETAILS =============================");
            System.out.println("PersonID: " + naturalPersonFVBean.getPersonID());
            System.out.println("PersonGivenName: " + naturalPersonFVBean.getPersonGivenName());
            System.out.println("PersonFamilyName: " + naturalPersonFVBean.getPersonFamilyName());
            System.out.println("PersonGenderCode: " + naturalPersonFVBean.getPersonGenderCode());
            System.out.println("PersonBirthName: " + naturalPersonFVBean.getPersonBirthName());
            System.out.println("PersonBirthDate: " + naturalPersonFVBean.getPersonBirthDate());
            System.out.println("PersonPlaceOfBirth: " + naturalPersonFVBean.getPersonPlaceOfBirthAddressPostName());
        } else {
            System.out.println("Natural Person is empty or null");
        }
    }

    public static void printNaturalPersonAddressDetails(final AddressBean naturalPersonAddressFVBean) {
        if (naturalPersonAddressExists(naturalPersonAddressFVBean)) {
            System.out.println("==================== NATURAL PERSON ADDRESS DETAILS =============================");
            System.out.println("Person Full Address: " + naturalPersonAddressFVBean.getAddressFullAddress());
            System.out.println("Person Street: " + naturalPersonAddressFVBean.getAddressThoroughfare());
            System.out.println("Person Street Number: " + naturalPersonAddressFVBean.getAddressLocatorDesignator());
            System.out.println("Person Post Name: " + naturalPersonAddressFVBean.getAddressPostName());
            System.out.println("Person Post Code: " + naturalPersonAddressFVBean.getAddressPostCode());
            System.out.println("Person Country: " + naturalPersonAddressFVBean.getAddressAdminUnitFirstline());
        } else {
            System.out.println("Natural Person Address is empty or null");
        }
    }

    public static void printLegalPersonDetails(final LegalPersonFVBean legalPersonFVBean) {
        if (legalPersonExistsAndNotEmpty(legalPersonFVBean)) {
            System.out.println("==================== LEGAL PERSON ADDRESS DETAILS =============================");
            System.out.println("Legal Person Entity Legal ID: " + legalPersonFVBean.getLegalEntityLegalID());
            System.out.println("Legal Person Entity Legal ID SCHEME: " + legalPersonFVBean.getLegalEntityLegalIDScheme());
            System.out.println("Legal Person Entity ID: " + legalPersonFVBean.getLegalEntityID());
            System.out.println("Legal Person Entity Name: " + legalPersonFVBean.getLegalEntityName());
        } else {
            System.out.println("Legal person is empty or null");
        }
    }

    public static void printLegalPersonAddressDetails(final AddressBean addressBean) {
        if (legalPersonAddressExists(addressBean)) {
            System.out.println("==================== LEGAL PERSON ADDRESS DETAILS =============================");
            System.out.println("Legal Person Full Address: " + addressBean.getAddressFullAddress());
            System.out.println("Legal Person Street: " + addressBean.getAddressThoroughfare());
            System.out.println("Legal Person Street Number: " + addressBean.getAddressLocatorDesignator());
            System.out.println("Legal Person Post Name: " + addressBean.getAddressPostName());
            System.out.println("Legal Person Post Code: " + addressBean.getAddressPostCode());
            System.out.println("Legal Person Country: " + addressBean.getAddressAdminUnitFirstline());
        } else {
            System.out.println("Legal person Address is empty or null");
        }
    }

    public static String getValue(final ConceptValuePojo conceptValuePojo) {
        if (conceptValuePojo != null) {
            if (conceptValuePojo.getBoolean() != null)
                return conceptValuePojo.getBoolean().toString();
            if (!conceptValuePojo.text().isEmpty())
                return conceptValuePojo.text().get(0);
            if (conceptValuePojo.getAmount() != null)
                return "Currency ID: " + conceptValuePojo.getAmount().getCurrencyID() +
                        " Value:" +
                        conceptValuePojo.getAmount().getValue();
            if (conceptValuePojo.getCode() != null)
                return conceptValuePojo.getCode();
            if (conceptValuePojo.getDate() != null)
                return conceptValuePojo.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            if (conceptValuePojo.getErrorCode() != null)
                return conceptValuePojo.getErrorCode();
            if (conceptValuePojo.getIdentifier() != null)
                return conceptValuePojo.getIdentifier();
            if (conceptValuePojo.getMeasure() != null)
                return "Unit Code: " + conceptValuePojo.getMeasure().getUnitCode() +
                        " Value:" +
                        conceptValuePojo.getMeasure().getValue();
            if (conceptValuePojo.getNumeric() != null)
                return conceptValuePojo.getNumeric().toPlainString();
            if (conceptValuePojo.getPeriod() != null)
                return "Start Date: " + conceptValuePojo.getPeriod().getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE) +
                        " End Date:" +
                        conceptValuePojo.getPeriod().getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            if (conceptValuePojo.getQuantity() != null)
                return "Unit Code: " + conceptValuePojo.getQuantity().getUnitCode() +
                        " Value:" +
                        conceptValuePojo.getQuantity().getValue();
            if (conceptValuePojo.getTime() != null)
                return conceptValuePojo.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
            if (conceptValuePojo.getURI() != null)
                return conceptValuePojo.getURI();
        }
        return "";
    }
}
