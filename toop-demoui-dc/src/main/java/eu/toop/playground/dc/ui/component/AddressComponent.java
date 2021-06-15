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
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;

import eu.toop.playground.dc.ui.model.AddressBean;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class AddressComponent extends Composite<FormLayout> {

  // Natural Person Address Text Fields
  @PropertyId("addressFullAddress")
  TextField addressFullAddress = new TextField("Full Address: ");

  @PropertyId("addressLocatorDesignator")
  TextField addressLocatorDesignator = new TextField("Street: ");

  @PropertyId("addressThoroughfare")
  TextField addressThoroughfare = new TextField("Number: ");

  @PropertyId("addressPostName")
  TextField addressPostName = new TextField("Post Name: ");

  @PropertyId("addressAdminUnitFirstline")
  TextField addressAdminUnitFirstline = new TextField("Country: ");

  @PropertyId("addressPostCode")
  TextField addressPostCode = new TextField("Post Code: ");

  private final AddressBean addressBean;

  public AddressComponent(AddressBean addressBean) {
    this.addressBean = addressBean;
    Binder<AddressBean> addressBeanBinder = new Binder<>(AddressBean.class, true);
    addressBeanBinder.setBean(addressBean);
    addressBeanBinder.bindInstanceFields(this);

    this.getContent()
        .add(
            addressFullAddress,
            addressLocatorDesignator,
            addressThoroughfare,
            addressPostName,
            addressAdminUnitFirstline,
            addressPostCode);
  }
}
