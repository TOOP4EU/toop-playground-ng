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
package eu.toop.playground.dc.config.enums;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 18/5/2020. */
public enum DCConfig {
  INSTANCE;

  private final String CONFIG_FILE = "dcui.conf";
  private final Config config;

  DCConfig() {
    Config config = ConfigFactory.parseResources(CONFIG_FILE);
    Config toopDirConfig =
        ConfigFactory.parseFile(Paths.get("/toop-dir/demo-ui-dc/" + CONFIG_FILE).toFile());
    Path f = Paths.get("./" + CONFIG_FILE);
    this.config =
        ConfigFactory.parseFile(f.toFile())
            .withFallback(toopDirConfig)
            .withFallback(config)
            .resolve();
  }

  public boolean useMockDSD() {
    return config.getBoolean("use-mock-dsd");
  }

  public boolean useDirectSubmit() {
    return config.getBoolean("use-direct-submit");
  }

  public boolean isDumpingEnabled() {
    return config.getBoolean("dump-messages");
  }

  public String getDumpLocation(){
    return config.getString("dump-location");
  }
  public String getKafkaClientTopic() {
    return getKafkaClientConfig().getString("topic");
  }

  public boolean isKafkaClientEnabled() {
    return getKafkaClientConfig().getBoolean("enabled");
  }

  public String getKafkaClientURL() {
    return getKafkaClientConfig().getString("url");
  }

  public String getSubmitRequestURL() {
    return useDirectSubmit()
        ? getEloniaDP().getString("submit-request-url")
        : getSimulatorDP().getString("submit-request-url");
  }

  public String getDSDEndpointURL() {
    return getSimulatorDP().getString("dsd-url");
  }

  public String getDefaultEloniaDevParticipantIdScheme() {
    return getEloniaDP().getString("participant-id-scheme");
  }

  public String getDefaultEloniaDevParticipantIdValue() {
    return getEloniaDP().getString("participant-id-value");
  }

  public String getDefaultFreedoniaDCSenderIdScheme() {
    return getFreedoniaDC().getString("sender-id-scheme");
  }

  public String getDefaultFreedoniaDCSenderIdValue() {
    return getFreedoniaDC().getString("sender-id-value");
  }

  private Config getKafkaClientConfig() {
    return config.getConfig("kafka-client");
  }

  private Config getFreedoniaDC() {
    return config.getConfig("dc").getConfig("freedonia");
  }

  private Config getEloniaDP() {
    return config.getConfig("dp").getConfig("elonia");
  }

  private Config getSimulatorDP() {
    return config.getConfig("dp").getConfig("simulator");
  }
}
