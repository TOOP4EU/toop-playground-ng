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
package eu.toop.playground.dc.ui.model.enums;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 6/7/2020. */
public enum EMimeType {
  /** Response type can be either EDMResponse or EDMErrorResponse */
  RESPONSE_TYPE("application/x-ebrs+xml"),

  DOCUMENT_PDF("application/pdf");

  private String value;

  EMimeType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
