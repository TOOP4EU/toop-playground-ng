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
package eu.toop.playground.dp.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.regrep.ERegRepResponseStatus;
import com.typesafe.config.ConfigFactory;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.error.EDMExceptionPojo;
import eu.toop.edm.error.EEDMExceptionType;
import eu.toop.edm.error.EToopDataElementResponseErrorCode;
import eu.toop.edm.error.EToopErrorCode;
import eu.toop.edm.error.EToopErrorOrigin;
import eu.toop.edm.error.EToopErrorSeverity;
import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.request.IEDMRequestPayloadConcepts;
import eu.toop.edm.response.ResponseDocumentPojo;
import eu.toop.edm.response.ResponseDocumentReferencePojo;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.playground.dp.DPException;
import eu.toop.playground.dp.DpConfig;
import eu.toop.playground.dp.datasource.GenericDocumentDataSource;
import eu.toop.playground.dp.datasource.RegisteredOrganizationDataSource;
import eu.toop.playground.dp.model.Attachment;
import eu.toop.playground.dp.model.DocumentDataset;
import eu.toop.playground.dp.model.EDMResponseWithAttachment;
import eu.toop.playground.dp.model.GBMDataset;

public class ToopDP {
    public static final DpConfig APPCONFIG = new DpConfig(ConfigFactory
            .parseFile(Paths.get("ToopDP.conf").toFile())
            .resolve());
    private static final Logger LOGGER = LoggerFactory.getLogger(ToopDP.class);
    private final RegisteredOrganizationDataSource roDs;
    private final GenericDocumentDataSource gdDs;

    public ToopDP() {
        this(APPCONFIG.dp.datasetGBM, APPCONFIG.dp.datasetDocument);
    }

    public ToopDP(String conceptDatasetsPath, String documentDatasetsPath) {
        if (Files.notExists(Paths.get(conceptDatasetsPath))) {
            LOGGER.info("GBM concept dataset directory not found, creating directories and copying examples to {}", conceptDatasetsPath);
            createExampleDataset(Paths.get(conceptDatasetsPath),
                    "datasets/gbm/example_LP.yaml");
            createExampleDataset(Paths.get(conceptDatasetsPath),
                    "datasets/gbm/example_LP_LR.yaml");
            createExampleDataset(Paths.get(conceptDatasetsPath),
                    "datasets/gbm/example_NP.yaml");
        }
        if (Files.notExists(Paths.get(documentDatasetsPath))) {
            LOGGER.info("Document dataset directory not found, creating directories and copying examples to {}", documentDatasetsPath);
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/example.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/min.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/example_LP.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/example_LP_JPEG.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/03a74657-c4c9-4a26-92ac-abf37e36c6ae.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/03e287f7-7500-446b-b360-1db78ffb2800.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/26bd0e98-108a-4d39-8553-d4f5693fcd41.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/622de68c-11a8-4006-adbf-4520f40693d0.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/516159d1-6d92-4806-b9ee-0d3b7d8996b9.yaml");
            createExampleDataset(Paths.get(documentDatasetsPath),
                    "datasets/document/a852053e-ba86-4690-86cf-7190a2f5d915.yaml");
        }

        final String attachmentsPath = documentDatasetsPath+"/attachments";
        if (Files.notExists(Paths.get(attachmentsPath))) {
            LOGGER.info("Attachment directory not found, creating directories and copying examples to {}", attachmentsPath);
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/dummy.jpg");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/dummy.pdf");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/03a74657-c4c9-4a26-92ac-abf37e36c6ae.pdf");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/03e287f7-7500-446b-b360-1db78ffb2800.pdf");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/26bd0e98-108a-4d39-8553-d4f5693fcd41.pdf");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/622de68c-11a8-4006-adbf-4520f40693d0.pdf");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/516159d1-6d92-4806-b9ee-0d3b7d8996b9.pdf");
            createExampleDataset(Paths.get(attachmentsPath),
                    "datasets/document/attachments/a852053e-ba86-4690-86cf-7190a2f5d915.pdf");
        }

        roDs = new RegisteredOrganizationDataSource(conceptDatasetsPath);
        gdDs = new GenericDocumentDataSource(documentDatasetsPath);
    }

    public RegisteredOrganizationDataSource getRegisteredOrganizationDatasource() {
        return roDs;
    }

    public GenericDocumentDataSource getDocumentDatasource() {
        return gdDs;
    }

    public EDMResponse createEDMResponseFromRequest(EDMRequest edmRequest) throws DPException, IOException {
        if (edmRequest != null)
            return createEDMResponseFromRequest(edmRequest.getWriter().getAsBytes());
        return createEDMResponseFromRequest((byte[]) null);
    }

    public byte[] createXMLResponseFromRequest(byte[] xmlRequest) throws DPException, IOException {
        return createEDMResponseFromRequest(xmlRequest).getWriter().getAsBytes();
    }

    public EDMResponse createEDMResponseFromRequest(byte[] xmlRequest) throws DPException, IOException {
        return createEDMResponseWithAttachmentsFromRequest(xmlRequest).getEdmResponse();
    }

    public EDMResponseWithAttachment createEDMResponseWithAttachmentsFromRequest(EDMRequest edmRequest) throws DPException, IOException {
        if (edmRequest != null)
            return createEDMResponseWithAttachmentsFromRequest(edmRequest.getWriter().getAsBytes());
        return createEDMResponseWithAttachmentsFromRequest((byte[]) null);
    }

    public EDMResponseWithAttachment createEDMResponseWithAttachmentsFromRequest(byte[] xmlRequest) throws DPException, IOException {

        EDMRequest edmRequest = null;
        if (xmlRequest != null)
            edmRequest = EDMRequest.reader().read(xmlRequest);

        if (edmRequest == null) {
            LOGGER.warn("Got an EDMRequest that was not readable.");
            String errorMessage = "Unable to read the supplied request.";
            throw new DPException(EDMErrorResponse.builder()
                    .requestID("NONE")
                    .responseStatus(ERegRepResponseStatus.FAILURE)
                    .errorProvider(getMinimumDPInfo())
                    .addException(EDMExceptionPojo.builder()
                            .timestampNow()
                            .errorCode(EToopErrorCode.GEN)
                            .errorMessage(errorMessage)
                            .errorOrigin(EToopErrorOrigin.REQUEST_RECEPTION)
                            .exceptionType(EEDMExceptionType.QUERY)
                            .severity(EToopErrorSeverity.FAILURE)
                            .build())
                    .build(), errorMessage);
        }

        switch (edmRequest.getQueryDefinition()) {
            case CONCEPT:
                LOGGER.info("Got a concept EDMRequest. ID: " + edmRequest.getRequestID());

                List<GBMDataset> x = roDs.findResponse(edmRequest);
                if (x.size()>0) {
                    if(x.size()>1){
                        LOGGER.warn("Found more than one suitable concept responses, creating a response for the first one.");
                    }
                    GBMDataset dataset = x.get(0);
                    LOGGER.info("Found a dataset for the EDMRequest. ID: " + edmRequest.getRequestID());
                    if (edmRequest.getPayloadProvider() instanceof IEDMRequestPayloadConcepts) {
                        IEDMRequestPayloadConcepts concepts = (IEDMRequestPayloadConcepts) edmRequest.getPayloadProvider();
                        return new EDMResponseWithAttachment(EDMResponse.builderConcept()
                                .responseStatus(ERegRepResponseStatus.SUCCESS)
                                .requestID(edmRequest.getRequestID())
                                .concept(ConceptPojo.builder(mergeConceptValues(dataset.getConceptPojoList(), concepts.getAllConcepts()).get(0)))
                                .issueDateTimeNow()
                                .dataProvider(getDPInfo())
                                .build());
                    }
                }
                LOGGER.warn("Found no dataset for the EDMRequest. ID: " + edmRequest.getRequestID());
                String errorMessage = "Found no dataset for the supplied identifiers.";
                throw new DPException(EDMErrorResponse.builder()
                        .requestID(edmRequest.getRequestID())
                        .responseStatus(ERegRepResponseStatus.FAILURE)
                        .errorProvider(getMinimumDPInfo())
                        .addException(EDMExceptionPojo.builder()
                                .timestampNow()
                                .errorCode(EToopErrorCode.GEN)
                                .errorMessage(errorMessage)
                                .errorOrigin(EToopErrorOrigin.RESPONSE_CREATION)
                                .exceptionType(EEDMExceptionType.OBJECT_NOT_FOUND)
                                .severity(EToopErrorSeverity.FAILURE)
                                .build())
                        .build(), errorMessage);
            case DOCUMENT_BY_DISTRIBUTION:
            case DOCUMENT_BY_ID:
                LOGGER.info("Got a Document EDMRequest. ID: " + edmRequest.getRequestID());
                List<DocumentDataset> foundDatasets = gdDs.findResponse(edmRequest);
                if (foundDatasets.size()>0) {
                    LOGGER.info("Found a dataset for the EDMRequest. ID: " + edmRequest.getRequestID());
                    switch (edmRequest.getResponseOption()) {
                        case INLINE:
                            LOGGER.info("Creating an Inline response.");
                            if(foundDatasets.size()>1){
                                LOGGER.info("Attaching {} document(s) on response.", foundDatasets.size());
                            }
                            List<ResponseDocumentPojo> responseDocumentPojos = foundDatasets.stream()
                            .map(dataset -> ResponseDocumentPojo.builder()
                                    .registryObjectID(dataset.getIDs().get(0))
                                    .dataset(dataset.getAsDatasetPojo())
                                    .repositoryItemRef(dataset.getRepositoryItemRef().getAsRepositoryItemRefPojo()).build())
                                    .collect(Collectors.toList());

                            List<Attachment> attachmentList = new ArrayList<>();
                            for (DocumentDataset foundDataset : foundDatasets) {
                                Attachment attachment = new Attachment(foundDataset);
                                attachmentList.add(attachment);
                            }

//                            DocumentDataset dataset = foundDatasets.get(0);
                            return new EDMResponseWithAttachment(EDMResponse.builderDocument()
                                    .responseStatus(ERegRepResponseStatus.SUCCESS)
                                    .requestID(edmRequest.getRequestID())
                                    .responseObjects(responseDocumentPojos)
                                    .issueDateTimeNow()
                                    .dataProvider(getDPInfo())
                                    .build(),attachmentList);
                        case REFERENCE:
                            LOGGER.info("Creating an ObjectReference response.");
                            List<ResponseDocumentReferencePojo> responseDocumentReferencePojos = foundDatasets.stream()
                                    .map(documentDataset -> ResponseDocumentReferencePojo.builder()
                                            .registryObjectID(documentDataset.getIDs().get(0))
                                            .dataset(documentDataset.getAsDatasetPojo()).build())
                                    .collect(Collectors.toList());

                            return new EDMResponseWithAttachment(EDMResponse.builderDocumentReference()
                                    .responseStatus(ERegRepResponseStatus.SUCCESS)
                                    .requestID(edmRequest.getRequestID())
                                    .responseObjects(responseDocumentReferencePojos)
                                    .issueDateTimeNow()
                                    .dataProvider(getDPInfo())
                                    .build());
                    }

                }
                LOGGER.warn("Found no dataset for the EDMRequest. ID: " + edmRequest.getRequestID());
                errorMessage = "Found no dataset for the supplied identifiers.";
                throw new DPException(EDMErrorResponse.builder()
                        .requestID(edmRequest.getRequestID())
                        .responseStatus(ERegRepResponseStatus.FAILURE)
                        .errorProvider(getMinimumDPInfo())
                        .addException(EDMExceptionPojo.builder()
                                .timestampNow()
                                .errorCode(EToopErrorCode.GEN)
                                .errorMessage(errorMessage)
                                .errorOrigin(EToopErrorOrigin.RESPONSE_CREATION)
                                .exceptionType(EEDMExceptionType.OBJECT_NOT_FOUND)
                                .severity(EToopErrorSeverity.FAILURE)
                                .build())
                        .build(), errorMessage);
        }
        return null;
    }

    private AgentPojo getDPInfo() {
        return AgentPojo.builder()
                .name(APPCONFIG.getDp().getName())
                .id(APPCONFIG.getDp().getId())
                .idSchemeID(APPCONFIG.getDp().getIdSchemeID())
                .address(AddressPojo.builder()
                        .town(APPCONFIG.getDp().getAddress().getCity())
                        .streetName(APPCONFIG.getDp().getAddress().getStreetName())
                        .buildingNumber(APPCONFIG.getDp().getAddress().getBuildingNumber())
                        .countryCode(APPCONFIG.getDp().getAddress().getCountryCode())
                        .fullAddress(APPCONFIG.getDp().getAddress().getFullAddress())
                        .postalCode(APPCONFIG.getDp().getAddress().getPostalCode()).build())
                .build();
    }

    private AgentPojo getMinimumDPInfo() {
        return AgentPojo.builder()
                .name(APPCONFIG.getDp().getName())
                .id(APPCONFIG.getDp().getId())
                .idSchemeID(APPCONFIG.getDp().getIdSchemeID())
                .build();
    }

    // cp1List contains all of the response values, cp2List contains the request concepts
    public List<ConceptPojo> mergeConceptValues(List<ConceptPojo> cp1List, List<ConceptPojo> cp2List) {
        List<ConceptPojo> mergedCPs = new ArrayList<>();
        for (ConceptPojo cp2 : cp2List) {
            for (ConceptPojo cp1 : cp1List) {
                if (cp1.getName() != null && cp1.getName().equals(cp2.getName())) {
                    ConceptPojo.Builder conceptPojoBuilder = ConceptPojo.builder();
                    conceptPojoBuilder.children(mergeConceptValues(cp1.children(), cp2.children()));
//                    LOGGER.info("Found and applied concepts for " + cp2.getName());
                    ToopKafkaClient.send(EErrorLevel.INFO, "Found and applied concepts for " + cp2.getName());

                    mergedCPs.add(conceptPojoBuilder.id(cp2.getID()).name(cp2.getName()).value(cp1.getValue()).build());
                    break;
                }
            }
        }

        return cp2List.stream()
                .map(conceptPojo -> mergedCPs.stream().filter(c -> Objects.equals(c.getName(), conceptPojo.getName()))
                        .findFirst()
                        .orElseGet(() -> {
                            ToopKafkaClient.send(EErrorLevel.WARN, "Found unknown concept for " + conceptPojo.getName());
                            return ConceptPojo.builder()
                                    .id(conceptPojo.getID())
                                    .name(conceptPojo.getName())
                                    .valueErrorCode(EToopDataElementResponseErrorCode.DP_ELE_001)
                                    .children(applyUnknownConceptStatus(conceptPojo.children()))
                                    .build();
                        }))
                .collect(Collectors.toList());
    }

    private List<ConceptPojo> applyUnknownConceptStatus(List<ConceptPojo> conceptPojoList) {
        return conceptPojoList.stream()
                .map(conceptPojo -> conceptPojo
                        .cloneAndModify(x -> {
                            ToopKafkaClient.send(EErrorLevel.WARN, "Found unknown concept for " + conceptPojo.getName());
                            x.valueErrorCode(EToopDataElementResponseErrorCode.DP_ELE_001);
                            x.children(applyUnknownConceptStatus(x.children()));
                        }))
                .collect(Collectors.toList());
    }

    private void createExampleDataset(Path path, String jarPath) {
        try (InputStream jarExample = this.getClass().getClassLoader().getResourceAsStream(jarPath)) {
            Files.createDirectories(path);
            Files.copy(jarExample,Paths.get(path.toString(), Paths.get(jarPath).getFileName().toString()));
//            byte[] buffer = new byte[jarExample.available()];
//            if (jarExample.read(buffer) > 0) {
//                File exampleFile = new File(path.toString(), Paths.get(jarPath).getFileName().toString());
//                try (FileOutputStream fos = new FileOutputStream(exampleFile)) {
//                    fos.write(buffer);
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
