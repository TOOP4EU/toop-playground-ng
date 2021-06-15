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
package eu.toop.playground.dp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;

public class GBMDataset extends Dataset{
    private List<Concept> concepts;


    public GBMDataset() {
    }

    public GBMDataset(List<Concept> concepts, String naturalPersonID, String legalPersonID, String legalRepresentativeID) {
        this.concepts = concepts;
        this.naturalPersonID = naturalPersonID;
        this.legalPersonID = legalPersonID;
        this.legalRepresentativeID = legalRepresentativeID;
    }

    public List<Concept> getConcepts() {
        return concepts;
    }

    @JsonIgnore
    public List<ConceptPojo> getConceptPojoList() {
        List<ConceptPojo> conceptPojoList = new ArrayList<>(concepts.size());
        for (Concept concept : concepts) {
            conceptPojoList.add(ConceptPojo.builder()
                    .randomID()
                    .name(EToopConcept.NAMESPACE_URI, concept.getName())
                    .children(getAsConceptPojoList(concept.getConcepts()))
                    .build());
        }
        return conceptPojoList;
    }

    @JsonIgnore
    private List<ConceptPojo> getAsConceptPojoList(List<GBMDataset.Concept> gbmDatasetConcept) {
        return gbmDatasetConcept.stream()
                .map(concept -> {
                            ConceptPojo.Builder builder = ConceptPojo
                                    .builder()
                                    .randomID()
                                    .name(EToopConcept.NAMESPACE_URI, concept.getName());
                            if ((concept.getAmountValue() != null) && (concept.getAmountCurrency() != null))
                                builder.valueAmount(concept.getAmountValue(), concept.getAmountCurrency());
                            if (concept.getTextValue() != null)
                                builder.valueText(concept.getTextValue());
                            if (concept.getCodeValue() != null)
                                builder.valueCode(concept.getCodeValue());
                            if (concept.getDateValue() != null)
                                builder.valueDate(concept.getDateValue());
                            if (concept.getIndicatorValue() != null)
                                builder.valueIndicator(concept.getIndicatorValue());
                            if ((concept.getMeasureValue() != null) && (concept.getMeasureUnit() != null))
                                builder.valueMeasure(concept.getMeasureValue(), concept.getMeasureUnit());
                            if (concept.getQuantityValue() != null)
                                builder.valueQuantity(concept.getQuantityValue(), concept.getQuantityUnit());
                            if (concept.getNumericValue() != null)
                                builder.valueNumeric(concept.getNumericValue());
                            if (concept.getTimeValue() != null)
                                builder.valueTime(concept.getTimeValue());
                            if (concept.getUriValue() != null)
                                builder.valueURI(concept.getUriValue());
                            if ((concept.getPeriodStartValue() != null) && (concept.getPeriodEndValue() != null))
                                builder.valuePeriod(concept.getPeriodStartValue(), concept.getPeriodEndValue());
                            return builder.children(concept.getConcepts() == null ? null : getAsConceptPojoList(concept.getConcepts()))
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "GBMDataset{" +
                "concepts=" + concepts +
                ", naturalPersonID='" + naturalPersonID + "'\n" +
                ", legalPersonID='" + legalPersonID + "'\n" +
                ", legalRepresentativeID='" + legalRepresentativeID + "'\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GBMDataset that = (GBMDataset) o;
        return Objects.equals(concepts, that.concepts) &&
                Objects.equals(naturalPersonID, that.naturalPersonID) &&
                Objects.equals(legalPersonID, that.legalPersonID) &&
                Objects.equals(legalRepresentativeID, that.legalRepresentativeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concepts, naturalPersonID, legalPersonID, legalRepresentativeID);
    }

    public static class Concept {
        private String name;
        private List<Concept> concepts;
        private String textValue;
        private String amountCurrency;
        private BigDecimal amountValue;
        private String codeValue;
        private LocalDate dateValue;
        private Boolean indicatorValue;
        private String measureUnit;
        private BigDecimal measureValue;
        private BigDecimal numericValue;
        private LocalDateTime periodStartValue;
        private LocalDateTime periodEndValue;
        private BigDecimal quantityValue;
        private String quantityUnit;
        private LocalTime timeValue;
        private String uriValue;
        private String errorCode;
        private String idValue;

        public Concept() {

        }

        public String getIdValue() {
            return idValue;
        }

        public void setIdValue(String idValue) {
            this.idValue = idValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Concept> getConcepts() {
            return concepts;
        }

        public void setConcepts(List<Concept> concepts) {
            this.concepts = concepts;
        }

        public String getTextValue() {
            return textValue;
        }

        public void setTextValue(String textValue) {
            this.textValue = textValue;
        }

        public String getAmountCurrency() {
            return amountCurrency;
        }

        public void setAmountCurrency(String amountCurrency) {
            this.amountCurrency = amountCurrency;
        }

        public BigDecimal getAmountValue() {
            return amountValue;
        }

        public void setAmountValue(BigDecimal amountValue) {
            this.amountValue = amountValue;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public LocalDate getDateValue() {
            return dateValue;
        }

        public void setDateValue(LocalDate dateValue) {
            this.dateValue = dateValue;
        }

        public Boolean getIndicatorValue() {
            return indicatorValue;
        }

        public void setIndicatorValue(Boolean indicatorValue) {
            this.indicatorValue = indicatorValue;
        }

        public String getMeasureUnit() {
            return measureUnit;
        }

        public void setMeasureUnit(String measureUnit) {
            this.measureUnit = measureUnit;
        }

        public BigDecimal getMeasureValue() {
            return measureValue;
        }

        public void setMeasureValue(BigDecimal measureValue) {
            this.measureValue = measureValue;
        }

        public BigDecimal getNumericValue() {
            return numericValue;
        }

        public void setNumericValue(BigDecimal numericValue) {
            this.numericValue = numericValue;
        }

        public LocalDateTime getPeriodStartValue() {
            return periodStartValue;
        }

        public void setPeriodStartValue(LocalDateTime periodStartValue) {
            this.periodStartValue = periodStartValue;
        }

        public LocalDateTime getPeriodEndValue() {
            return periodEndValue;
        }

        public void setPeriodEndValue(LocalDateTime periodEndValue) {
            this.periodEndValue = periodEndValue;
        }

        public BigDecimal getQuantityValue() {
            return quantityValue;
        }

        public void setQuantityValue(BigDecimal quantityValue) {
            this.quantityValue = quantityValue;
        }

        public String getQuantityUnit() {
            return quantityUnit;
        }

        public void setQuantityUnit(String quantityUnit) {
            this.quantityUnit = quantityUnit;
        }

        public LocalTime getTimeValue() {
            return timeValue;
        }

        public void setTimeValue(LocalTime timeValue) {
            this.timeValue = timeValue;
        }

        public String getUriValue() {
            return uriValue;
        }

        public void setUriValue(String uriValue) {
            this.uriValue = uriValue;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }


        @Override
        public String toString() {
            Concept concept = this;
            StringBuilder sb = new StringBuilder();
            sb.append("Concept{" + "name='").append(name).append("'\n")
                    .append(", concepts=").append(concepts).append('\n');
            if (concept.getAmountValue() != null)
                sb.append(", amountCurrency='").append(amountCurrency).append("'\n")
                        .append(", amountValue=").append(amountValue).append('\n');
            if (concept.getTextValue() != null)
                sb.append(", textValue='").append(textValue).append("'\n");
            if (concept.getCodeValue() != null)
                sb.append(", codeValue='").append(codeValue).append("'\n");
            if (concept.getDateValue() != null)
                sb.append(", dateValue=").append(dateValue).append('\n');
            if (concept.getIndicatorValue() != null)
                sb.append(", indicatorValue=").append(indicatorValue).append('\n');
            if ((concept.getMeasureValue() != null) && (concept.getMeasureUnit() != null))
                sb.append(", measureUnit='").append(measureUnit).append("'\n").append(", measureValue='").append(measureValue).append("'\n");
            if (concept.getQuantityValue() != null)
                sb.append(", quantityValue=").append(quantityValue).append('\n').append(", quantityUnit='").append(quantityUnit).append("'\n");
            if (concept.getNumericValue() != null)
                sb.append(", numericValue=").append(numericValue).append('\n');
            if (concept.getTimeValue() != null)
                sb.append(", timeValue=").append(timeValue).append('\n');
            if (concept.getUriValue() != null)
                sb.append(", uriValue='").append(uriValue).append("'\n");
            if ((concept.getPeriodStartValue() != null) && (concept.getPeriodEndValue() != null))
                sb.append(", periodStartValue=").append(periodStartValue).append('\n').append(", periodEndValue=").append(periodEndValue).append('\n');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Concept concept = (Concept) o;
            return Objects.equals(name, concept.name) &&
                    Objects.equals(concepts, concept.concepts) &&
                    Objects.equals(textValue, concept.textValue) &&
                    Objects.equals(amountCurrency, concept.amountCurrency) &&
                    Objects.equals(amountValue, concept.amountValue) &&
                    Objects.equals(codeValue, concept.codeValue) &&
                    Objects.equals(dateValue, concept.dateValue) &&
                    Objects.equals(indicatorValue, concept.indicatorValue) &&
                    Objects.equals(measureUnit, concept.measureUnit) &&
                    Objects.equals(measureValue, concept.measureValue) &&
                    Objects.equals(numericValue, concept.numericValue) &&
                    Objects.equals(periodStartValue, concept.periodStartValue) &&
                    Objects.equals(periodEndValue, concept.periodEndValue) &&
                    Objects.equals(quantityValue, concept.quantityValue) &&
                    Objects.equals(quantityUnit, concept.quantityUnit) &&
                    Objects.equals(timeValue, concept.timeValue) &&
                    Objects.equals(uriValue, concept.uriValue) &&
                    Objects.equals(errorCode, concept.errorCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, concepts, textValue, amountCurrency, amountValue, codeValue, dateValue, indicatorValue, measureUnit, measureValue, numericValue, periodStartValue, periodEndValue, quantityValue, quantityUnit, timeValue, uriValue, errorCode);
        }
    }
}
