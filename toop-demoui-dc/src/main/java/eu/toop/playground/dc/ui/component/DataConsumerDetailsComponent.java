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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import eu.toop.playground.dc.ui.util.MessageCreation;
/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DataConsumerDetailsComponent extends Composite<VerticalLayout> {
    // Data Consumer Details
    H2 dcHeader = new H2("Data Consumer Details: ");
    Div dataConsumerBox = new Div();
    Paragraph dcName;
    Paragraph dcID;
    Paragraph dcFullAddress;

    public DataConsumerDetailsComponent() {
        dataConsumerBox.setClassName("dataConsumerBox");
        dcName =  new Paragraph("Data Consumer Name: " + MessageCreation.createDataConsumer().getName());
        dcID =  new Paragraph("Data Consumer ID: " + MessageCreation.createDataConsumer().getID());
        dcFullAddress = new Paragraph(
                "Data Consumer FullAddress: "
                        + MessageCreation.createDataConsumer().getAddress().getFullAddress());

        dataConsumerBox.add(dcName, dcID, dcFullAddress);

        getContent().setPadding(false);
        getContent().add(dcHeader, dataConsumerBox);
    }
}
