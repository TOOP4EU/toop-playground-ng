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
import java.util.Optional;

import com.helger.commons.mime.MimeTypeParser;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.server.StreamResource;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.response.IEDMResponsePayloadDocument;
import eu.toop.edm.response.IEDMResponsePayloadDocumentReference;
import eu.toop.playground.dc.ui.model.Attachment;
import eu.toop.playground.dc.ui.model.DocumentResponseFVBean;
import eu.toop.playground.dc.ui.presenter.MainPresenter;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DocumentResponseComponent extends Composite<VerticalLayout> {

    H2 documentHead = new H2("Document Metadata: ");
    Div documentBox;
    private final DocumentResponseFVBean documentResponseFVBean;

    public DocumentResponseComponent(
            DocumentResponseFVBean documentResponseFVBean, MainPresenter presenter) {
        this.documentResponseFVBean = documentResponseFVBean;
        documentBox = new Div();
        documentBox.setClassName("documentBox");

        TreeGrid<DatasetPojo> documentGrid = new TreeGrid<>(DatasetPojo.class);
        documentGrid.setItems(documentResponseFVBean.getDatasetList());
        documentGrid.removeAllColumns();

        if (documentResponseFVBean.getResponse().getAllPayloadProviders().get(0)
                instanceof IEDMResponsePayloadDocumentReference) {
            documentGrid
                    .addColumn(DatasetPojo -> DatasetPojo.getAllIDs().get(0))
                    .setResizable(true)
                    .setHeader("Document Number");

        } else if (documentResponseFVBean.getResponse().getAllPayloadProviders().get(0)
                instanceof IEDMResponsePayloadDocument) {
            documentGrid
                    .addColumn(DatasetPojo -> DatasetPojo.getAllIDs().get(0))
                    .setHeader("Document Number")
                    .setResizable(true);
        }

        documentGrid
                .addColumn(DatasetPojo::getAllDescriptions)
                .setHeader("Description")
                .setResizable(true);
        documentGrid.addColumn(DatasetPojo::getAllTitles).setHeader("Title").setResizable(true);
        documentGrid
                .addColumn(DatasetPojo -> DatasetPojo.getDistribution() != null ? DatasetPojo.getDistribution().getDocumentType() : null)
                .setHeader("Document Type")
                .setResizable(true);
        documentGrid.addColumn(DatasetPojo::getLanguage).setHeader("Language").setResizable(true);
        documentGrid.addColumn(DatasetPojo::getIssuedDT).setHeader("Release Date").setResizable(true);
        documentGrid
                .addColumn(DatasetPojo -> DatasetPojo.getCreator() != null ? DatasetPojo.getCreator().getName() : null)
                .setHeader("Creator Name")
                .setResizable(true);
        documentGrid
                .addColumn(DatasetPojo -> DatasetPojo.getCreator() != null ? DatasetPojo.getCreator().getID() : null)
                .setHeader("Creator ID")
                .setResizable(true);
        /* 2nd step request - DOCUMENT REF Query  */
        if (documentResponseFVBean.getResponse().getAllPayloadProviders().get(0)
                instanceof IEDMResponsePayloadDocumentReference && documentResponseFVBean.getResponseWithAttachment() == null) {
            documentGrid
                    .addComponentColumn(
                            DatasetPojo -> new RequestByIDButtonComponent(presenter, DatasetPojo.getAllIDs().get(0), this))
                    .setHeader("Fetch Document")
                    .setResizable(true);
        }
        /* DOCUMENT Query */
        if (documentResponseFVBean.getResponseWithAttachment() != null) {
            if (documentResponseFVBean.getResponseWithAttachment().getEdmResponse().getAllPayloadProviders().get(0)
                    instanceof IEDMResponsePayloadDocument) {
                documentGrid
                        .addComponentColumn(
                                DatasetPojo -> {

                                    Optional<Attachment> theAttachment = documentResponseFVBean.getResponseWithAttachment()
                                            .getAttachmentList()
                                            .stream()
                                            .filter( attachment -> DatasetPojo.getAllIDs().get(0).equals(attachment.getContentID()))
                                            .findFirst();

                                    if(theAttachment.isPresent()){
                                        Attachment a = theAttachment.get();

                                        Icon download = new Icon(VaadinIcon.DOWNLOAD);
                                        download.setColor("097bd3");
                                        download.setSize("30");
                                        String fileExtension = MimeTypeParser.safeParseMimeType(a.getMimeType()).getContentSubType();
                                        Anchor anchor =
                                                new Anchor(
                                                        new StreamResource(
                                                                "attachment."+fileExtension, () -> new ByteArrayInputStream(a.getAsByteArray())),
                                                        "");
                                        anchor.getElement().setAttribute("download", true);
                                        anchor.setHref(
                                                new StreamResource(
                                                        "attachment." +fileExtension, () -> new ByteArrayInputStream(a.getAsByteArray())));
                                        anchor.add(download);
                                        return anchor;
                                    }
                                    Anchor a = new Anchor();
                                    Icon x = new Icon(VaadinIcon.CLOSE);
                                    x.setSize("30");
                                    a.add(x);
                                    a.setEnabled(false);
                                    return a;
                                })
                        .setHeader("Download")
                        .setResizable(true);
            }
        }


        documentBox.add(documentGrid);
        getContent().setPadding(false);
        getContent().add(documentHead, documentBox);
    }

    public DocumentResponseFVBean getDocumentResponseFVBean() {
        return documentResponseFVBean;
    }

    public void downloadXML(EDMRequest request) {

        //        messageCreationBox.add(buttonWrapper);
    }
}
