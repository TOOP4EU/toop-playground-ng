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
package eu.toop.playground;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.EDMRequest;
import eu.toop.playground.dp.datasource.Datasource;
import eu.toop.playground.dp.datasource.GenericDocumentDataSource;
import eu.toop.playground.dp.datasource.RegisteredOrganizationDataSource;
import eu.toop.playground.dp.model.DocumentDataset;
import eu.toop.playground.dp.model.GBMDataset;
import eu.toop.playground.dp.service.ToopDP;

public class YAMLDataSourceTest {

    @Before
    public void setUp() throws Exception {
        try{
        Files.walk(Paths.get("./datasets"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }catch (NoSuchFileException e){
            // No dataset directory to cleanup, ignore
        }

        new ToopDP();
    }

    @Test
    public void getResponseFromRequest() throws IOException {
        Datasource<GBMDataset> dataSource = new RegisteredOrganizationDataSource();
        EDMRequest request = EDMRequest.reader().read(ClassPathResource.getInputStream("Concept Request_NP.xml"));
        List<GBMDataset> x = dataSource.findResponse(request);
        assertTrue(x.size()>0);
    }

    @Test
    public void getConceptPojoFromDataset() throws IOException {
        Datasource<GBMDataset> dataSource = new RegisteredOrganizationDataSource();
        EDMRequest request = EDMRequest.reader().read(ClassPathResource.getInputStream("Concept Request_NP.xml"));
        List<GBMDataset> x = dataSource.findResponse(request);
        assertTrue(x.size()>0);
        assertNotNull(x.get(0).getConceptPojoList());
    }

    @Test
    public void getAllGBMDatasets() throws IOException {
        Datasource<GBMDataset> dataSource = new RegisteredOrganizationDataSource();
        assertNotNull(dataSource.getDatasets());
    }

    @Test
    public void getAllDocumentDatasets() throws IOException {
        Datasource<DocumentDataset> dataSource = new GenericDocumentDataSource();
        assertNotNull(dataSource.getDatasets());
    }

    @After
    public void tearDown() throws Exception {
        Files.walk(Paths.get("./datasets"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
