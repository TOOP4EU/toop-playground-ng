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
package eu.toop.playground.dc.util;

import eu.toop.playground.dc.config.enums.DCConfig;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageDumper {
  private static final Path dumpIncomingLocation =
      Paths.get(DCConfig.INSTANCE.getDumpLocation() + "/incoming");
  private static final Path dumpOutgoingLocation =
      Paths.get(DCConfig.INSTANCE.getDumpLocation() + "/outgoing");

  public static void dumpMessage(String message, MessageType type) {
    dumpMessage(message.getBytes(), type);
  }

  private MessageDumper(){

  }

  public static void dumpMessage(byte[] bytes, MessageType type) {
    if (DCConfig.INSTANCE.isDumpingEnabled()) {
      try {
        switch (type) {
          case INCOMING:
            if (!Files.isDirectory(dumpIncomingLocation))
              Files.createDirectories(dumpIncomingLocation);
            Files.write(
                Paths.get(
                    dumpIncomingLocation.toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".txt"),
                bytes);
            break;
          case OUTGOING:
            if (!Files.isDirectory(dumpOutgoingLocation))
              Files.createDirectories(dumpOutgoingLocation);
            Files.write(
                Paths.get(
                    dumpOutgoingLocation.toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".txt"),
                bytes);
            break;
        }
      } catch (IOException e) {
        LogManager.getLogger().warn("Unable to dump message.");
        LogManager.getLogger().warn(e);
      }
    }
  }
}
