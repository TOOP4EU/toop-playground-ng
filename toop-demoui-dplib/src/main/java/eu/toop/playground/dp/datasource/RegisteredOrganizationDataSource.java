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
package eu.toop.playground.dp.datasource;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.playground.dp.model.GBMDataset;

public class RegisteredOrganizationDataSource extends YAMLDataSource<GBMDataset> {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(RegisteredOrganizationDataSource.class);

  public RegisteredOrganizationDataSource() throws IOException {
    this("datasets/gbm");
  }

  public RegisteredOrganizationDataSource(String location) {
    super(location, new GBMDataset());
  }
  }