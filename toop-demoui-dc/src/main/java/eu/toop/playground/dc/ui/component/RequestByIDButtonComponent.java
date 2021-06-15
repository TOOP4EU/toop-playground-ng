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

import java.io.ByteArrayInputStream;
import java.util.Objects;

import org.vaadin.olli.FileDownloadWrapper;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;

import eu.toop.edm.EDMRequest;
import eu.toop.playground.dc.ui.presenter.MainPresenter;

/** @author Maria Siapera [mariaspr at unipi.gr] on 10/6/2020. */
public class RequestByIDButtonComponent extends Composite<HorizontalLayout> {

  MainPresenter presenter;
  Button downloadRequestByIdBtn;
  Button createDocIDRequestBtn;
  EDMRequest request;

  public RequestByIDButtonComponent(MainPresenter presenter, String docID, DocumentResponseComponent documentResponseComponent) {
    this.presenter = presenter;
    createDocIDRequestBtn = new Button("create request");
    downloadRequestByIdBtn = new Button();
    downloadRequestByIdBtn.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));
    downloadRequestByIdBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    createDocIDRequestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    createDocIDRequestBtn.addClickListener(
        click -> {
          presenter.createDocumentRefIDRequestMessage(documentResponseComponent, docID, this);
        });


    getContent().add(createDocIDRequestBtn);
  }

  public void setRequest(EDMRequest request) {
    this.request = request;
    FileDownloadWrapper buttonWrapper =
            new FileDownloadWrapper(
                    new StreamResource(
                            "TOOPRequest.xml",
                            () ->
                                    new ByteArrayInputStream(
                                            Objects.requireNonNull(request.getWriter().getAsBytes()))));

    buttonWrapper.wrapComponent(downloadRequestByIdBtn);
    Notification notification =
            new Notification("Created TOOPRequest Document", 3000, Notification.Position.TOP_START);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    notification.open();
    getContent().add(buttonWrapper);
  }
}
