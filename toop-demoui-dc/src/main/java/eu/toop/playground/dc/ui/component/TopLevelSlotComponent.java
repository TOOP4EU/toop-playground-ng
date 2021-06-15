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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.PropertyId;

import eu.toop.playground.dc.ui.model.ConceptQueryFVBean;
import eu.toop.playground.dc.ui.model.DocumentQueryFVBean;
import eu.toop.playground.dc.ui.presenter.MainPresenter;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class TopLevelSlotComponent extends Composite<VerticalLayout> {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TopLevelSlotComponent.class.getName());

    private Div DSDQueryBox;
    private Div fetchBox;
    private Button fetchDSD;

    // Top level slots
    @PropertyId("procedure")
    TextField procedure = new TextField("Procedure: ");

    @PropertyId("dataProviderCountry")
    TextField dataProviderCountry = new TextField("Data Provider Country: ");

    @PropertyId("datasetIdentifier")
    ComboBox<String> datasetIdentifier = new ComboBox<>();
    BinderValidationStatus<ConceptQueryFVBean> statusConcept;
    BinderValidationStatus<DocumentQueryFVBean> statusDocument;
    BeanValidationBinder<ConceptQueryFVBean> binderConceptQuery;
    BeanValidationBinder<DocumentQueryFVBean> binderDocumentQuery;

    ConceptQueryFVBean conceptQueryFVBean;
    DocumentQueryFVBean documentQueryFVBean;

    public TopLevelSlotComponent(
            MainPresenter presenter,
            ConceptQueryFVBean conceptQueryFVBean,
            ConceptQueryComponent conceptQueryComponent) {
        this.conceptQueryFVBean = conceptQueryFVBean;
        getContent().setSpacing(false);
        getContent().setPadding(false);
        DSDQueryBox = new Div();
        fetchBox = new Div();
        FormLayout DSDQueryLayout = new FormLayout();
        procedure.getStyle().set("width", "48%");
        getContent().setWidthFull();
        DSDQueryBox.setClassName("DSDQueryBox");
        datasetIdentifier.setItems("REGISTERED_ORGANIZATION_TYPE");
        datasetIdentifier.setPlaceholder("Select: ");
        datasetIdentifier.setLabel("Dataset Type Identifier: ");
        binderConceptQuery = new BeanValidationBinder<>(ConceptQueryFVBean.class, true);
        binderConceptQuery.setBean(conceptQueryFVBean);
        binderConceptQuery.bindInstanceFields(this);

        fetchDSD = new Button("fetch info");
        fetchDSD.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        fetchDSD.addClickListener(
                click -> {
                    try {
                        statusConcept = binderConceptQuery.validate();
                        if (!statusConcept.hasErrors() && !dataProviderCountry.getValue().isEmpty()) {
                            presenter.populateConceptDatasetGrid(conceptQueryComponent, datasetIdentifier.getValue(), dataProviderCountry.getValue());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        fetchBox.add(fetchDSD);

        DSDQueryBox.add(DSDQueryLayout);
        DSDQueryLayout.add(dataProviderCountry, datasetIdentifier, fetchDSD);

        getContent().add(procedure, DSDQueryBox);
    }

    public TopLevelSlotComponent(
            MainPresenter presenter,
            DocumentQueryFVBean documentQueryFVBean,
            DocumentQueryComponent documentQueryComponent) {
        this.documentQueryFVBean = documentQueryFVBean;

        getContent().setSpacing(false);
        getContent().setPadding(false);
        DSDQueryBox = new Div();
        fetchBox = new Div();
        FormLayout DSDQueryLayout = new FormLayout();
        procedure.getStyle().set("width", "48%");
        getContent().setWidthFull();
        DSDQueryBox.setClassName("DSDQueryBox");
        datasetIdentifier.setAllowCustomValue(true);
        datasetIdentifier.setItems("FINANCIAL_RECORD_TYPE");
        datasetIdentifier.setPlaceholder("Select: ");
        datasetIdentifier.setLabel("Dataset Type Identifier: ");
        binderDocumentQuery = new BeanValidationBinder<>(DocumentQueryFVBean.class, true);
        binderDocumentQuery.setBean(documentQueryFVBean);
        binderDocumentQuery.bindInstanceFields(this);

        fetchDSD = new Button("fetch info");
        fetchDSD.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        datasetIdentifier.addCustomValueSetListener(event -> {
            datasetIdentifier.setValue(event.getDetail());
        });
        fetchDSD.addClickListener(
                click -> {
                    try {
                        statusDocument = binderDocumentQuery.validate();
                        if (!statusDocument.hasErrors()) {
                            presenter.populateDistributionGrid(documentQueryComponent, datasetIdentifier.getValue(), dataProviderCountry.getValue());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        fetchBox.add(fetchDSD);

        DSDQueryBox.add(DSDQueryLayout);
        DSDQueryLayout.add(dataProviderCountry, datasetIdentifier, fetchDSD);

        getContent().add(procedure, DSDQueryBox);
    }

    public void resetConceptBean(ConceptQueryFVBean conceptQueryFVBean) {
        this.conceptQueryFVBean = conceptQueryFVBean;
        binderConceptQuery.setBean(conceptQueryFVBean);
        binderConceptQuery.bindInstanceFields(this);
    }

    public void resetDocumentBean(DocumentQueryFVBean documentQueryFVBean) {
        this.documentQueryFVBean = documentQueryFVBean;
        binderDocumentQuery.setBean(documentQueryFVBean);
        binderDocumentQuery.bindInstanceFields(this);
    }


}
