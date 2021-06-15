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
package eu.toop.playground.dp.model;

public class Dataset {

  protected String naturalPersonID;
  protected String legalPersonID;
  protected String legalRepresentativeID;

  public String getNaturalPersonID() {
    return naturalPersonID;
  }

  public void setNaturalPersonID(String naturalPersonID) {
    this.naturalPersonID = naturalPersonID;
  }

  public String getLegalPersonID() {
    return legalPersonID;
  }

  public void setLegalPersonID(String legalPersonID) {
    this.legalPersonID = legalPersonID;
  }

  public String getLegalRepresentativeID() {
    return legalRepresentativeID;
  }

  public void setLegalRepresentativeID(String legalRepresentativeID) {
    this.legalRepresentativeID = legalRepresentativeID;
  }
}
