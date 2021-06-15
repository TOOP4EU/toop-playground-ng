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

import java.util.Arrays;
import java.util.stream.Collectors;

import eu.toop.playground.dc.ui.model.LegalPersonFVBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;
import eu.toop.playground.dc.ui.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

import eu.toop.playground.dc.ui.model.ConceptQueryFVBean;
import eu.toop.playground.dc.ui.model.DocumentQueryFVBean;
import eu.toop.playground.dc.ui.model.enums.EQueryDefinition;
import eu.toop.playground.dc.ui.presenter.MainPresenter;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class QueryDefinitionComponent extends Composite<FormLayout> {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(QueryDefinitionComponent.class.getName());

    MainPresenter presenter;

    ConceptQueryComponent conceptQueryComponent;
    DocumentQueryComponent documentQueryComponent;
    RadioButtonGroup<String> queryTypeRadioGroup;

    Div queryBox = new Div();

    public QueryDefinitionComponent(MainPresenter presenter, Div requestPage) {
        this.presenter = presenter;
        queryBox.getStyle().set("width", "95%");
        getContent().setWidthFull();

        queryTypeRadioGroup = new RadioButtonGroup<>();
        queryTypeRadioGroup.setLabel("What is the Query type: ");
        queryTypeRadioGroup.setItems(
                Arrays.stream(EQueryDefinition.values())
                        .map(EQueryDefinition::getQueryDefinition)
                        .collect(Collectors.toList()));
        queryTypeRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        queryTypeRadioGroup.addValueChangeListener(
                event -> {
                    presenter.initQueryType(event);
                });

        queryBox.setClassName("queryBox");
        queryBox.setVisible(false);

        getContent().add(queryTypeRadioGroup);
        getContent().add(queryBox);
    }

    public void renderConceptQuery(ConceptQueryFVBean conceptQueryFVBean) {
        // reset things before rendering
        resetRenderedComponents();
        conceptQueryComponent = new ConceptQueryComponent(presenter, conceptQueryFVBean);
        LOGGER.debug("Render Concept Query: inside QueryDefinition Component");
        queryBox.setVisible(true);

        // add toplevelcomponent

        queryBox.add(conceptQueryComponent);
    }

    public void renderDocumentQuery(DocumentQueryFVBean documentQueryFVBean, String queryDef) {
        // reset if conceptQuery was pressed before:
        resetRenderedComponents();

        documentQueryComponent = new DocumentQueryComponent(presenter, queryDef, documentQueryFVBean);
        queryBox.setVisible(true);
        queryBox.add(documentQueryComponent);

        LOGGER.debug("Render Document Query: inside QueryDefinition Component");
    }

    public boolean conceptQueryComponentExists(ConceptQueryComponent conceptQueryComponent) {
        return conceptQueryComponent != null;
    }

    public boolean documentQueryComponentExists(DocumentQueryComponent documentQueryComponent) {
        return documentQueryComponent != null;
    }

    public void resetRenderedComponents() {
        // reset if documentQuery was pressed before:
        if (documentQueryComponentExists(documentQueryComponent)) {
            queryBox.remove(documentQueryComponent);
        }

        if (conceptQueryComponentExists(conceptQueryComponent)) {
            queryBox.remove(conceptQueryComponent);
        }
    }

    /* broken bindings after resend request fix*/
    public void setBeans(ConceptQueryFVBean conceptQueryFVBean, DocumentQueryFVBean documentQueryFVBean) {
        if (Utilities.conceptQueryComponentExists(conceptQueryComponent)) {
            conceptQueryComponent.resetConceptBean(conceptQueryFVBean);
        }
        if (Utilities.documentQueryComponentExists(documentQueryComponent)) {
            documentQueryComponent.resetDocumentBean(documentQueryFVBean);
        }

    }

}
