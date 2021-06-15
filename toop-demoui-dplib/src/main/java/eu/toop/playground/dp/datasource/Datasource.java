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
