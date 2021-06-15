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
package eu.toop.playground.dc.ui.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 1/6/2020. */
@JsonPropertyOrder({"scheme", "value"})
public class DSDIDTypeDto {

  private String scheme;
  private String value;

  @JsonCreator
  public DSDIDTypeDto(@JsonProperty("scheme") String scheme, @JsonProperty("value") String value) {
    this.scheme = scheme;
    this.value = value;
  }

  public String getScheme() {
    return scheme;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "DSDIDTypeDto{" + "scheme='" + scheme + '\'' + ", value='" + value + '\'' + '}';
  }
}
