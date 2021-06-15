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
