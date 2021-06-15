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
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import eu.toop.playground.dc.ui.presenter.MainPresenter;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class UploadComponent extends Composite<VerticalLayout> {

  MemoryBuffer buffer;
  H2 uploadHeader = new H2("Upload a response: ");
  Upload upload;
  Div uploadBox;

  public UploadComponent(MainPresenter presenter) {
    uploadBox = new Div();
    uploadBox.setClassName("uploadBox");
    buffer = new MemoryBuffer();
    upload = new Upload(buffer);
    upload.addFinishedListener(
        event -> {
          System.out.println("Uploaded file: " + event.getFileName());
          presenter.importResponseFromUpload(buffer);
        });
    uploadBox.add(upload);

    getContent().setPadding(false);
    getContent().add(uploadHeader, uploadBox);
  }
}
