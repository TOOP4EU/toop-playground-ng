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
package eu.toop.playground.dc.ui.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.playground.dc.ui.util.MessageCreation;

/** @author Maria Siapera [mariaspr at unipi.gr] */
public class ConceptService {

  private List<ConceptPojo> allConcepts = new ArrayList<>();

  public List<String> getConceptNames() {
    return Arrays.stream(EToopConcept.values())
        .map(EToopConcept::getID)
        .collect(Collectors.toList());
  }

  public List<ConceptPojo> getConceptPojoParent() {
    allConcepts.add(MessageCreation.createConceptRequestList(getConceptNames()));
    return allConcepts;
  }
}
