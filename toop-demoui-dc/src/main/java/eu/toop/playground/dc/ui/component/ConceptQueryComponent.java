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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import eu.toop.playground.dc.ui.model.DocumentQueryFVBean;
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
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.server.StreamResource;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.playground.dc.ui.model.ConceptQueryFVBean;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;
import eu.toop.playground.dc.ui.presenter.MainPresenter;
import eu.toop.playground.dc.ui.util.Utilities;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ConceptQueryComponent extends Composite<VerticalLayout> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ConceptQueryComponent.class.getName());

    MainPresenter presenter;
    Div conceptSelectionBox = new Div();
    Div messageCreationBox = new Div();
    TopLevelSlotComponent topLevelSlotComponent;
    H3 conceptSelectionHeader = new H3("Concept Selection: ");
    H3 createConceptQueryHeader = new H3("Create ConceptQuery:  ");
    Button createRequestBtn;

    MultiSelect<Grid<DSDDatasetResponseDto>, DSDDatasetResponseDto> selected;

    Div distributionBox;
    H3 distributionSelectionHeader = new H3("Data Provider and Dataset Selection: ");
    Grid<DSDDatasetResponseDto> datasetGrid;

    Button downloadBtn = new Button();
    FileDownloadWrapper buttonWrapper;

    ConceptQueryFVBean conceptQueryFVBean;

    public ConceptQueryComponent(MainPresenter presenter, ConceptQueryFVBean conceptQueryFVBean) {
        this.presenter = presenter;
        this.conceptQueryFVBean = conceptQueryFVBean;
        topLevelSlotComponent = new TopLevelSlotComponent(presenter, conceptQueryFVBean, this);
        messageCreationBox.setClassName("messageCreationBox");

        presenter.populateConceptSelectionGrid(this);

        createRequestBtn = new Button("Create Request");
        createRequestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        createRequestBtn.addClickListener(
                click -> {
                    for (DSDDatasetResponseDto selectedDataset : selected.getSelectedItems()) {
                        presenter.validateDS();
                        if (!presenter.validationCheckPass()) {
                            presenter.createConceptRequestMessage(this, selectedDataset);
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
        );

        messageCreationBox.add(createConceptQueryHeader, createRequestBtn);

        downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        downloadBtn.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));
        getContent().add(topLevelSlotComponent);
    }

    public void renderConceptDatasetList(List<DSDDatasetResponseDto> datasets) {
        if (Utilities.datasetGridExists(datasetGrid)) {
            distributionBox.remove(datasetGrid);
            distributionBox.remove(conceptSelectionBox);
        }

        /* Only toop-edm:2.1.x responses rendered */
        List<DSDDatasetResponseDto> filtered210Datasets = datasets.stream()
                .filter(dsdDatasetResponseDto -> dsdDatasetResponseDto.getAccessServiceConforms().contains("2.1"))
                .collect(Collectors.toList());

        distributionBox = new Div();
        distributionBox.setClassName("distributionSelectionBox");
        LOGGER.debug("ConceptDataset: " + filtered210Datasets.stream().collect(Collectors.toList()));

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

                    LOGGER.debug(Arrays.toString(selected.getSelectedItems().stream().map(x -> x.getParticipantId().getValue()).collect(Collectors.toList()).toArray()));
                    if (selected.getSelectedItems().size() == 1) {
                        distributionBox.add(conceptSelectionBox);
                    } else if (selected.getSelectedItems().size() == 0) {
                        distributionBox.remove(conceptSelectionBox);
                    }
                });

        distributionBox.add(datasetGrid);
        getContent().add(distributionSelectionHeader, distributionBox);
    }

    public void renderConceptSelectionGrid(List<ConceptPojo> allConcepts) {
        TreeGrid<ConceptPojo> conceptGrid = new TreeGrid<>(ConceptPojo.class);
        conceptGrid.setItems(allConcepts, ConceptPojo::getAllChildren);

        conceptGrid.removeAllColumns();
        conceptGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        conceptGrid
                .addHierarchyColumn(ConceptPojo::getName)
                .setHeader("Concept QName")
                .setResizable(true);

        MultiSelect<Grid<ConceptPojo>, ConceptPojo> selectedConcepts = conceptGrid.asMultiSelect();


        selectedConcepts.addSelectionListener(
                e -> {
                    if (selectedConcepts.getSelectedItems().size() >= 1) {
                        conceptSelectionBox.add(messageCreationBox);
                    } else if (selectedConcepts.getSelectedItems().size() == 0) {
                        conceptSelectionBox.remove(messageCreationBox);
                    }
                });

        selectedConcepts.addValueChangeListener(
                e -> {
                    List<ConceptPojo> children = selectedConcepts.getSelectedItems().stream().collect(Collectors.toList());
                    if (selectedConcepts.getSelectedItems().size() == 1) {
                        if (conceptGrid.getTreeData().getParent(children.get(0)) != null) {
                            conceptGrid.select(conceptGrid.getTreeData().getParent(children.get(0)));
                        } else if (conceptGrid.getTreeData().getChildren(children.get(0)) != null) {
                            selectedConcepts.deselectAll();
                        }
                    }
                    presenter.notifyConceptSelection(selectedConcepts.getSelectedItems());
                });

        conceptSelectionBox.add(conceptSelectionHeader, conceptGrid);
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
        Notification notification =
                new Notification("Created TOOPRequest Document", 3000, Notification.Position.TOP_START);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
        messageCreationBox.add(buttonWrapper);
    }

    public void resetConceptBean(ConceptQueryFVBean conceptQueryFVBean) {
        this.conceptQueryFVBean = conceptQueryFVBean;
        topLevelSlotComponent.resetConceptBean(conceptQueryFVBean);

    }


}
