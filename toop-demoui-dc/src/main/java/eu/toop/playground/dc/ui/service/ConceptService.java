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
