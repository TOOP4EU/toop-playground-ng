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
package eu.toop.playground.dp.datasource;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.request.IEDMRequestPayloadDocumentID;
import eu.toop.playground.dp.model.Dataset;
import eu.toop.playground.dp.model.DocumentDataset;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Datasource<T extends Dataset> {

    default List<T> findResponse(EDMRequest edmRequest) {
        return getDatasets().values()
                .stream()
                .filter(dataset ->
                        (((edmRequest.getDataSubjectLegalPerson() != null) &&
                                (dataset.getLegalPersonID() != null) &&
                                dataset.getLegalPersonID().equals(toopIdentifierExtractor(edmRequest.getDataSubjectLegalPerson().getID())) ||
                                ((edmRequest.getDataSubjectLegalPerson() != null) &&
                                        (dataset.getLegalPersonID() != null) &&
                                        dataset.getLegalPersonID().equals(toopIdentifierExtractor(edmRequest.getDataSubjectLegalPerson().getLegalID())) ||
                                        ((edmRequest.getDataSubjectNaturalPerson() != null) &&
                                                (dataset.getNaturalPersonID() != null) &&
                                                dataset.getNaturalPersonID().equals(toopIdentifierExtractor(edmRequest.getDataSubjectNaturalPerson().getID())))))) || (edmRequest.getPayloadProvider() instanceof IEDMRequestPayloadDocumentID))
                .filter(dataset -> {
                    if (edmRequest.getAuthorizedRepresentative() != null && dataset.getLegalRepresentativeID() != null) {
                        return dataset.getLegalRepresentativeID().equals(toopIdentifierExtractor(edmRequest.getAuthorizedRepresentative().getID()));
                    }
                        return ((edmRequest.getAuthorizedRepresentative() == null && dataset.getLegalRepresentativeID() == null) || (edmRequest.getPayloadProvider() instanceof IEDMRequestPayloadDocumentID));
                })
                .filter(dataset -> {
                    if ((edmRequest.getPayloadProvider() instanceof IEDMRequestPayloadDocumentID) &&
                    dataset instanceof DocumentDataset) {
                        String documentID = ((IEDMRequestPayloadDocumentID) edmRequest.getPayloadProvider()).getDocumentID();
                        return documentID.equals(((DocumentDataset) dataset).getIDs().get(0));
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }


    default String toopIdentifierExtractor(String identifier) {
        if (identifier == null)
            return null;
        String[] identifierParts = identifier.split("/");
        if (identifierParts.length != 3)
            return identifier;
        return identifierParts[2];

    }

    Map<Path, T> getDatasets();
}
