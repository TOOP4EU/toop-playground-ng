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

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.dom.ElementFactory;
import eu.toop.edm.model.EToopGenderCode;
import eu.toop.edm.model.EToopIdentifierType;
import eu.toop.playground.dc.ui.model.AddressBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class NaturalPersonComponent extends Composite<FormLayout> {

    @PropertyId("personID")
    TextField personID = new TextField("Person ID: ");

    @PropertyId("personIDScheme")
    ComboBox<String> personIDScheme = new ComboBox<>();

    @PropertyId("personGivenName")
    TextField personGivenName = new TextField("Person Given Name: ");

    @PropertyId("personFamilyName")
    TextField personFamilyName = new TextField("Person Family Name: ");

    @PropertyId("personGenderCode")
    ComboBox<String> personGenderCode = new ComboBox<>();

    @PropertyId("personBirthName")
    TextField personBirthName = new TextField("Person Birth Name: ");

    @PropertyId("personBirthDate")
    DatePicker personBirthDate = new DatePicker("Person Birth Date: ");

    @PropertyId("personPlaceOfBirthAddressPostName")
    TextField personPlaceOfBirthAddressPostName = new TextField("Person Place of Birth: ");

    AddressComponent address;
    H5 personalInformationHeader = new H5("Natural Person Information: ");
    Div addressContainer;
    BeanValidationBinder binderNP;
    BinderValidationStatus<NaturalPersonFVBean> status;
    NaturalPersonFVBean naturalPersonFVBean;

    public NaturalPersonComponent(NaturalPersonFVBean naturalPersonFVBean) {
        this.naturalPersonFVBean = naturalPersonFVBean;
        binderNP = new BeanValidationBinder(NaturalPersonFVBean.class, true);
        binderNP.setBean(naturalPersonFVBean);
        binderNP.bindInstanceFields(this);
        if (naturalPersonFVBean.getAddress() == null) {
            naturalPersonFVBean.setAddress(new AddressBean());
        }
        address = new AddressComponent(naturalPersonFVBean.getAddress());

        personIDScheme.setItems(
                Arrays.stream(EToopIdentifierType.values()).map(EToopIdentifierType::getID).collect(Collectors.toList()));
        personIDScheme.setLabel("Person ID Scheme: ");
        //set default value
        personIDScheme.setValue(EToopIdentifierType.EIDAS.getID());

        H5 addressHeader = new H5("Natural Person Address:");
        addressContainer = new Div(addressHeader, address);
        addressContainer.setClassName("address");

        personGenderCode.setItems(
                Arrays.stream(EToopGenderCode.values()).map(EToopGenderCode::getID).collect(Collectors.toList()));
        personGenderCode.setLabel("Person Gender Code: ");

        getContent().add(personalInformationHeader);
        getContent().getElement().appendChild(ElementFactory.createBr());
        getContent()
                .add(personID,
                        personIDScheme,
                        personGivenName,
                        personFamilyName,
                        personGenderCode,
                        personBirthName,
                        personBirthDate,
                        personPlaceOfBirthAddressPostName);
        getContent().getElement().appendChild(ElementFactory.createBr());
        getContent().add(addressContainer);
    }

    public boolean validate() {
        status = binderNP.validate();
        return status.hasErrors();
    }

    public void setBean(NaturalPersonFVBean naturalPersonFVBean) {
        this.naturalPersonFVBean = naturalPersonFVBean;
        binderNP.setBean(naturalPersonFVBean);
        binderNP.bindInstanceFields(this);

    }

}
