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
package eu.toop.playground.dc.ui.component;

import com.vaadin.flow.component.combobox.ComboBox;
import eu.toop.edm.model.EToopIdentifierType;
import eu.toop.playground.dc.ui.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.dom.ElementFactory;

import eu.toop.playground.dc.ui.model.AddressBean;
import eu.toop.playground.dc.ui.model.LegalPersonFVBean;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class LegalPersonComponent extends Composite<FormLayout> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegalPersonComponent.class);
    private LegalPersonFVBean legalPersonFVBean;

    @PropertyId("legalEntityLegalID")
    TextField legalEntityLegalID = new TextField("Legal Entity Legal ID: ");

    @PropertyId("legalEntityLegalIDScheme")
    ComboBox<String> legalEntityLegalIDScheme = new ComboBox<>();

    @PropertyId("legalEntityID")
    TextField legalEntityID = new TextField("Legal Entity ID: ");

    @PropertyId("legalEntityIDScheme")
    ComboBox<String> legalEntityIDScheme = new ComboBox<>();

    @PropertyId("legalEntityName")
    TextField legalEntityName = new TextField("Company Name: ");

    AddressComponent address;
    H5 legalInformationHeader = new H5("Legal Person Information: ");
    Div addressContainer;
    BeanValidationBinder<LegalPersonFVBean> binderLP;
    BinderValidationStatus<LegalPersonFVBean> status;

    public LegalPersonComponent(LegalPersonFVBean legalPersonFVBean) {
        this.legalPersonFVBean = legalPersonFVBean;
        binderLP = new BeanValidationBinder<>(LegalPersonFVBean.class, true);
        binderLP.setBean(legalPersonFVBean);
        binderLP.bindInstanceFields(this);

        if (legalPersonFVBean.getAddress() == null) {
            legalPersonFVBean.setAddress(new AddressBean());
        }
        address = new AddressComponent(legalPersonFVBean.getAddress());

        legalEntityLegalIDScheme.setItems(
                Arrays.stream(EToopIdentifierType.values()).map(EToopIdentifierType::getID).collect(Collectors.toList()));
        legalEntityLegalIDScheme.setLabel("Legal Entity Legal Scheme ID: ");
        // set default value
        legalEntityLegalIDScheme.setValue(EToopIdentifierType.VATREGISTRATION.getID());

        legalEntityIDScheme.setItems(
                Arrays.stream(EToopIdentifierType.values()).map(EToopIdentifierType::getID).collect(Collectors.toList()));
        legalEntityIDScheme.setLabel("Legal Entity Scheme ID: ");
        legalEntityIDScheme.setValue(EToopIdentifierType.EIDAS.getID());

        H5 addressHeader = new H5("Legal Person Address:");
        addressContainer = new Div(addressHeader, address);
        addressContainer.setClassName("address");

        getContent().add(legalInformationHeader);
        getContent().getElement().appendChild(ElementFactory.createBr());
        getContent().add(legalEntityLegalID, legalEntityLegalIDScheme, legalEntityID, legalEntityIDScheme, legalEntityName);
        getContent().getElement().appendChild(ElementFactory.createBr());
        getContent().add(addressContainer);
    }

    public boolean validate() {
        status = binderLP.validate();
        return status.hasErrors();
    }

    public void setBean(LegalPersonFVBean legalPersonFVBean) {
        this.legalPersonFVBean = legalPersonFVBean;
        binderLP.setBean(legalPersonFVBean);
        binderLP.bindInstanceFields(this);

    }
}
