#!/usr/bin/env bash
#
# This work is protected under copyrights held by the members of the
# TOOP Project Consortium as indicated at
# http://wiki.ds.unipi.gr/display/TOOP/Contributors
# (c) 2018-2021. All rights reserved.
#
# This work is licensed under the EUPL 1.2.
#
#  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
#
# Licensed under the EUPL, Version 1.2 or – as soon they will be approved
# by the European Commission - subsequent versions of the EUPL
# (the "Licence");
# You may not use this work except in compliance with the Licence.
# You may obtain a copy of the Licence at:
#
#         https://joinup.ec.europa.eu/software/page/eupl
#



# This bash script runs maven build for vaadin production
# and then creates a dockerfile with the pom version.

projectVersion='2.1.1'
artifact=toop-demoui-dc

echo Building toop/${artifact}:${projectVersion}

#mvn clean package -Pproduction

docker build --build-arg VERSION=${projectVersion} -t toop/${artifact}:${projectVersion} .
docker build --build-arg VERSION=${projectVersion} -t toop/${artifact}:latest .