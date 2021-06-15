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

import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.playground.dc.ui.model.AddressBean;
import eu.toop.playground.dc.ui.model.LegalPersonFVBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class MessageCreation {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MessageCreation.class.getName());

    public static AgentPojo createDataConsumer() {
        return AgentPojo.builder()
                .address(
                        AddressPojo.builder()
                                .town("FreedoniaTown")
                                .streetName("FreedoniaStreet")
                                .buildingNumber("55")
                                .countryCode("PF")
                                .fullAddress("FreedoniaStreet 55, 11155, FreedoniaTown, PF")
                                .postalCode("11134")
                                .build())
                .name("Freedonia Data Consumer System")
                .id("PF730757727")
                .idSchemeID("VATRegistration")
                .build();
    }

    public static PersonPojo createNaturalPerson(
            NaturalPersonFVBean naturalPersonFVBean, AddressBean naturalPersonAddressFVBean) {
        return PersonPojo.builder()
                .address(
                        AddressPojo.builder()
                                .town(naturalPersonAddressFVBean.getAddressPostName())
                                .streetName(naturalPersonAddressFVBean.getAddressThoroughfare())
                                .buildingNumber(naturalPersonAddressFVBean.getAddressLocatorDesignator())
                                .countryCode(naturalPersonAddressFVBean.getAddressAdminUnitFirstline())
                                .fullAddress(naturalPersonAddressFVBean.getAddressFullAddress())
                                .postalCode(naturalPersonAddressFVBean.getAddressPostCode())
                                .build())
                .birthDate(naturalPersonFVBean.getPersonBirthDate())
                .birthTown(naturalPersonFVBean.getPersonPlaceOfBirthAddressPostName())
                .birthName(naturalPersonFVBean.getPersonBirthName())
                .familyName(naturalPersonFVBean.getPersonFamilyName())
                .genderCode(naturalPersonFVBean.getPersonGenderCode())
                .givenName(naturalPersonFVBean.getPersonGivenName())
                .id(naturalPersonFVBean.getPersonID())
                .idSchemeID(naturalPersonFVBean.getPersonIDScheme())
                .build();
    }

    public static BusinessPojo createLegalPerson(
            LegalPersonFVBean legalPersonFVBean, AddressBean addressBean) {
        return BusinessPojo.builder()
                .address(
                        AddressPojo.builder()
                                .town(addressBean.getAddressPostName())
                                .streetName(addressBean.getAddressThoroughfare())
                                .buildingNumber(addressBean.getAddressLocatorDesignator())
                                .countryCode(addressBean.getAddressAdminUnitFirstline())
                                .fullAddress(addressBean.getAddressFullAddress())
                                .postalCode(addressBean.getAddressPostCode())
                                .build())
                .legalID(legalPersonFVBean.getLegalEntityLegalID())
                .legalIDSchemeID(legalPersonFVBean.getLegalEntityLegalIDScheme())
                .legalName(legalPersonFVBean.getLegalEntityName())
                .id(legalPersonFVBean.getLegalEntityID()).idSchemeID(legalPersonFVBean.getLegalEntityIDScheme())
                .build();
    }

    public static ConceptPojo createConceptRequestList(List<String> selectedConceptList) {
        ConceptPojo.Builder conceptPojoBuilder = new ConceptPojo.Builder();

        conceptPojoBuilder
                .randomID()
                .name(EToopConcept.NAMESPACE_URI, EToopConcept.REGISTERED_ORGANIZATION.getID());

        for (String conceptID : selectedConceptList) {
            conceptPojoBuilder.addChild(
                    ConceptPojo.builder().randomID().name(EToopConcept.NAMESPACE_URI, conceptID).build());
        }
        return conceptPojoBuilder.build();
    }

    public static ConceptPojo createConceptRequestListFromQName(List<QName> selectedConceptList) {
        ConceptPojo.Builder conceptPojoBuilder = new ConceptPojo.Builder();

        QName parentQName = null;

        // find parent qName, create parent ConceptPojo
        if (parentQName == null) {
            for (QName qName : selectedConceptList) {
                if (qName.getLocalPart().equals("RegisteredOrganization")) {
                    conceptPojoBuilder.randomID().name(qName);
                    parentQName = qName;
                }
            }
        }

        for (QName name : selectedConceptList) {
            if (name.equals(parentQName)) {
                continue;
            }
            conceptPojoBuilder.addChild(ConceptPojo.builder().randomID().name(name).build());
        }
        return conceptPojoBuilder.build();
    }
}
