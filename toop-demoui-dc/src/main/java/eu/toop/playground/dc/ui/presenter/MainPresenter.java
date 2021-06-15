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
package eu.toop.playground.dc.ui.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import eu.toop.edm.model.*;
import eu.toop.edm.request.EDMRequestPayloadDocumentID;
import eu.toop.edm.request.IEDMRequestPayloadDocumentID;
import eu.toop.playground.dc.ui.model.exceptions.EvaluationException;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.request.EDMRequestPayloadConcepts;
import eu.toop.edm.request.EDMRequestPayloadDistribution;
import eu.toop.edm.response.EDMResponsePayloadConcepts;
import eu.toop.edm.response.IEDMResponsePayloadConcepts;
import eu.toop.edm.response.IEDMResponsePayloadDocument;
import eu.toop.edm.response.IEDMResponsePayloadDocumentReference;
import eu.toop.edm.response.IEDMResponsePayloadProvider;
import eu.toop.edm.response.ResponseDocumentPojo;
import eu.toop.edm.response.ResponseDocumentReferencePojo;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.playground.dc.config.enums.DCConfig;
import eu.toop.playground.dc.ui.component.ConceptQueryComponent;
import eu.toop.playground.dc.ui.component.DocumentQueryComponent;
import eu.toop.playground.dc.ui.component.DocumentResponseComponent;
import eu.toop.playground.dc.ui.component.RequestByIDButtonComponent;
import eu.toop.playground.dc.ui.model.AddressBean;
import eu.toop.playground.dc.ui.model.ConceptQueryFVBean;
import eu.toop.playground.dc.ui.model.ConceptResponseFVBean;
import eu.toop.playground.dc.ui.model.DocumentQueryFVBean;
import eu.toop.playground.dc.ui.model.DocumentResponseFVBean;
import eu.toop.playground.dc.ui.model.EDMResponseWithAttachment;
import eu.toop.playground.dc.ui.model.ErrorResponseFVBean;
import eu.toop.playground.dc.ui.model.LegalPersonFVBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;
import eu.toop.playground.dc.ui.model.ResultBean;
import eu.toop.playground.dc.ui.model.dto.DSDDatasetResponseDto;
import eu.toop.playground.dc.ui.model.enums.EResponseStatus;
import eu.toop.playground.dc.ui.service.ConceptService;
import eu.toop.playground.dc.ui.service.ConnectionService;
import eu.toop.playground.dc.ui.service.DSDService;
import eu.toop.playground.dc.ui.util.MessageCreation;
import eu.toop.playground.dc.ui.util.Utilities;
import eu.toop.playground.dc.ui.view.DynamicRequestView;

/** @author Maria Siapera [mariaspr at unipi.gr] */
public class MainPresenter {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainPresenter.class.getName());

  private DynamicRequestView view;
  private NaturalPersonFVBean naturalPersonFVBean;
  private LegalPersonFVBean legalPersonFVBean;
  private NaturalPersonFVBean naturalPersonARFVBean;
  private ConceptQueryFVBean conceptQueryFVBean;
  private ConceptResponseFVBean conceptResponseFVBean;
  private DocumentResponseFVBean documentResponseFVBean;
  private DocumentQueryFVBean documentQueryFVBean;
  private ErrorResponseFVBean errorResponseFVBean;
  private List<DSDDatasetResponseDto> DSDDatasetResponseList;
  private List<ConceptPojo> allConcepts;
  private List<ResultBean> resultBeanList;
  private ConnectionService connectionService;
  private Map<String, ResultBean> resultMap;
  private final DSDService dsdService = new DSDService();
  private final ConceptService conceptService = new ConceptService();
  private boolean hasErrors = true;

  public void init(final DynamicRequestView view) {

    this.view = view;
    LOGGER.debug("Got concept dataset from mock DSD: ");
    allConcepts = conceptService.getConceptPojoParent();
    // init models
    conceptQueryFVBean = new ConceptQueryFVBean();
    documentQueryFVBean = new DocumentQueryFVBean();
    naturalPersonFVBean = new NaturalPersonFVBean();
    legalPersonFVBean = new LegalPersonFVBean();
    naturalPersonARFVBean = new NaturalPersonFVBean();
    conceptResponseFVBean = new ConceptResponseFVBean();
    documentResponseFVBean = new DocumentResponseFVBean();
    errorResponseFVBean = new ErrorResponseFVBean();
    resultBeanList = new ArrayList<>();

    connectionService = new ConnectionService();
    resultMap = new LinkedHashMap<>();
  }

  public void initDataSubject(final HasValue.ValueChangeEvent event) {
    LOGGER.debug("Radio button value: " + event.getValue().toString());
    if (event.getValue().toString().equals("Natural Person")) {
      naturalPersonFVBean = new NaturalPersonFVBean();
      view.initNP(naturalPersonFVBean);
    } else {
      legalPersonFVBean = new LegalPersonFVBean();
      view.initLP(legalPersonFVBean);
    }
  }

  public void initQueryType(final HasValue.ValueChangeEvent event) {
    LOGGER.debug("Radio button value: " + event.getValue().toString());
    if (event.getValue().toString().equals("Concept Query")) {
      view.initConceptQuery(conceptQueryFVBean);
    } else {
      view.initDocumentQuery(documentQueryFVBean, event.getValue().toString());
    }
  }

  public void initAuthorizedRepresentative() {
    naturalPersonARFVBean = new NaturalPersonFVBean();
    view.initAR(naturalPersonARFVBean);
  }

  public void resetAuthorizedRepresentative() {
    naturalPersonARFVBean = new NaturalPersonFVBean();
  }

  public void resetNP() {
    naturalPersonFVBean = new NaturalPersonFVBean();
  }

  public void resetLP() {
    legalPersonFVBean = new LegalPersonFVBean();
  }

  public void notifyConceptSelection(final Set<ConceptPojo> selectedConcepts) {
    conceptQueryFVBean.setConceptRequestList(
        selectedConcepts.stream().map(x -> x.getName()).collect(Collectors.toList()));
    //        List<String> conceptList = selectedConcepts.stream().collect(Collectors.toList());
    LOGGER.debug("SELECTED CONCEPTS FROM UI: ");
    LOGGER.debug(Arrays.toString(conceptQueryFVBean.getConceptRequestList().toArray()));
  }

  public void populateConceptSelectionGrid(final ConceptQueryComponent conceptQueryComponent) {
    conceptQueryComponent.renderConceptSelectionGrid(allConcepts);
  }

  public void populateDistributionGrid(
      final DocumentQueryComponent documentQueryComponent,
      final String datasetTypeIdentifier,
      final String DPCountry)
      throws IOException {
    documentQueryComponent.renderDatasetList(
        dsdService.getDatasets(datasetTypeIdentifier, DPCountry));
    final Notification notification =
            new Notification(
                    "toop-edm:v2.0 responses are filtered out", 5000, Notification.Position.TOP_START);
    notification.addThemeVariants(
            NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_PRIMARY);
    notification.open();
  }

  public void populateConceptDatasetGrid(
      final ConceptQueryComponent conceptQueryComponent,
      final String datasetTypeIdentifier,
      final String DPCountry)
      throws IOException {
    if (!DPCountry.isEmpty()) {
      conceptQueryComponent.renderConceptDatasetList(
              dsdService.getDatasets(datasetTypeIdentifier, DPCountry));
      final Notification notification =
              new Notification(
                      "toop-edm:v2.0 responses are filtered out", 5000, Notification.Position.TOP_START);
      notification.addThemeVariants(
              NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_PRIMARY);
      notification.open();
    }

  }

  public void showResponse(final EDMResponse response) {
    if (response.getAllPayloadProviders().get(0) instanceof EDMResponsePayloadConcepts) {
      LOGGER.debug("INSIDE SHOW RESPONSE - CONCEPT: ");
      importConceptResponse(response);
    } else if (response.getAllPayloadProviders().get(0) instanceof IEDMResponsePayloadDocument
        || response.getAllPayloadProviders().get(0)
            instanceof IEDMResponsePayloadDocumentReference) {
      LOGGER.debug("INSIDE SHOW RESPONSE - DOCUMENT: ");
      importDocumentResponse(response);
    }
  }

  public void showResponse(final EDMResponseWithAttachment edmResponseWithAttachment) {
    if (edmResponseWithAttachment.getEdmResponse().getAllPayloadProviders().get(0)
            instanceof ResponseDocumentPojo
        || edmResponseWithAttachment.getEdmResponse().getAllPayloadProviders().get(0)
            instanceof ResponseDocumentReferencePojo) {
      LOGGER.debug("INSIDE SHOW RESPONSE - DOCUMENT WITH ATTACHMENT: ");
      importDocumentResponse(edmResponseWithAttachment);
    }
  }

  public void showErrorResponse(final EDMErrorResponse errorResponse) {
    LOGGER.debug("INSIDE SHOW ERROR RESPONSE: ");
    LOGGER.debug(errorResponse.getErrorProvider().toString());
    importErrorResponse(errorResponse);
  }

  public void createConceptRequestMessage(
      final ConceptQueryComponent conceptQueryComponent,
      final DSDDatasetResponseDto dsdDatasetResponseDto) {
    LOGGER.debug("Create ConceptQuery Request message ....");
    makeConceptRequests(conceptQueryComponent, dsdDatasetResponseDto);
  }

  public void makeConceptRequests(
      final ConceptQueryComponent conceptQueryComponent,
      final DSDDatasetResponseDto dsdDatasetResponseDto) {
    final EDMRequest.BuilderConcept requestBuilder = EDMRequest.builderConcept();
    addCommonSlots(requestBuilder);
    addConcepts(requestBuilder);
    final EDMRequest request = requestBuilder.randomID().build();
    submitRequest(request, dsdDatasetResponseDto);
    // LOGGER.info(request.getWriter().getAsString());
    conceptQueryComponent.downloadXML(request);
  }

  public void addConcepts(final EDMRequest.BuilderConcept requestBuilder) {
    requestBuilder.concept(
        MessageCreation.createConceptRequestListFromQName(
            conceptQueryFVBean.getConceptRequestList()));
  }

  public void addCommonSlots(final EDMRequest.BuilderConcept requestBuilder) {
    requestBuilder
        .issueDateTimeNow()
        .procedure(EToopLanguageCode.EN.getID(), conceptQueryFVBean.getProcedure())
        .dataConsumer(MessageCreation.createDataConsumer())
        .datasetIdentifier(conceptQueryFVBean.getDatasetIdentifier())
        .specificationIdentifier(conceptQueryFVBean.getSpecificationIdentifier())
        .consentToken(conceptQueryFVBean.getConsentToken());
    addDataSubject(requestBuilder);
  }

  public void addDataSubject(final EDMRequest.BuilderConcept requestBuilder) {
    // DATA SUBJECT CHECKS:
    if (Utilities.authorizedRepresentativeExistsAndNotEmpty(naturalPersonARFVBean)) {
      LOGGER.debug("INSIDE AUTHORIZED REPRESENTATIVE");
      requestBuilder.authorizedRepresentative(
          MessageCreation.createNaturalPerson(
              naturalPersonARFVBean, naturalPersonARFVBean.getAddress()));
    }
    if (Utilities.legalPersonExistsAndNotEmpty(legalPersonFVBean)) {
      LOGGER.debug("INSIDE LEGAL PERSON EXIST: ");
      requestBuilder.dataSubject(
          MessageCreation.createLegalPerson(legalPersonFVBean, legalPersonFVBean.getAddress()));
    }
    if (Utilities.naturalPersonExistsAndNotEmpty(naturalPersonFVBean)) {
      LOGGER.debug("INSIDE NATURAL PERSON BEAN");
      requestBuilder.dataSubject(
          MessageCreation.createNaturalPerson(
              naturalPersonFVBean, naturalPersonFVBean.getAddress()));
    }
  }

  private void submitRequest(
      final EDMRequest request, final DSDDatasetResponseDto dsdDatasetResponseDto) {
    try {
      ToopKafkaClient.send(EErrorLevel.INFO, "Freedonia DC Sending an EDMRequest...");
      connectionService.submit(request, dsdDatasetResponseDto);

      LOGGER.debug("Inside submit request");

      resultMap.put(request.getRequestID(), new ResultBean(request, dsdDatasetResponseDto));

      LOGGER.debug(
          "Request with id: {} added in map. Request submitted successfully.",
          request.getRequestID());

      // put into List of results too.
      resultBeanList =
          resultMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
      view.initStatusGrid(resultBeanList);

    } catch (final IOException | EvaluationException e) {
      final Notification notification =
          new Notification(
              "Unable to send request to the connector", 5000, Notification.Position.TOP_START);
      notification.addThemeVariants(
          NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
      notification.open();
      final Notification notification2 =
          new Notification(e.getMessage(), 5000, Notification.Position.TOP_START);
      notification2.addThemeVariants(
          NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
      notification2.open();
      // Kafka client log
      ToopKafkaClient.send(EErrorLevel.ERROR, e.getMessage());
      LOGGER.error(e.getMessage());
    }
  }

  public void addResponse(final EDMResponse response) {
    LOGGER.info("EDMResponse received {}", response.getRequestID());
    if (resultMap.keySet().contains(response.getRequestID())) {
      // response is mine, use it
      resultMap.get(response.getRequestID()).setStatus(EResponseStatus.SUCCESS);
      resultMap.get(response.getRequestID()).setResponse(response);
      // populate resultBeanList
      resultBeanList =
          resultMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
      view.refreshStatusGrid();
      LOGGER.debug("Result Bean List has been created successfully");
    } else {
      LOGGER.debug("EDMResponse {} is NOT mine", response.getRequestID());
    }
  }

  public void addResponse(final EDMResponseWithAttachment response) {
    LOGGER.info("EDMResponseWithAttachment received {}", response.getEdmResponse().getRequestID());
    if (resultMap.keySet().contains(response.getEdmResponse().getRequestID())) {
      // response is mine, use it
      resultMap.get(response.getEdmResponse().getRequestID()).setStatus(EResponseStatus.SUCCESS);
      resultMap.get(response.getEdmResponse().getRequestID()).setResponseWithAttachment(response);
      // populate resultBeanList
      resultBeanList =
          resultMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
      view.refreshStatusGrid();
      LOGGER.debug("Result Bean List has been created successfully");
    } else {
      LOGGER.debug(
          "EDMResponseWithAttachment {} is NOT mine", response.getEdmResponse().getRequestID());
    }
  }

  public void addResponse(final EDMErrorResponse response) {
    LOGGER.debug("EDMErrorResponse received {}", response.getRequestID());
    if (resultMap.keySet().contains(response.getRequestID())) {
      // error response is mine, use it
      resultMap.get(response.getRequestID()).setStatus(EResponseStatus.FAILED);
      resultMap.get(response.getRequestID()).setErrorResponse(response);
      // populate resultBeanList
      resultBeanList =
          resultMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
      view.refreshStatusGrid();
      LOGGER.debug("Result Bean List has been created successfully");
    } else {
      LOGGER.debug("EDMErrorResponse {} is NOT mine", response.getRequestID());
    }
  }

  /* 2nd step */
  public void createDocumentRefIDRequestMessage(
      final DocumentResponseComponent documentResponseComponent,
      final String docID,
      final RequestByIDButtonComponent requestByIDButtonComponent) {
    LOGGER.debug("CREATE DOCUMENT ID REQUEST MESSAGE...");
    // get the request coming from the response by retrieving the correct resultbean
    EDMRequest docIDRefRequest =
        resultMap
            .get(documentResponseComponent.getDocumentResponseFVBean().getRequestID())
            .getRequest();
    resetBeans();
    resetValuesDocumentQuery(docIDRefRequest);
    resetValuesDataSubject(docIDRefRequest);

    LOGGER.debug("Resend document query message: ");
    final EDMRequest.BuilderDocumentByID requestBuilder = EDMRequest.builderDocumentByID();
    addCommonSlots(requestBuilder);
//    addDataSubject(requestBuilder);
    if (documentResponseComponent
            .getDocumentResponseFVBean()
            .getResponse()
            .getAllPayloadProviders()
            .get(0)
        instanceof IEDMResponsePayloadDocumentReference) {
      addDocumentID(requestBuilder, docID);
    }
    docIDRefRequest = requestBuilder.randomID().build();

    submitRequest(
        docIDRefRequest,
        resultMap
            .get(documentResponseComponent.getDocumentResponseFVBean().getRequestID())
            .getDsdDTO());
    LOGGER.debug(docIDRefRequest.getWriter().getAsString());
    requestByIDButtonComponent.setRequest(docIDRefRequest);
  }

  /* 1st step */
  public void createDocumentRefRequestMessage(
      final DocumentQueryComponent documentQueryComponent,
      final DSDDatasetResponseDto dsdDatasetResponseDto) {
    LOGGER.debug("CREATE DOCUMENT REFERENCE REQUEST MESSAGE...");
    final EDMRequest.BuilderDocumentsByDistribution requestBuilder =
        EDMRequest.builderDocumentReferencesByDistribution();
    addCommonSlots(requestBuilder);
    addDistribution(requestBuilder);
    final EDMRequest request = requestBuilder.randomID().build();
    submitRequest(request, dsdDatasetResponseDto);
    documentQueryComponent.downloadXML(request);
  }

  // TODO: move creation methods to the beans.
  public void createDocumentRequestMessage(
      final DocumentQueryComponent documentQueryComponent,
      final DSDDatasetResponseDto dsdDatasetResponseDto) {
    LOGGER.debug("CREATE DOCUMENT REQUEST MESSAGE...");
    final EDMRequest.BuilderDocumentsByDistribution requestBuilder =
        EDMRequest.builderDocumentsByDistribution();
    addCommonSlots(requestBuilder);
    addDistribution(requestBuilder);
    final EDMRequest request = requestBuilder.randomID().build();
    submitRequest(request, dsdDatasetResponseDto);
    // LOGGER.debug(request.getWriter().getAsString());
    documentQueryComponent.downloadXML(request);
  }

  public void addDistribution(final EDMRequest.BuilderDocumentsByDistribution requestBuilder) {
    requestBuilder.addDistribution(
        DistributionPojo.builder()
            .format(EToopDistributionFormat.valueOf("UNSTRUCTURED"))
            .mediaType("application/pdf")
            .build());
  }

  public void addDocumentID(
      final EDMRequest.BuilderDocumentByID requestBuilder, final String documentID) {
    requestBuilder.documentID(documentID);
  }

  public void addCommonSlots(final EDMRequest.BuilderDocumentsByDistribution requestBuilder) {
    requestBuilder
        .issueDateTimeNow()
        .procedure(EToopLanguageCode.EN.getID()
                , documentQueryFVBean.getProcedure())
        .dataConsumer(MessageCreation.createDataConsumer())
        .datasetIdentifier(documentQueryFVBean.getDatasetIdentifier())
        .specificationIdentifier(documentQueryFVBean.getSpecificationIdentifier())
        .consentToken(documentQueryFVBean.getConsentToken());
    addDataSubject(requestBuilder);
  }

  public void addCommonSlots(final EDMRequest.BuilderDocumentByID requestBuilder) {
    requestBuilder
        .issueDateTimeNow()
        .procedure(EToopLanguageCode.EN.getID(), documentQueryFVBean.getProcedure())
        .dataConsumer(MessageCreation.createDataConsumer())
        .datasetIdentifier(documentQueryFVBean.getDatasetIdentifier())
        .specificationIdentifier(documentQueryFVBean.getSpecificationIdentifier())
        .consentToken(documentQueryFVBean.getConsentToken());
//    addDataSubject(requestBuilder);
  }

  public void addDataSubject(final EDMRequest.BuilderDocumentsByDistribution requestBuilder) {
    // DATA SUBJECT CHECKS:
    if (Utilities.authorizedRepresentativeExistsAndNotEmpty(naturalPersonARFVBean)) {
      LOGGER.debug("INSIDE AUTHORIZED REPRESENTATIVE");
      requestBuilder.authorizedRepresentative(
          MessageCreation.createNaturalPerson(
              naturalPersonARFVBean, naturalPersonARFVBean.getAddress()));
    }
    if (Utilities.legalPersonExistsAndNotEmpty(legalPersonFVBean)) {
      LOGGER.debug("INSIDE LEGAL PERSON EXIST: ");
      requestBuilder.dataSubject(
          MessageCreation.createLegalPerson(legalPersonFVBean, legalPersonFVBean.getAddress()));
    }
    if (Utilities.naturalPersonExistsAndNotEmpty(naturalPersonFVBean)) {
      LOGGER.debug("INSIDE NATURAL PERSON BEAN");
      // Data Subject must be present problem when Natural Person is indeed filled in.
      requestBuilder.dataSubject(
          MessageCreation.createNaturalPerson(
              naturalPersonFVBean, naturalPersonFVBean.getAddress()));
    }
  }

  public void addDataSubject(final EDMRequest.BuilderDocumentByID requestBuilder) {
    // DATA SUBJECT CHECKS:
    if (Utilities.authorizedRepresentativeExistsAndNotEmpty(naturalPersonARFVBean)) {
      LOGGER.debug("INSIDE AUTHORIZED REPRESENTATIVE");
      requestBuilder.authorizedRepresentative(
          MessageCreation.createNaturalPerson(
              naturalPersonARFVBean, naturalPersonARFVBean.getAddress()));
    }
    if (Utilities.legalPersonExistsAndNotEmpty(legalPersonFVBean)) {
      LOGGER.debug("INSIDE LEGAL PERSON EXIST: ");
      requestBuilder.dataSubject(
          MessageCreation.createLegalPerson(legalPersonFVBean, legalPersonFVBean.getAddress()));
    }
    if (Utilities.naturalPersonExistsAndNotEmpty(naturalPersonFVBean)) {
      LOGGER.debug("INSIDE NATURAL PERSON BEAN");
      // Data Subject must be present problem when Natural Person is indeed filled in.
      requestBuilder.dataSubject(
          MessageCreation.createNaturalPerson(
              naturalPersonFVBean, naturalPersonFVBean.getAddress()));
    }
  }

  public void resendConceptQuery(
      EDMRequest request, final DSDDatasetResponseDto dsdDatasetResponseDto) {
    resetBeans();
    resetValuesConceptQuery(request);
    resetValuesDataSubject(request);

    LOGGER.debug("Resend concept query message: ");
    final EDMRequest.BuilderConcept requestBuilder = EDMRequest.builderConcept();
    addCommonSlots(requestBuilder);
    addConcepts(requestBuilder);
//    addDataSubject(requestBuilder);
    request = requestBuilder.randomID().build();
    submitRequest(request, dsdDatasetResponseDto);
    LOGGER.info(request.getWriter().getAsString());
  }

  public void resetValuesDataSubject(final EDMRequest request) {
    if (Utilities.personPojoExists(request.getAuthorizedRepresentative())) {
      naturalPersonARFVBean.setPersonID(request.getAuthorizedRepresentative().getID());
      naturalPersonARFVBean.setPersonIDScheme(request.getAuthorizedRepresentative().getIDSchemeID());
      naturalPersonARFVBean.setPersonFamilyName(
          request.getAuthorizedRepresentative().getFamilyName());
      naturalPersonARFVBean.setPersonGivenName(
          request.getAuthorizedRepresentative().getGivenName());
      naturalPersonARFVBean.setPersonGenderCode(
          request.getAuthorizedRepresentative().getGenderCode());
      naturalPersonARFVBean.setPersonBirthName(
          request.getAuthorizedRepresentative().getBirthName());
      naturalPersonARFVBean.setPersonBirthDate(
          request.getAuthorizedRepresentative().getBirthDate());
      naturalPersonARFVBean.setPersonPlaceOfBirthAddressPostName(
          request.getAuthorizedRepresentative().getBirthTown());
      final AddressBean addressBean = new AddressBean();
      assert request.getAuthorizedRepresentative().getAddress() != null;
      addressBean.setAddressFullAddress(
          request.getAuthorizedRepresentative().getAddress().getFullAddress());
      addressBean.setAddressLocatorDesignator(
          request.getAuthorizedRepresentative().getAddress().getStreetName());
      addressBean.setAddressThoroughfare(
          request.getAuthorizedRepresentative().getAddress().getBuildingNumber());
      addressBean.setAddressPostName(request.getAuthorizedRepresentative().getAddress().getTown());
      addressBean.setAddressAdminUnitFirstline(
          request.getAuthorizedRepresentative().getAddress().getCountryCode());
      addressBean.setAddressPostCode(
          request.getAuthorizedRepresentative().getAddress().getPostalCode());
      naturalPersonARFVBean.setAddress(addressBean);
    }

    if (Utilities.personPojoExists(request.getDataSubjectNaturalPerson())) {
      naturalPersonFVBean.setPersonID(request.getDataSubjectNaturalPerson().getID());
      naturalPersonFVBean.setPersonIDScheme(request.getDataSubjectNaturalPerson().getIDSchemeID());
      naturalPersonFVBean.setPersonFamilyName(
          request.getDataSubjectNaturalPerson().getFamilyName());
      naturalPersonFVBean.setPersonGivenName(request.getDataSubjectNaturalPerson().getGivenName());
      naturalPersonFVBean.setPersonGenderCode(
          request.getDataSubjectNaturalPerson().getGenderCode());
      naturalPersonFVBean.setPersonBirthName(request.getDataSubjectNaturalPerson().getBirthName());
      naturalPersonFVBean.setPersonBirthDate(request.getDataSubjectNaturalPerson().getBirthDate());
      naturalPersonFVBean.setPersonPlaceOfBirthAddressPostName(
          request.getDataSubjectNaturalPerson().getBirthTown());
      final AddressBean addressBean = new AddressBean();
      assert request.getDataSubjectNaturalPerson().getAddress() != null;
      addressBean.setAddressFullAddress(
          request.getDataSubjectNaturalPerson().getAddress().getFullAddress());
      addressBean.setAddressLocatorDesignator(
          request.getDataSubjectNaturalPerson().getAddress().getStreetName());
      addressBean.setAddressThoroughfare(
          request.getDataSubjectNaturalPerson().getAddress().getBuildingNumber());
      addressBean.setAddressPostName(request.getDataSubjectNaturalPerson().getAddress().getTown());
      addressBean.setAddressAdminUnitFirstline(
          request.getDataSubjectNaturalPerson().getAddress().getCountryCode());
      addressBean.setAddressPostCode(
          request.getDataSubjectNaturalPerson().getAddress().getPostalCode());
      naturalPersonFVBean.setAddress(addressBean);
    }

    if (Utilities.businessPojoExists(request.getDataSubjectLegalPerson())) {
      legalPersonFVBean.setLegalEntityLegalID(request.getDataSubjectLegalPerson().getLegalID());
      legalPersonFVBean.setLegalEntityLegalIDScheme(request.getDataSubjectLegalPerson().getLegalIDSchemeID());
      legalPersonFVBean.setLegalEntityID(request.getDataSubjectLegalPerson().getID());
      legalPersonFVBean.setLegalEntityIDScheme(request.getDataSubjectLegalPerson().getIDSchemeID());
      legalPersonFVBean.setLegalEntityName(request.getDataSubjectLegalPerson().getLegalName());

      final AddressBean addressBean = new AddressBean();
      assert request.getDataSubjectLegalPerson().getAddress() != null;
      addressBean.setAddressFullAddress(
          request.getDataSubjectLegalPerson().getAddress().getFullAddress());
      addressBean.setAddressLocatorDesignator(
          request.getDataSubjectLegalPerson().getAddress().getStreetName());
      addressBean.setAddressThoroughfare(
          request.getDataSubjectLegalPerson().getAddress().getBuildingNumber());
      addressBean.setAddressPostName(request.getDataSubjectLegalPerson().getAddress().getTown());
      addressBean.setAddressAdminUnitFirstline(
          request.getDataSubjectLegalPerson().getAddress().getCountryCode());
      addressBean.setAddressPostCode(
          request.getDataSubjectLegalPerson().getAddress().getPostalCode());
      legalPersonFVBean.setAddress(addressBean);
    }
  }

  public void resetValuesConceptQuery(final EDMRequest request) {
    if (request.getProcedure() != null) {
      conceptQueryFVBean.setProcedure(request.getProcedure().getLocalizedString().get(0).getValue());
    }
    conceptQueryFVBean.setDatasetIdentifier(request.getDatasetIdentifier());
    conceptQueryFVBean.setSpecificationIdentifier(request.getSpecificationIdentifier());
    conceptQueryFVBean.setConsentToken(request.getConsentToken());
    final EDMRequestPayloadConcepts conceptPayload =
        (EDMRequestPayloadConcepts) request.getPayloadProvider();
    List<QName> concepts =
        conceptPayload.getAllConcepts().stream()
            .map(ConceptPojo -> ConceptPojo.getName())
            .collect(Collectors.toList());
    concepts.addAll(
        conceptPayload.getAllConcepts().get(0).getAllChildren().stream()
            .map(ConceptPojo -> ConceptPojo.getName())
            .collect(Collectors.toList()));
    conceptQueryFVBean.setConceptRequestList(concepts);
    //    LOGGER.debug("DEBUGGING RESEND CONCEPTS: ");
    //    LOGGER.debug(Arrays.toString(conceptQueryFVBean.getConceptRequestList().stream().map(x->
    // x.getLocalPart()).collect(Collectors.toList()).toArray()));
  }

  public void resendDocumentQuery(
      EDMRequest request, final DSDDatasetResponseDto dsdDatasetResponseDto) {
    resetBeans();
    resetValuesDocumentQuery(request);
    resetValuesDataSubject(request);

    LOGGER.debug("Resend document query message: ");
    final EDMRequest.BuilderDocumentsByDistribution requestBuilder =
        EDMRequest.builderDocumentsByDistribution();
    addCommonSlots(requestBuilder);
    addDataSubject(requestBuilder);
    addDistribution(requestBuilder);
    request = requestBuilder.randomID().build();
    submitRequest(request, dsdDatasetResponseDto);
    // LOGGER.debug(request.getWriter().getAsString());
  }

  public void resendDocumentQueryByID(
          EDMRequest request, final DSDDatasetResponseDto dsdDatasetResponseDto) {
    resetBeans();
    resetValuesDocumentQuery(request);
//    resetValuesDataSubject(request);

    LOGGER.debug("Resend document query message: ");
    final EDMRequest.BuilderDocumentByID requestBuilder = EDMRequest.builderDocumentByID();
    addCommonSlots(requestBuilder);
//    addDataSubject(requestBuilder);
    addDocumentID(requestBuilder, ((IEDMRequestPayloadDocumentID) request.getPayloadProvider()).getDocumentID());
    request = requestBuilder.randomID().build();
    submitRequest(request, dsdDatasetResponseDto);
    // LOGGER.debug(request.getWriter().getAsString());
  }

  public void resetValuesDocumentQuery(final EDMRequest request) {
    documentQueryFVBean.setProcedure(request.getProcedure().toString());
    documentQueryFVBean.setDatasetIdentifier(request.getDatasetIdentifier());
    documentQueryFVBean.setSpecificationIdentifier(request.getSpecificationIdentifier());
    documentQueryFVBean.setConsentToken(request.getConsentToken());

    if (request.getQueryDefinition().getID().equals(EToopQueryDefinitionType.DOCUMENT_BY_ID.getID())) {
      final EDMRequestPayloadDocumentID edmRequestPayloadDocumentID = (EDMRequestPayloadDocumentID) request.getPayloadProvider();
      documentQueryFVBean.setDocumentID(edmRequestPayloadDocumentID.getDocumentID());
    } else if (request.getQueryDefinition().getID().equals(EToopQueryDefinitionType.DOCUMENT_BY_DISTRIBUTION.getID())) {
      final EDMRequestPayloadDistribution distributionPayload =
              (EDMRequestPayloadDistribution) request.getPayloadProvider();
      documentQueryFVBean.setDistributionList(distributionPayload.getAllDistributions());
    }


  }

  public void resetBeans() {
    conceptQueryFVBean = new ConceptQueryFVBean();
    documentQueryFVBean = new DocumentQueryFVBean();
    naturalPersonFVBean = new NaturalPersonFVBean();
    legalPersonFVBean = new LegalPersonFVBean();
    naturalPersonARFVBean = new NaturalPersonFVBean();
    view.resetBeans(legalPersonFVBean, naturalPersonFVBean, naturalPersonARFVBean, conceptQueryFVBean, documentQueryFVBean);
  }

  public void importResponseFromUpload(final MemoryBuffer buffer) {
    final EDMResponse response = EDMResponse.reader().read(buffer.getInputStream());
    conceptResponseFVBean.setRequestID(response.getRequestID());
    conceptResponseFVBean.setDataProviderID(response.getDataProvider().getID());
    conceptResponseFVBean.setDataProviderName(response.getDataProvider().getName());
    if (response.getAllPayloadProviders().get(0) instanceof EDMResponsePayloadConcepts) {
      final EDMResponsePayloadConcepts conceptPayload =
          (EDMResponsePayloadConcepts) response.getAllPayloadProviders().get(0);
      conceptResponseFVBean.setAllConcepts(conceptPayload.getAllConcepts());
      conceptResponseFVBean.setRootConcept(conceptPayload.getAllConcepts().get(0));
      conceptResponseFVBean.setConcepts(conceptPayload.concepts().get(0).getAllChildren());
    }
    LOGGER.debug("PARENT CONCEPT: " + conceptResponseFVBean.getAllConcepts().get(0).getID());

    view.renderConceptResponse(conceptResponseFVBean);
  }

  public void importDocumentResponse(final EDMResponse response) {
    documentResponseFVBean.setResponse(response);
    documentResponseFVBean.setRequestID(response.getRequestID());
    documentResponseFVBean.setDataProviderID(response.getDataProvider().getID());
    documentResponseFVBean.setDataProviderName(response.getDataProvider().getName());

    LOGGER.debug("PAYLOAD PROVIDER SIZE: " + response.getAllPayloadProviders().size());

    if (response.getAllPayloadProviders().get(0) instanceof IEDMResponsePayloadDocumentReference) {
      final List<DatasetPojo> datasetList = new ArrayList<>();
      for (final IEDMResponsePayloadProvider element : response.getAllPayloadProviders()) {
        datasetList.add(((IEDMResponsePayloadDocumentReference) element).getDataset());
        documentResponseFVBean.setDatasetList(datasetList);
      }

      final IEDMResponsePayloadDocumentReference documentPayload =
          (IEDMResponsePayloadDocumentReference) response.getAllPayloadProviders().get(0);
      documentResponseFVBean.setDataset(documentPayload.getDataset());
    }

    if (response.getAllPayloadProviders().get(0) instanceof IEDMResponsePayloadDocument) {
      final List<DatasetPojo> datasetList = new ArrayList<>();
      for (final IEDMResponsePayloadProvider element : response.getAllPayloadProviders()) {
        datasetList.add(((IEDMResponsePayloadDocument) element).getDataset());
        LOGGER.debug("DatasetList is " + datasetList);

        documentResponseFVBean.setDatasetList(datasetList);
      }
    }
    LOGGER.debug("Import Document Response NOT YET READY. ");
    view.renderDocumentResponse(documentResponseFVBean);
  }

  public void importDocumentResponse(final EDMResponseWithAttachment responseWithAttachment) {
    documentResponseFVBean.setResponseWithAttachment(responseWithAttachment);
    documentResponseFVBean.setRequestID(responseWithAttachment.getEdmResponse().getRequestID());
    documentResponseFVBean.setDataProviderID(
        responseWithAttachment.getEdmResponse().getDataProvider().getID());
    documentResponseFVBean.setDataProviderName(
        responseWithAttachment.getEdmResponse().getDataProvider().getName());

    LOGGER.debug(
        "PAYLOAD PROVIDER SIZE: "
            + responseWithAttachment.getEdmResponse().getAllPayloadProviders().size());

    if (responseWithAttachment.getEdmResponse().getAllPayloadProviders().get(0)
        instanceof IEDMResponsePayloadDocumentReference) {
      final List<DatasetPojo> datasetList = new ArrayList<>();
      for (final IEDMResponsePayloadProvider element :
          responseWithAttachment.getEdmResponse().getAllPayloadProviders()) {
        datasetList.add(((IEDMResponsePayloadDocumentReference) element).getDataset());
        documentResponseFVBean.setDatasetList(datasetList);
      }

      final IEDMResponsePayloadDocumentReference documentPayload =
          (IEDMResponsePayloadDocumentReference)
              responseWithAttachment.getEdmResponse().getAllPayloadProviders().get(0);
      documentResponseFVBean.setDataset(documentPayload.getDataset());
    }

    if (responseWithAttachment.getEdmResponse().getAllPayloadProviders().get(0)
        instanceof IEDMResponsePayloadDocument) {
      final List<DatasetPojo> datasetList = new ArrayList<>();
      for (final IEDMResponsePayloadProvider element :
          responseWithAttachment.getEdmResponse().getAllPayloadProviders()) {
        datasetList.add(((IEDMResponsePayloadDocument) element).getDataset());
        LOGGER.debug("DatasetList is " + datasetList);

        documentResponseFVBean.setDatasetList(datasetList);
      }
    }
    LOGGER.debug("Import Document ResponseWithAttachment. ");
    view.renderDocumentResponse(documentResponseFVBean);
  }

  public void importConceptResponse(final EDMResponse response) {
    LOGGER.debug("INSIDE importConceptResponse: ");
    conceptResponseFVBean.setRequestID(response.getRequestID());
    conceptResponseFVBean.setDataProviderID(response.getDataProvider().getID());
    conceptResponseFVBean.setDataProviderName(response.getDataProvider().getName());
    if (response.getAllPayloadProviders().get(0) instanceof EDMResponsePayloadConcepts) {
      final IEDMResponsePayloadConcepts conceptPayload =
          (IEDMResponsePayloadConcepts) response.getAllPayloadProviders().get(0);
      //      LOGGER.debug("PARENT CONCEPT: " +conceptPayload.getAllConcepts().get(0).toString());
      conceptResponseFVBean.setAllConcepts(conceptPayload.getAllConcepts());
      conceptResponseFVBean.setRootConcept(conceptPayload.getAllConcepts().get(0));
      conceptResponseFVBean.setConcepts(conceptPayload.concepts().get(0).getAllChildren());
    }

    view.renderConceptResponse(conceptResponseFVBean);
  }

  public void importErrorResponse(final EDMErrorResponse errorResponse) {
    LOGGER.debug("INSIDE importErrorResponse: ");
    errorResponseFVBean.setErrorProviderID(errorResponse.getErrorProvider().getID());
    errorResponseFVBean.setErrorProviderName(errorResponse.getErrorProvider().getName());
    errorResponseFVBean.setRequestID(errorResponse.getRequestID());
    errorResponseFVBean.setResponseStatus(errorResponse.getResponseStatus().toString());
    errorResponseFVBean.setExceptionList(errorResponse.getAllExceptions());
    view.renderErrorResponse(errorResponseFVBean);
  }

  public void validateDS() {
    view.validateDS();
  }

  public void hasValidationErrors(boolean validationResult) {
    hasErrors = validationResult;
  }

  public boolean validationCheckPass() {
    return hasErrors;
  };
}
