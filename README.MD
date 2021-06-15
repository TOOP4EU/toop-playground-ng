# toop-playground-ng

This project contains the n(ext) g(eneration) of the toop demo ui, using the EDM v2 components. 

# License

All rights to the results that are made available via this repository are owned by their respective creators, as identified in the relevant file names. Unless explicitly indicated otherwise, the results are made available to you under the EUPL, Version 1.2, an EU approved open source licence. For a full version of the licence and guidance, please visit https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12

Note that the results are protected by copyright, and all rights which are not expressly licenced to you by the owners or granted by applicable law are explicitly reserved.

This repository is the only formal source of the results of the TOOP project, an action that was funded by the EU Horizon 2020 research and innovation programme under grant agreement No 737460 (see https://toop.eu/). If you have obtained the results elsewhere or under a different licence, it is likely that this is in violation of copyright law. In case of doubt, please contact us.  

# Prerequisites

- Java Development Kit (JDK) 8+
- Maven

# Workflow

Before doing anything, run "mvn clean install" to compile the entire project.

## DemoUI DC 

To run the application, run "mvn jetty:run" from the toop-demoui-dc module and open http://localhost:8080/

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (nested in the UI class)
- run "mvn clean package"
- test the war file with "mvn jetty:run-war"

Travis will automatically deploy the DC on https://dc-freedonia.dev.exchange.toop.eu

## DemoUI DP

To run the web application, run "mvn exec:java" from the toop-demoui-dpweb module and open http://localhost:9090/

To produce a deployable JAR of the web application, including all of the dependencies, run "mvn clean package".

The toop-demoui-dplib module contains a TOOP Data Provider that uses YAML files as a data source. 
It can take an EDMRequest as an input and produces an EDMResponse or an EDMErrorResponse in the output.
This is used by the Data Provider WebApp. 

Travis will automatically deploy the DP on https://dp-elonia.dev.exchange.toop.eu