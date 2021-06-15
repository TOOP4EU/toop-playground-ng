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

import java.util.List;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.model.EToopQueryDefinitionType;
import eu.toop.playground.dc.ui.model.EDMResponseWithAttachment;
import eu.toop.playground.dc.ui.model.ResultBean;
import eu.toop.playground.dc.ui.presenter.MainPresenter;

public class RequestStatusGridComponent extends Composite<VerticalLayout> {

    H2 requestStatusHeader = new H2("Request Status: ");
    Div requestStatusBox;
    Grid<ResultBean> resultGrid = new Grid<>(ResultBean.class, true);
    ;

    public RequestStatusGridComponent() {
        requestStatusBox = new Div();
        requestStatusBox.setClassName("requestStatusBox");
        getContent().setPadding(false);
    }

    public void renderStatusGrid(List<ResultBean> results, MainPresenter presenter) {
        resultGrid.setItems(results);
        resultGrid.removeAllColumns();
        resultGrid
                .addColumn(ResultBean -> ResultBean.getRequest().getRequestID())
                .setHeader("Request ID")
                .setResizable(true);
        resultGrid.addColumn(ResultBean -> ResultBean.getRequest().getQueryDefinition()).setHeader("Request Type").setResizable(true);
        resultGrid
                .addColumn(ResultBean -> ResultBean.getDpIdentifier())
                .setHeader("Data Provider Identifier")
                .setResizable(true);
        resultGrid
                .addColumn(ResultBean -> ResultBean.getRequest().getIssueDateTime())
                .setHeader("Request Date/Time")
                .setResizable(true);
        resultGrid.addColumn(ResultBean::getStatus).setHeader("Status").setResizable(true);
        resultGrid
                .addComponentColumn(
                        ResultBean -> {
                            ActionButtonComponent buttonComponent = new ActionButtonComponent(presenter);
                            if (responseExists(ResultBean.getResponse())) {
                                buttonComponent.enableBtns(true);
                                buttonComponent.enableAttachBtns(false);
                                buttonComponent.enableErrorBtns(false);
                                buttonComponent.showErrorBtns(false);
                                buttonComponent.showBtns(true);
                                buttonComponent.showAttachBtns(false);
                                buttonComponent.setResponse(ResultBean.getResponse());
                            } else if (responseWithAttachmentsExists(ResultBean.getResponseWithAttachment())) {
                                buttonComponent.enableAttachBtns(true);
                                buttonComponent.enableErrorBtns(false);
                                buttonComponent.enableBtns(false);
                                buttonComponent.showErrorBtns(false);
                                buttonComponent.showAttachBtns(true);
                                buttonComponent.showBtns(false);
                                buttonComponent.setResponseWithAttachment(ResultBean.getResponseWithAttachment());
                            } else if (responseErrorExists(ResultBean.getErrorResponse())) {
                                buttonComponent.enableErrorBtns(true);
                                buttonComponent.enableBtns(false);
                                buttonComponent.enableAttachBtns(false);
                                buttonComponent.showErrorBtns(true);
                                buttonComponent.showBtns(false);
                                buttonComponent.showAttachBtns(false);
                                buttonComponent.setErrorResponse(ResultBean.getErrorResponse());
                            } else {
                                buttonComponent.enableBtns(false);
                                buttonComponent.enableAttachBtns(false);
                                buttonComponent.enableErrorBtns(false);
                                buttonComponent.showErrorBtns(false);
                                buttonComponent.showAttachBtns(false);
                            }
                            return buttonComponent;
                        })
                .setHeader("Action")
                .setResizable(true);

        // TODO: refactor too many if...else
        resultGrid
                .addComponentColumn(
                        ResultBean -> {
                            Button resendRequestBtn = new Button("resend request");
                            resendRequestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                            if (ResultBean.getRequest().getQueryDefinition().getID().equals(EToopQueryDefinitionType.CONCEPT.getID())) {
                                resendRequestBtn.addClickListener(
                                        click -> {
                                            presenter.resendConceptQuery(ResultBean.getRequest(), ResultBean.getDsdDTO());
                                        });
                            } else if (ResultBean.getRequest()
                                    .getQueryDefinition()
                                    .getID()
                                    .equals(EToopQueryDefinitionType.DOCUMENT_BY_DISTRIBUTION.getID())) {
                                resendRequestBtn.addClickListener(
                                        click -> {
                                            presenter.resendDocumentQuery(ResultBean.getRequest(), ResultBean.getDsdDTO());
                                        });
                            } else if (ResultBean.getRequest().getQueryDefinition().getID().equals(EToopQueryDefinitionType.DOCUMENT_BY_ID.getID())) {
                                resendRequestBtn.addClickListener(
                                        click -> {
                                            presenter.resendDocumentQueryByID(ResultBean.getRequest(), ResultBean.getDsdDTO());
                                        });
                            }
                            return resendRequestBtn;
                        })
                .setHeader("Action 2")
                .setResizable(true);

        requestStatusBox.add(resultGrid);
        getContent().add(requestStatusHeader, requestStatusBox);
    }

    public void refreshGrid() {
        resultGrid.getDataProvider().refreshAll();
        Notification notification = new Notification("Response Created...", 3000, Notification.Position.TOP_START);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    public boolean gridExists(Grid<ResultBean> grid) {
        return grid != null;
    }

    public boolean responseExists(EDMResponse response) {
        return response != null;
    }

    public boolean responseWithAttachmentsExists(EDMResponseWithAttachment response) {
        return response != null;
    }

    public boolean responseErrorExists(EDMErrorResponse errorResponse) {
        return errorResponse != null;
    }
}
