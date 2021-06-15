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
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

import eu.toop.playground.dc.ui.model.LegalPersonFVBean;
import eu.toop.playground.dc.ui.model.NaturalPersonFVBean;
import eu.toop.playground.dc.ui.presenter.MainPresenter;
import eu.toop.playground.dc.ui.util.Utilities;

/**
 * @author Maria Siapera [mariaspr at unipi.gr]
 */
public class DataSubjectComponent extends Composite<FormLayout> {

    private final MainPresenter presenter;
    Accordion dataSubjectAccordion = null;
    Accordion authorizedRepresentativeAccordion = null;
    Button authorizedRepresentativeBtn;
    boolean NPFlag = false;
    boolean LPFlag = false;
    boolean ARFlag = false;
    boolean NPValResult = false;
    boolean LPValResult = false;
    boolean ARValResult = false;
    NaturalPersonComponent naturalPersonComponent;
    NaturalPersonComponent arComponent;
    LegalPersonComponent legalPersonComponent;

    public DataSubjectComponent(final MainPresenter presenter) {
        this.presenter = presenter;
        // DATA SUBJECT RADIO GROUP
        final RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Create Request for Natural or Legal Person: ");
        radioGroup.setItems("Natural Person", "Legal Person");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        radioGroup.addValueChangeListener(
                e -> {
                    if (accordionExists(dataSubjectAccordion)) {
                        // reset accordion
                        getContent().remove(dataSubjectAccordion);
                    }
                    presenter.initDataSubject(e);
                });

        getContent().add(radioGroup);

        authorizedRepresentativeBtn =
                new Button("Authorized Representative", new Icon(VaadinIcon.PLUS_CIRCLE));
        authorizedRepresentativeBtn.addThemeVariants(
                ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);

        authorizedRepresentativeBtn.addClickListener(
                click -> {
                    presenter.initAuthorizedRepresentative();
                });

        getContent().add(authorizedRepresentativeBtn);
    }

    // render methods
    public void renderNP(final NaturalPersonFVBean naturalPersonFVBean) {
        presenter.resetLP();
        NPFlag = true;
        LPFlag = false;
        resetValidationFlags();
        System.out.println("renderNP called");
        resetDataSubjectAccordion();
        naturalPersonComponent = new NaturalPersonComponent(naturalPersonFVBean);

        dataSubjectAccordion.add("Natural Person", naturalPersonComponent);
        getContent().add(dataSubjectAccordion);
    }

    public void renderLP(final LegalPersonFVBean legalPersonFVBean) {
        presenter.resetNP();
        LPFlag = true;
        NPFlag = false;
        resetValidationFlags();
        System.out.println("renderLP called");
        resetDataSubjectAccordion();

        legalPersonComponent = new LegalPersonComponent(legalPersonFVBean);
        dataSubjectAccordion.add("Legal Person", legalPersonComponent);
        getContent().add(dataSubjectAccordion);
    }

    public void renderAR(final NaturalPersonFVBean naturalPersonARFVBean) {
        arComponent = new NaturalPersonComponent(naturalPersonARFVBean);
        if (!accordionExists(authorizedRepresentativeAccordion)) {
            System.out.println("ACCORDION ADD CALLED: ");
            authorizedRepresentativeAccordion = new Accordion();
            authorizedRepresentativeBtn.setText("Remove Authorized Representative: ");
            authorizedRepresentativeBtn.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE));
            authorizedRepresentativeBtn.addThemeVariants(
                    ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            authorizedRepresentativeAccordion.add(
                    "Authorized Representative", arComponent);
            getContent().add(authorizedRepresentativeAccordion);
            authorizedRepresentativeBtn.getStyle().set("backgroundColor", "#f52419");
            ARFlag = true;
        } else if (accordionExists(authorizedRepresentativeAccordion)) {
            System.out.println("AUTHORIZED ACCORDION REMOVE called");
            authorizedRepresentativeBtn.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
            authorizedRepresentativeBtn.setText("Authorized Representative");
            getContent().remove(authorizedRepresentativeAccordion);
            resetAuthRepAccordion();
            presenter.resetAuthorizedRepresentative();
            authorizedRepresentativeBtn.getStyle().set("backgroundColor", "#1676f3");
            ARFlag = false;
        }
    }

    public void resetAuthRepAccordion() {
        authorizedRepresentativeAccordion = null;
    }

    public void resetDataSubjectAccordion() {
        dataSubjectAccordion = new Accordion();
    }

    public boolean accordionExists(final Accordion accordion) {
        return accordion != null;
    }

    public boolean validate() {
        if (NPFlag) {
            NPValResult = naturalPersonComponent.validate();
        }
        if (LPFlag) {
            LPValResult = legalPersonComponent.validate();
        }
        if (ARFlag) {
            ARValResult = arComponent.validate();
        }
        return NPValResult || LPValResult || ARValResult;
    }

    public void resetValidationFlags() {
        NPValResult = false;
        LPValResult = false;
        ARValResult = false;
    }

    /* broken bindings after resend request fix*/
    public void setBeans(final LegalPersonFVBean legalPersonFVBean, final NaturalPersonFVBean naturalPersonFVBean, final NaturalPersonFVBean naturalPersonARFVBean) {
        if (Utilities.legalPersonComponentExists(legalPersonComponent)) {legalPersonComponent.setBean(legalPersonFVBean);}
        if (Utilities.naturalPersonComponentExists(naturalPersonComponent)) {naturalPersonComponent.setBean(naturalPersonFVBean);}
        if (Utilities.naturalPersonComponentExists(arComponent)) { arComponent.setBean(naturalPersonARFVBean);}
    }
}
