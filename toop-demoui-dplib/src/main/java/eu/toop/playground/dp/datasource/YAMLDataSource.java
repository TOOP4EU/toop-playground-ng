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
package eu.toop.playground.dp.datasource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import eu.toop.playground.dp.model.Dataset;

public abstract class YAMLDataSource<X extends Dataset> implements Datasource<X> {
    private static final Logger LOGGER = LoggerFactory.getLogger(YAMLDataSource.class);
    protected final Map<Path, X> datasets;
    protected X targetClass;

    public YAMLDataSource(String location, X targetClass) {
        this.targetClass = targetClass;
        ObjectMapper om = new ObjectMapper(new YAMLFactory()).registerModules(new JavaTimeModule()).findAndRegisterModules();
        LOGGER.info("Initializing datasource for {}s with location {} ", targetClass.getClass().getSimpleName(), location);
        datasets = new HashMap<>();
        Path datasetsPath = Paths.get(location);
        if(Files.exists(datasetsPath)){
            try (Stream<Path> paths = Files.walk(datasetsPath)) {
                paths.filter(Files::isRegularFile)
                        .filter(f -> f.toFile().getName().endsWith(".yaml"))
                        .forEach(f -> {
                            try {
                                LOGGER.info("Adding {} from {} ", targetClass.getClass().getSimpleName(), f.toFile().getAbsolutePath());

                                JavaType type = om.getTypeFactory().
                                        constructType(targetClass.getClass());
                                datasets.put(f, om.readValue(f.toFile(), type));
                            } catch (IOException e) {
                                LOGGER.warn("Failed to read {}", f.toFile().getAbsolutePath());
                                LOGGER.warn(e.getMessage());
                            }
                        });
            } catch (IOException e) {
                LOGGER.error("Failed to read {}", location);
            }
        } else {
            LOGGER.error("Path does not exist {}", location);
        }

        if(datasets.size()==0)
            LOGGER.warn("WARNING: No {}s have been loaded", targetClass.getClass().getSimpleName());
    }

    @Override
    public Map<Path, X> getDatasets() {
        return datasets;
    }
}
