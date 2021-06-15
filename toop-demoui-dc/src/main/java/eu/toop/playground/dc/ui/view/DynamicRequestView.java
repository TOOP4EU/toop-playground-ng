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
package eu.toop.playground.dc.ui.view;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.shared.Registration;

import eu.toop.kafkaclient.ToopKafkaSettings;
import eu.toop.playground.dc.config.enums.DCConfig;
import eu.toop.playground.dc.ui.component.ConceptResponseComponent;
import eu.toop.playground.dc.ui.component.DataConsumerDetailsComponent;
import eu.toop.playground.dc.ui.component.DataProviderDetailsComponent;
import eu.toop.playground.dc.ui.component.DataSubjectComponent;
import eu.toop.playground.dc.ui.component.DocumentResponseComponent;
import eu.toop.playground.dc.ui.component.ErrorResponseComponent;
import eu.toop.playground.dc.ui.component.QueryDefinitionComponent;
import eu.toop.playground.dc.ui.component.RequestStatusGridComponent;
import eu.toop.playground.dc.ui.component.UploadComponent;
import eu.toop.playground.dc.ui.model.ConceptQueryFVBean;
import eu.toop.playground.dc.ui.model.ConceptResponseFVBean;
import eu.toop.playground.dc.ui.model.DocumentQueryFVBean;
import eu.toop.playground.dc.ui.model.DocumentResponseFVBean;
import eu.toop.playground.dc.ui.model.ErrorResponseFVBean;
import eu.toop.playground.dc.ui.model.LegalPersonFVBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;
import eu.toop.playground.dc.ui.model.ResultBean;
import eu.toop.playground.dc.ui.presenter.MainPresenter;
import eu.toop.playground.dc.ui.service.BroadcasterService;
import eu.toop.playground.dc.ui.util.Utilities;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
@Route(value = "")
@Push
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class DynamicRequestView extends VerticalLayout implements RouterLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicRequestView.class);

    private MainPresenter presenter;
    Tab requestTab = new Tab("REQUEST");
    Tab responseTab = new Tab("RESPONSE");
    Div requestPage = new Div();
    Div responsePage = new Div();

    final Div header = new Div();
    final Div content = new Div();
    final Div queryContent = new Div();
    private DataConsumerDetailsComponent DCComponent;
    private DataSubjectComponent DSComponent;
    private QueryDefinitionComponent queryDefinitionComponent;
    private UploadComponent upload;
    private RequestStatusGridComponent requestStatusGridComponent;
    private DataProviderDetailsComponent dataProviderDetailsComponent;
    private ConceptResponseComponent conceptResponseComponent;
    private DocumentResponseComponent documentResponseComponent;
    private ErrorResponseComponent errorResponseComponent;

    private Registration broadcastRespRegistration;
    private Registration broadcastRespWithAttachRegistration;
    private Registration broadcastErrorRespRegistration;

    public DynamicRequestView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        presenter = new MainPresenter();
        presenter.init(this);
        //    upload = new UploadComponent(presenter);

        DSComponent = new DataSubjectComponent(presenter);

        header.getStyle().set("flexShrink", "0");
        header.setText("FREEDONIA | ONLINE");
        header.setClassName("header");
        header.setHeight("70px");

        content.setClassName("content");
        queryContent.setClassName("queryContent");

        add(header);

        H2 dataSubjectHeader = new H2("Data Subject Creation: ");
        H2 conceptHeader = new H2("Query Definition: ");

        DCComponent = new DataConsumerDetailsComponent();

        requestPage.add(DCComponent, dataSubjectHeader, content, conceptHeader, queryContent);
        requestPage.setSizeFull();

        responsePage.setSizeFull();
        responsePage.setClassName("responsePage");

        // TABS LOGIC
        Tabs tabs = new Tabs(requestTab, responseTab);
        tabs.setSelectedTab(requestTab);
        tabs.setFlexGrowForEnclosedTabs(1);
        add(tabs);
        add(requestPage);
        add(responsePage);
        requestPage.setVisible(true);
        responsePage.setVisible(false);

        tabs.addSelectedChangeListener(
                e -> {
                    if (e.getSelectedTab() == requestTab) {
                        requestPage.setVisible(true);
                        responsePage.setVisible(false);
                    } else {
                        requestPage.setVisible(false);
                        responsePage.setVisible(true);
                    }
                });

        // add DataSubject to view
        content.add(DSComponent);

        // REQUEST TYPE RADIOGROUP

        queryDefinitionComponent = new QueryDefinitionComponent(presenter, requestPage);
        queryContent.add(queryDefinitionComponent);

        // Upload Component is not needed, Kept for debugging.
        //    responsePage.add(upload);
        requestStatusGridComponent = new RequestStatusGridComponent();
        responsePage.add(requestStatusGridComponent);

        initKafkaClient();
    }

    private void initKafkaClient() {
        LOGGER.debug("Initializing Kafka-Client...");
        LOGGER.debug("Kafka-Client is enabled = {}", DCConfig.INSTANCE.isKafkaClientEnabled());

        if (DCConfig.INSTANCE.isKafkaClientEnabled()) {
            ToopKafkaSettings.setKafkaEnabled(true);
            ToopKafkaSettings.defaultProperties()
                    .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, DCConfig.INSTANCE.getKafkaClientURL());
            ToopKafkaSettings.setKafkaTopic(DCConfig.INSTANCE.getKafkaClientTopic());
        } else {
            ToopKafkaSettings.setKafkaEnabled(false);
        }
    }

    public void initStatusGrid(List<ResultBean> results) {
        requestStatusGridComponent.renderStatusGrid(results, presenter);
    }

    public void refreshStatusGrid() {
        requestStatusGridComponent.refreshGrid();
    }

    public void initNP(NaturalPersonFVBean naturalPersonFVBean) {
        DSComponent.renderNP(naturalPersonFVBean);
    }

    public void initLP(LegalPersonFVBean legalPersonFVBean) {
        DSComponent.renderLP(legalPersonFVBean);
    }

    public void initAR(NaturalPersonFVBean naturalPersonARFVBean) {
        DSComponent.renderAR(naturalPersonARFVBean);
    }

    public void initConceptQuery(ConceptQueryFVBean conceptQueryFVBean) {
        presenter.validateDS();
        LOGGER.debug("Init concept query is called: ");
        queryDefinitionComponent.renderConceptQuery(conceptQueryFVBean);
    }

    public void initDocumentQuery(DocumentQueryFVBean documentQueryFVBean, String queryDef) {
        presenter.validateDS();
        LOGGER.debug("Init document query is called: ");
        queryDefinitionComponent.renderDocumentQuery(documentQueryFVBean, queryDef);
    }

    public void renderConceptResponse(ConceptResponseFVBean conceptResponseFVBean) {
        LOGGER.debug("render concept response called: ");
        resetRenderedResponseComponents();

        dataProviderDetailsComponent = new DataProviderDetailsComponent(conceptResponseFVBean);
        conceptResponseComponent = new ConceptResponseComponent(conceptResponseFVBean);
        responsePage.add(dataProviderDetailsComponent, conceptResponseComponent);
    }

    public void renderDocumentResponse(DocumentResponseFVBean documentResponseFVBean) {
        resetRenderedResponseComponents();

        dataProviderDetailsComponent = new DataProviderDetailsComponent(documentResponseFVBean);
        documentResponseComponent = new DocumentResponseComponent(documentResponseFVBean, presenter);
        responsePage.add(dataProviderDetailsComponent, documentResponseComponent);
    }

    public void renderErrorResponse(ErrorResponseFVBean errorResponseFVBean) {
        resetRenderedResponseComponents();

        dataProviderDetailsComponent = new DataProviderDetailsComponent(errorResponseFVBean);
        errorResponseComponent = new ErrorResponseComponent(errorResponseFVBean);
        responsePage.add(dataProviderDetailsComponent, errorResponseComponent);
    }

    public void resetRenderedResponseComponents() {
        if (Utilities.dataProviderDetailsComponentExists(dataProviderDetailsComponent)
                && Utilities.documentResponseComponentExists(documentResponseComponent)) {
            responsePage.remove(dataProviderDetailsComponent);
            responsePage.remove(documentResponseComponent);
        }

        if (Utilities.dataProviderDetailsComponentExists(dataProviderDetailsComponent)
                && Utilities.conceptResponseComponentExists(conceptResponseComponent)) {
            responsePage.remove(dataProviderDetailsComponent);
            responsePage.remove(conceptResponseComponent);
        }

        if (Utilities.dataProviderDetailsComponentExists(dataProviderDetailsComponent)
                && Utilities.errorResponseComponentExists(errorResponseComponent)) {
            responsePage.remove(dataProviderDetailsComponent);
            responsePage.remove(errorResponseComponent);
        }
    }

    /* broken bindings after resend request fix*/
    public void resetBeans(LegalPersonFVBean legalPersonFVBean,
                           NaturalPersonFVBean naturalPersonFVBean,
                           NaturalPersonFVBean naturalPersonARFVBean,
                           ConceptQueryFVBean conceptQueryFVBean,
                           DocumentQueryFVBean documentQueryFVBean) {
        DSComponent.setBeans(legalPersonFVBean, naturalPersonFVBean, naturalPersonARFVBean);
        queryDefinitionComponent.setBeans(conceptQueryFVBean, documentQueryFVBean);
    }

    public void validateDS() {
        presenter.hasValidationErrors(DSComponent.validate());
    }

    public Accordion resetAccordion() {
        return new Accordion();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        LOGGER.debug("On Attach Called");
        UI ui = attachEvent.getUI();
        broadcastRespRegistration =
                BroadcasterService.INSTANCE.registerResponse(
                        response -> ui.access(() -> presenter.addResponse(response)));

        broadcastErrorRespRegistration =
                BroadcasterService.INSTANCE.registerErrorResponse(
                        response -> ui.access(() -> presenter.addResponse(response)));

        broadcastRespWithAttachRegistration =
                BroadcasterService.INSTANCE.registerResponseWithAttachment(
                        response -> ui.access(() -> presenter.addResponse(response)));

        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        LOGGER.debug("On Detach Called");
        broadcastRespRegistration.remove();
        broadcastErrorRespRegistration.remove();
        broadcastRespWithAttachRegistration.remove();
        broadcastRespRegistration = null;
        broadcastErrorRespRegistration = null;
        broadcastRespWithAttachRegistration = null;
    }
}
