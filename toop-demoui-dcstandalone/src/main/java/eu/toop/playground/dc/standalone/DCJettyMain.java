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
package eu.toop.playground.dc.standalone;

import com.vaadin.flow.server.startup.ServletContextListeners;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.*;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(description = "Freedonia DC standalone jar", name = "freedonia-dc-jar", mixinStandardHelpOptions = true, separator = " ", version = {
        "DemoUI-DC-NG Standalone v2.0.0",
        "Picocli " + picocli.CommandLine.VERSION,
        "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
        "OS: ${os.name} ${os.version} ${os.arch}"})

public class DCJettyMain implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DCJettyMain.class);
    private static final int DEFAULT_PORT = 8080;

    @Option(names = {"-p", "--port"},
            paramLabel = "Port",
            defaultValue = "" + DEFAULT_PORT,
            description = "Port to run the Freedonia DC on (default: ${DEFAULT-VALUE})")
    private Integer startPort;

    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new DCJettyMain());
        final int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        try {
            if (Files.notExists(Paths.get("./dcui.conf"))) {
                LOGGER.info("dcui.conf not found, copying default to {}", Paths.get("./dcui.conf").getFileName());
                try (InputStream dcuiConfResource = getClass().getResourceAsStream("/dcui.conf")) {
                    byte[] buffer = new byte[dcuiConfResource.available()];
                    dcuiConfResource.read(buffer);
                    File dcuiConfFile = new File("./dcui.conf");
                    try (FileOutputStream fos = new FileOutputStream(dcuiConfFile)) {
                        fos.write(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("Starting server on http://localhost:{}/", startPort);
            URL webRootLocation = this.getClass().getResource("/META-INF/resources/");
            URI webRootUri = webRootLocation.toURI();

            WebAppContext context = new WebAppContext();
            context.setBaseResource(Resource.newResource(webRootUri));
            context.setContextPath("/");
            context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
            context.setConfigurationDiscovered(true);
            context.setConfigurations(new Configuration[]{
                    new AnnotationConfiguration(),
                    new WebInfConfiguration(),
                    new WebXmlConfiguration(),
                    new MetaInfConfiguration(),
                    new FragmentConfiguration(),
                    new EnvConfiguration(),
                    new PlusConfiguration(),
                    new JettyWebXmlConfiguration()
            });
            context.getServletContext().setExtendedListenerTypes(true);
            context.addEventListener(new ServletContextListeners());

            Server server = new Server(startPort);
            server.setHandler(context);

            server.start();
            server.join();
        } catch (final Exception ex) {
            LOGGER.error("Error running DemoUI-DC-NG Standalone", ex);
        }
        return 0;
    }
}
