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
import java.util.Objects;

import org.vaadin.olli.FileDownloadWrapper;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMResponse;
import eu.toop.playground.dc.ui.model.EDMResponseWithAttachment;
import eu.toop.playground.dc.ui.presenter.MainPresenter;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class ActionButtonComponent extends Composite<HorizontalLayout> {

  Button showResponseBtn;
  Button showErrorResponseBtn;
  Button downloadResponse;
  Button downloadResponseWithAttachment;
  Button downloadErrorResponse;
  Button showResponseAttachBtn;
  MainPresenter presenter;
  EDMResponse response;
  EDMResponseWithAttachment edmResponseWithAttachment;
  EDMErrorResponse errorResponse;

  public ActionButtonComponent(MainPresenter presenter) {
    this.presenter = presenter;
    showResponseBtn = new Button("show response");
    showResponseAttachBtn = new Button("show response");
    showErrorResponseBtn = new Button("show error");
    downloadResponse = new Button();
    downloadErrorResponse = new Button();
    downloadResponseWithAttachment = new Button();

    showResponseBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    showResponseAttachBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    showErrorResponseBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
    downloadResponse.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    downloadResponseWithAttachment.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    downloadErrorResponse.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
    downloadResponse.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));
    downloadResponseWithAttachment.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));
    downloadErrorResponse.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));

    enableBtns(false);

    showResponseBtn.addClickListener(
        click -> {
          presenter.showResponse(response);
        });

    showResponseAttachBtn.addClickListener(click -> {
      presenter.showResponse(edmResponseWithAttachment);
    });

    showErrorResponseBtn.addClickListener(
        click -> {
          presenter.showErrorResponse(errorResponse);
        });

    FileDownloadWrapper buttonWrapper =
        new FileDownloadWrapper(
            new StreamResource(
                "TOOPResponse.xml",
                () ->
                    new ByteArrayInputStream(
                        Objects.requireNonNull(response.getWriter().getAsBytes()))));

    FileDownloadWrapper buttonAttachWrapper =
            new FileDownloadWrapper(
                    new StreamResource(
                            "TOOPResponseWithAttachment.xml",
                            () ->
                                    new ByteArrayInputStream(
                                            Objects.requireNonNull(edmResponseWithAttachment.getEdmResponse().getWriter().getAsBytes()))));


    FileDownloadWrapper buttonErrorWrapper =
        new FileDownloadWrapper(
            new StreamResource(
                "TOOPErrorResponse.xml",
                () ->
                    new ByteArrayInputStream(
                        Objects.requireNonNull(errorResponse.getWriter().getAsBytes()))));

    buttonWrapper.wrapComponent(downloadResponse);
    buttonErrorWrapper.wrapComponent(downloadErrorResponse);
    buttonAttachWrapper.wrapComponent(downloadResponseWithAttachment);

    getContent().add(showResponseBtn, buttonWrapper);
    getContent().add(showResponseAttachBtn, buttonAttachWrapper);
    getContent().add(showErrorResponseBtn, buttonErrorWrapper);
  }

  public void enableBtns(Boolean bool) {
    showResponseBtn.setEnabled(bool);
    downloadResponse.setEnabled(bool);
  }

  public void enableAttachBtns(Boolean bool) {
    showResponseAttachBtn.setEnabled(bool);
    downloadResponseWithAttachment.setEnabled(bool);
  }

  public void enableErrorBtns(Boolean bool) {
    showErrorResponseBtn.setEnabled(bool);
    downloadErrorResponse.setEnabled(bool);
  }

  public void showBtns(Boolean bool) {
    showResponseBtn.setVisible(bool);
    downloadResponse.setVisible(bool);
  }

  public void showAttachBtns(Boolean bool) {
    showResponseAttachBtn.setVisible(bool);
    downloadResponseWithAttachment.setVisible(bool);
  }


  public void showErrorBtns(Boolean bool) {
    showErrorResponseBtn.setVisible(bool);
    downloadErrorResponse.setVisible(bool);
  }

  public void setResponse(EDMResponse response) {
    this.response = response;
  }

  public void setResponseWithAttachment(EDMResponseWithAttachment response) {
    this.edmResponseWithAttachment = response;
  }

  public void setErrorResponse(EDMErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
  }

}
