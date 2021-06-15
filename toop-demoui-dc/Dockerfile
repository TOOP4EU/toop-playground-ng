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

FROM tomcat:9-jdk11

ARG VERSION="2.1.1"
ARG ARTIFACT=toop-demoui-dc-${VERSION}.war

#create tc webapp folder
WORKDIR $CATALINA_HOME/webapps

ENV CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true -Djava.security.egd=file:/dev/urandom" \
    TOOP_COMPONENT=$ARTIFACT

#COPY target/${ARTIFACT} ./
RUN echo "Download ${ARTIFACT}" && curl https://oss.sonatype.org/service/local/repositories/releases/content/eu/toop/toop-demoui-dc/${VERSION}/${ARTIFACT} -o ${ARTIFACT}

RUN rm -fr manager host-manager ROOT && \
    unzip ${ARTIFACT} -d ROOT && \
    echo "Extracted ${ARTIFACT}, now removing the war" && \
    rm -fr ${ARTIFACT}
