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
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import eu.toop.playground.dc.ui.util.MessageCreation;
/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DataConsumerDetailsComponent extends Composite<VerticalLayout> {
    // Data Consumer Details
    H2 dcHeader = new H2("Data Consumer Details: ");
    Div dataConsumerBox = new Div();
    Paragraph dcName;
    Paragraph dcID;
    Paragraph dcFullAddress;

    public DataConsumerDetailsComponent() {
        dataConsumerBox.setClassName("dataConsumerBox");
        dcName =  new Paragraph("Data Consumer Name: " + MessageCreation.createDataConsumer().getName());
        dcID =  new Paragraph("Data Consumer ID: " + MessageCreation.createDataConsumer().getID());
        dcFullAddress = new Paragraph(
                "Data Consumer FullAddress: "
                        + MessageCreation.createDataConsumer().getAddress().getFullAddress());

        dataConsumerBox.add(dcName, dcID, dcFullAddress);

        getContent().setPadding(false);
        getContent().add(dcHeader, dataConsumerBox);
    }
}
