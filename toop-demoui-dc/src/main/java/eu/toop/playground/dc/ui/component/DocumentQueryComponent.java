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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.olli.FileDownloadWrapper;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.server.StreamResource;

import eu.toop.edm.EDMRequest;
import eu.toop.playground.dc.ui.model.DocumentQueryFVBean;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;
import eu.toop.playground.dc.ui.model.enums.EQueryDefinition;
import eu.toop.playground.dc.ui.presenter.MainPresenter;
import eu.toop.playground.dc.ui.util.Utilities;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DocumentQueryComponent extends Composite<VerticalLayout> {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DocumentResponseComponent.class.getName());
    private MainPresenter presenter;
    TopLevelSlotComponent topLevelSlotComponent;
    Div distributionSelectionBox = new Div();
    Div messageCreationBox = new Div();
    H3 distributionSelectionHeader = new H3("Data Provider and Distribution Selection: ");
    H3 createDocumentQueryHeader = new H3("Create DocumentQuery:  ");
    Grid<DSDDatasetResponseDto> datasetGrid;
    String selectedQueryDef;
    Button createRequestBtn;

    Button downloadBtn = new Button();
    MultiSelect<Grid<DSDDatasetResponseDto>, DSDDatasetResponseDto> selected;

    public DocumentQueryComponent(
            MainPresenter presenter, String selectedQueryDef, DocumentQueryFVBean documentQueryFVBean) {
        this.presenter = presenter;
        this.selectedQueryDef = selectedQueryDef;
        distributionSelectionBox.setClassName("distributionSelectionBox");
        topLevelSlotComponent = new TopLevelSlotComponent(presenter, documentQueryFVBean, this);

        createRequestBtn = new Button("Create Request");
        createRequestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        createRequestBtn.addClickListener(
                click -> {
                    for (DSDDatasetResponseDto selectedDataset : selected.getSelectedItems()) {
                        if (selectedQueryDef.equals(EQueryDefinition.DOCUMENT.getQueryDefinition())) {
                            presenter.validateDS();
                            if (!presenter.validationCheckPass()) {
                                presenter.createDocumentRequestMessage(this, selectedDataset);
                            } else {
                                final Notification notification =
                                        new Notification(
                                                "Missing Required Fields. Please fill in all the required fields", 9000, Notification.Position.TOP_START);
                                notification.addThemeVariants(
                                        NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                notification.open();
                            }
                        } else if (selectedQueryDef.equals(EQueryDefinition.DOCUMENT_REF.getQueryDefinition())) {
                            presenter.validateDS();
                            if (!presenter.validationCheckPass()) {
                                presenter.createDocumentRefRequestMessage(this, selectedDataset);
                            } else {
                                final Notification notification =
                                        new Notification(
                                                "Missing Required Fields. Please fill in all the required fields", 9000, Notification.Position.TOP_START);
                                notification.addThemeVariants(
                                        NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                                notification.open();
                            }
                        }
                    }


                });


        messageCreationBox.add(createDocumentQueryHeader, createRequestBtn);

        downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        downloadBtn.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));

        getContent().add(topLevelSlotComponent);
    }

    public void renderDatasetList(List<DSDDatasetResponseDto> datasets) {
        if (Utilities.datasetGridExists(datasetGrid)) {
            distributionSelectionBox.remove(datasetGrid);
            distributionSelectionBox.remove(messageCreationBox);
        }
        /* Only toop-edm:2.1.x responses rendered */
        List<DSDDatasetResponseDto> filtered210Datasets = datasets.stream()
                .filter(dsdDatasetResponseDto -> dsdDatasetResponseDto.getAccessServiceConforms().contains("2.1"))
                .collect(Collectors.toList());

        datasetGrid = new Grid<>(DSDDatasetResponseDto.class);
        datasetGrid.setItems(filtered210Datasets);

        datasetGrid.removeAllColumns();
        datasetGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        datasetGrid
                .addColumn(DSDDatasetResponseDto::getDatasetId)
                .setHeader("DatasetIdentifier")
                .setResizable(true);
        datasetGrid
                .addColumn(DSDDatasetResponseDto -> filtered210Datasets.get(0).getParticipantId().getValue().toString())
                .setHeader("ParticipantID")
                .setResizable(true);
        datasetGrid
                .addColumn(DSDDatasetResponseDto -> filtered210Datasets.get(0).getParticipantId().getScheme())
                .setHeader("Participant Scheme ID")
                .setResizable(true);
        datasetGrid
                .addColumn(DSDDatasetResponseDto -> filtered210Datasets.get(0).getDoctypeId().getValue())
                .setHeader("Document Type ID")
                .setResizable(true);
        datasetGrid
                .addColumn(DSDDatasetResponseDto -> filtered210Datasets.get(0).getDoctypeId().getScheme())
                .setHeader("Document Scheme ID")
                .setResizable(true);
        datasetGrid
                .addColumn(DSDDatasetResponseDto::getDistributionFormat)
                .setHeader("Format")
                .setResizable(true);
        datasetGrid
                .addColumn(DSDDatasetResponseDto::getAccessServiceConforms)
                .setHeader("Conforms to")
                .setResizable(true);

        selected = datasetGrid.asMultiSelect();

        selected.addSelectionListener(
                e -> {
                    if (selected.getSelectedItems().size() == 1) {
                        distributionSelectionBox.add(messageCreationBox);
                    } else if (selected.getSelectedItems().size() == 0) {
                        distributionSelectionBox.remove(messageCreationBox);
                    }
                });

        distributionSelectionBox.add(distributionSelectionHeader, datasetGrid);
        getContent().add(distributionSelectionHeader, distributionSelectionBox);
    }

    public void downloadXML(EDMRequest request) {
        FileDownloadWrapper buttonWrapper =
                new FileDownloadWrapper(
                        new StreamResource(
                                "TOOPRequest.xml",
                                () ->
                                        new ByteArrayInputStream(
                                                Objects.requireNonNull(request.getWriter().getAsBytes()))));

        buttonWrapper.wrapComponent(downloadBtn);
        Notification notification = new Notification("Created TOOPRequest Document", 3000, Notification.Position.TOP_START);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
        messageCreationBox.add(buttonWrapper);
    }

    public void resetDocumentBean(DocumentQueryFVBean documentQueryFVBean) {
        topLevelSlotComponent.resetDocumentBean(documentQueryFVBean);
    }

}
