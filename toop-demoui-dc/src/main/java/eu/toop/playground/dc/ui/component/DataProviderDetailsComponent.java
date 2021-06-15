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

import eu.toop.playground.dc.ui.model.ConceptResponseFVBean;
import eu.toop.playground.dc.ui.model.DocumentResponseFVBean;
import eu.toop.playground.dc.ui.model.ErrorResponseFVBean;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DataProviderDetailsComponent extends Composite<VerticalLayout> {

    H2 dataProviderHeader = new H2("Data Provider Details: ");
    Div dataProviderBox;
    Paragraph dpID;
    Paragraph dpName;
    Paragraph requestID;
    Paragraph responseStatus;
    public DataProviderDetailsComponent(ConceptResponseFVBean conceptResponseFVBean) {
        dataProviderBox = new Div();
        dataProviderBox.setClassName("dataProviderBox");
        dpID = new Paragraph("Data Provider Name: " + conceptResponseFVBean.getDataProviderName());
        dpName =  new Paragraph("Data Provider ID: " + conceptResponseFVBean.getDataProviderID());
        requestID =  new Paragraph("Request ID: " + conceptResponseFVBean.getRequestID());
        dataProviderBox.add(dpID, dpName, requestID);
        getContent().setPadding(false);
        getContent().add(dataProviderHeader, dataProviderBox);
    }

    public DataProviderDetailsComponent(DocumentResponseFVBean documentResponseFVBean) {
        dataProviderBox = new Div();
        dataProviderBox.setClassName("dataProviderBox");
        dpID = new Paragraph("Data Provider Name: " + documentResponseFVBean.getDataProviderName());
        dpName =  new Paragraph("Data Provider ID: " + documentResponseFVBean.getDataProviderID());
        requestID =  new Paragraph("Request ID: " + documentResponseFVBean.getRequestID());
        dataProviderBox.add(dpID, dpName, requestID);
        getContent().setPadding(false);
        getContent().add(dataProviderHeader, dataProviderBox);
    }

    public DataProviderDetailsComponent(ErrorResponseFVBean errorResponseFVBean) {
        dataProviderBox = new Div();
        dataProviderBox.setClassName("dataProviderBox");
        dpID = new Paragraph("Error Provider Name: " + errorResponseFVBean.getErrorProviderName());
        dpName =  new Paragraph("Error Provider ID: " + errorResponseFVBean.getErrorProviderID());
        requestID = new Paragraph("Request ID: " + errorResponseFVBean.getRequestID());
        responseStatus =  new Paragraph("Response Status: " + errorResponseFVBean.getResponseStatus());
        dataProviderBox.add(dpID, dpName, requestID, responseStatus);
        getContent().setPadding(false);
        getContent().add(dataProviderHeader, dataProviderBox);
    }
}
