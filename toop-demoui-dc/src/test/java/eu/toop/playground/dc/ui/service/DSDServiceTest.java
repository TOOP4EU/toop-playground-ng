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

import eu.toop.playground.dc.ui.model.dto.DSDResponseDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 29/5/2020. */
public class DSDServiceTest {

  public final Logger LOGGER = LoggerFactory.getLogger(DSDServiceTest.class.getName());

  private DSDService dsdService = new DSDService();

  @Before
  public void setUp() {}

  @Ignore
  @Test
  public void testGetSimDatasets() throws IOException {
    DSDResponseDto dsdResponseDto = dsdService.getSimDatasets("REGISTERED_ORGANIZATION_TYPE");

    if (!dsdResponseDto.getDatasetDtoList().isEmpty()) {
      System.out.println(dsdResponseDto);
    } else {
      LOGGER.debug("DSDResponseDto dataset arrayList is empty.");
    }
  }

  @Ignore
  @Test
  public void testGetSimDatasetsByCountry() throws IOException {
    DSDResponseDto dsdResponseDto = dsdService.getSimDatasets("REGISTERED_ORGANIZATION_TYPE", "SV");

    if (!dsdResponseDto.getDatasetDtoList().isEmpty()) {
      System.out.println(dsdResponseDto);
    } else {
      LOGGER.debug("DSDResponseDto dataset arrayList is empty.");
    }
  }
}
