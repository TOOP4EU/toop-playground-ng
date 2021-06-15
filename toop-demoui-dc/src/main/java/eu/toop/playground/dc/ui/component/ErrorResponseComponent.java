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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;

import eu.toop.edm.error.EDMExceptionPojo;
import eu.toop.playground.dc.ui.model.ErrorResponseFVBean;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ErrorResponseComponent extends Composite<VerticalLayout> {
  H2 errorHead = new H2("Error Exceptions:  ");
  Div errorBox;

  public ErrorResponseComponent(ErrorResponseFVBean errorResponseFVBean) {
    errorBox = new Div();
    errorBox.setClassName("errorBox");

    TreeGrid<EDMExceptionPojo> exceptionGrid = new TreeGrid<>(EDMExceptionPojo.class);
    exceptionGrid.setItems(errorResponseFVBean.getExceptionList());
    exceptionGrid.removeAllColumns();
    exceptionGrid.addColumn(EDMExceptionPojo::getErrorCode).setHeader("Error Code").setResizable(true);
    exceptionGrid.addColumn(EDMExceptionPojo::getExceptionType).setHeader("Error ExceptionType").setResizable(true);
    exceptionGrid.addColumn(EDMExceptionPojo::getErrorOrigin).setHeader("Error Origin").setResizable(true);
    exceptionGrid.addColumn(EDMExceptionPojo::getErrorMessage).setHeader("Error Message").setResizable(true);
    exceptionGrid.addColumn(EDMExceptionPojo::getSeverity).setHeader("Error Severity").setResizable(true);
    exceptionGrid.addColumn(EDMExceptionPojo::getTimestamp).setHeader("Error Timestamp").setResizable(true);
    exceptionGrid.addColumn(EDMExceptionPojo::getErrorDetails).setHeader("Error Details").setResizable(true);

    errorBox.add(exceptionGrid);
    getContent().setPadding(false);
    getContent().add(errorHead, errorBox);
  }
}
