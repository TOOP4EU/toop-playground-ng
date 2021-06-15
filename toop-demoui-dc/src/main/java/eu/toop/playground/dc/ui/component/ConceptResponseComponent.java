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

import eu.toop.edm.model.ConceptPojo;
import eu.toop.playground.dc.ui.model.ConceptResponseFVBean;
import eu.toop.playground.dc.ui.util.Utilities;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ConceptResponseComponent extends Composite<VerticalLayout> {

  H2 conceptHead = new H2("Concepts in Response: ");
  Div conceptBox;

  public ConceptResponseComponent(ConceptResponseFVBean conceptResponseFVBean) {

    conceptBox = new Div();
    conceptBox.setClassName("conceptBox");

    TreeGrid<ConceptPojo> conceptGrid = new TreeGrid<>(ConceptPojo.class);
    conceptGrid.setItems(conceptResponseFVBean.getAllConcepts(), ConceptPojo::getAllChildren);

    conceptGrid.removeAllColumns();
    conceptGrid
        .addHierarchyColumn(ConceptPojo::getName)
        .setHeader("Concept QName")
        .setResizable(true);
    conceptGrid.addColumn(ConceptPojo::getID).setHeader("Concept ID").setResizable(true);
    conceptGrid
        .addColumn(ConceptPojo -> Utilities.getValue(ConceptPojo.getValue()))
        .setHeader("Value")
        .setResizable(true);
    conceptBox.add(conceptGrid);
    getContent().setPadding(false);
    getContent().add(conceptHead, conceptBox);
  }
}
