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
