#!/bin/bash
#
# Copyright 2021 - TOOP Project
#
# This file and its contents are licensed under the EUPL, Version 1.2
# or – as soon they will be approved by the European Commission – subsequent
# versions of the EUPL (the "Licence");
#
# You may not use this work except in compliance with the Licence.
# You may obtain a copy of the Licence at:
#
#       https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
#
# Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the Licence for the specific language governing permissions and limitations under the Licence.
#


mv ${TRAVIS_BUILD_DIR}/toop-demoui-dc/target/toop-demoui-dc*.war ${TRAVIS_BUILD_DIR}/toop-demoui-dc/target/toop-demoui-dc.war
mv ${TRAVIS_BUILD_DIR}/toop-demoui-dpweb/target/toop-demoui-dpweb*jar-with-dependencies.jar ${TRAVIS_BUILD_DIR}/toop-demoui-dpweb/target/toop-demoui-dp.jar
scp -P ${PORT} ${TRAVIS_BUILD_DIR}/toop-demoui-dc/target/toop-demoui-dc.war ${DEPLOY_USER}@${DEPLOY_HOST_FREEDONIA}:/opt/tomcat/webapps/ROOT.war
scp -P ${PORT} ${TRAVIS_BUILD_DIR}/toop-demoui-dpweb/target/toop-demoui-dp.jar ${DEPLOY_USER}@${DEPLOY_HOST_ELONIA}:/toop-dir/demo-ui-dp/toop-demoui-dp.jar

ssh -p ${PORT} -tt ${DEPLOY_USER}@${DEPLOY_HOST_ELONIA} << EOF
  cd /toop-dir/demo-ui-dp/
  ./stop-demoui-dp.sh && ./start-demoui-dp.sh
  exit
EOF