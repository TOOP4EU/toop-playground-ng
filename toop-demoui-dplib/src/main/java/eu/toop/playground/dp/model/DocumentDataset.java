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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.DocumentReferencePojo;
import eu.toop.edm.model.QualifiedRelationPojo;
import eu.toop.edm.model.RepositoryItemRefPojo;

public class DocumentDataset extends Dataset {

  private String description;
  private String title;
  private DocumentReference distribution;
  private List<String> IDs = new ArrayList<>();
  private LocalDateTime issued;
  private String language;
  private LocalDateTime lastModified;
  private LocalDate validFrom;
  private LocalDate validTo;
  private Creator creator;
  private List<QualifiedRelation> qualifiedRelation = new ArrayList<>();
  private RepositoryItemRef repositoryItemRef;
  private String attachmentLocation;
  private String attachmentCid;

  public String getAttachmentCid() {
    return attachmentCid;
  }

  public void setAttachmentCid(String attachmentCid) {
    this.attachmentCid = attachmentCid;
  }

  public String getAttachmentLocation() {
    return attachmentLocation;
  }

  public void setAttachmentLocation(String attachmentLocation) {
    this.attachmentLocation = attachmentLocation;
  }

  public static class RepositoryItemRef {
    private String link;
    private String title;

    public String getLink() {
      return link;
    }

    public void setLink(String link) {
      this.link = link;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public RepositoryItemRefPojo getAsRepositoryItemRefPojo() {
      return RepositoryItemRefPojo.builder().link(link).title(title).build();
    }

    @Override
    public String toString() {
      return "RepositoryItemRef{" + "link='" + link + "'\n" + ", title='" + title + "'\n" + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      RepositoryItemRef that = (RepositoryItemRef) o;
      return Objects.equals(link, that.link) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
      return Objects.hash(link, title);
    }
  }

  public static class DocumentReference {
    private String documentURI;
    private String documentDescription;
    private String documentType;

    public String getDocumentURI() {
      return documentURI;
    }

    public void setDocumentURI(String documentURI) {
      this.documentURI = documentURI;
    }

    public String getDocumentDescription() {
      return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
      this.documentDescription = documentDescription;
    }

    public String getDocumentType() {
      return documentType;
    }

    public void setDocumentType(String documentType) {
      this.documentType = documentType;
    }

    @Override
    public String toString() {
      return "DocumentReference{"
          + "documentURI='"
          + documentURI
          + "'\n"
          + ", documentDescription='"
          + documentDescription
          + "'\n"
          + ", documentType='"
          + documentType
          + "'\n"
          + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DocumentReference that = (DocumentReference) o;
      return Objects.equals(documentURI, that.documentURI)
          && Objects.equals(documentDescription, that.documentDescription)
          && Objects.equals(documentType, that.documentType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(documentURI, documentDescription, documentType);
    }
  }

  public static class QualifiedRelation {
    private String description;
    private String title;
    private String id;

    public QualifiedRelation() {}

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getID() {
      return id;
    }

    public void setID(String id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "QualifiedRelation{"
          + "description='"
          + description
          + "'\n"
          + ", title='"
          + title
          + "'\n"
          + ", id='"
          + id
          + "'\n"
          + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      QualifiedRelation that = (QualifiedRelation) o;
      return Objects.equals(description, that.description)
          && Objects.equals(title, that.title)
          && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(description, title, id);
    }
  }

  public static class Creator {
    private String id;
    private String name;
    private String postName;
    private String idScheme;

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getPostName() {
      return postName;
    }

    public String getIdScheme() {
      return idScheme;
    }

    public void setId(String id) {
      this.id = id;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setPostName(String postName) {
      this.postName = postName;
    }

    public void setIdScheme(String idScheme) {
      this.idScheme = idScheme;
    }

    @Override
    public String toString() {
      return "Creator{"
          + "id='"
          + id
          + "'\n"
          + ", name='"
          + name
          + "'\n"
          + ", postName='"
          + postName
          + "'\n"
          + ", idScheme='"
          + idScheme
          + "'\n"
          + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Creator creator = (Creator) o;
      return Objects.equals(id, creator.id)
          && Objects.equals(name, creator.name)
          && Objects.equals(postName, creator.postName)
          && Objects.equals(idScheme, creator.idScheme);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, name, postName, idScheme);
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public DocumentReference getDistribution() {
    return distribution;
  }

  public void setDistribution(DocumentReference distribution) {
    this.distribution = distribution;
  }

  public List<String> getIDs() {
    return IDs;
  }

  public void setIDs(List<String> IDs) {
    this.IDs = IDs;
  }

  public LocalDateTime getIssued() {
    return issued;
  }

  public void setIssued(LocalDateTime issued) {
    this.issued = issued;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }

  public LocalDate getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(LocalDate validFrom) {
    this.validFrom = validFrom;
  }

  public LocalDate getValidTo() {
    return validTo;
  }

  public void setValidTo(LocalDate validTo) {
    this.validTo = validTo;
  }

  public Creator getCreator() {
    return creator;
  }

  public void setCreator(Creator creator) {
    this.creator = creator;
  }

  public List<QualifiedRelation> getQualifiedRelation() {
    return qualifiedRelation;
  }

  public void setQualifiedRelation(List<QualifiedRelation> qualifiedRelation) {
    this.qualifiedRelation = qualifiedRelation;
  }

  public RepositoryItemRef getRepositoryItemRef() {
    return repositoryItemRef;
  }

  public void setRepositoryItemRef(RepositoryItemRef repositoryItemRef) {
    this.repositoryItemRef = repositoryItemRef;
  }

  @Override
  public String toString() {
    return "DocumentDataset{"
        + "naturalPersonID='"
        + naturalPersonID
        + "'\n"
        + ", legalPersonID='"
        + legalPersonID
        + "'\n"
        + ", legalRepresentativeID='"
        + legalRepresentativeID
        + "'\n"
        + ", description='"
        + description
        + "'\n"
        + ", title='"
        + title
        + "'\n"
        + ", distribution="
        + distribution
        + ", IDs="
        + IDs
        + ", issued="
        + issued
        + ", language='"
        + language
        + "'\n"
        + ", lastModified="
        + lastModified
        + ", validFrom="
        + validFrom
        + ", validTo="
        + validTo
        + ", creator="
        + creator
        + ", qualifiedRelation="
        + qualifiedRelation
        + ", repositoryItemRef="
        + repositoryItemRef
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DocumentDataset that = (DocumentDataset) o;
    return Objects.equals(naturalPersonID, that.naturalPersonID)
        && Objects.equals(legalPersonID, that.legalPersonID)
        && Objects.equals(legalRepresentativeID, that.legalRepresentativeID)
        && Objects.equals(description, that.description)
        && Objects.equals(title, that.title)
        && Objects.equals(distribution, that.distribution)
        && Objects.equals(IDs, that.IDs)
        && Objects.equals(issued, that.issued)
        && Objects.equals(language, that.language)
        && Objects.equals(lastModified, that.lastModified)
        && Objects.equals(validFrom, that.validFrom)
        && Objects.equals(validTo, that.validTo)
        && Objects.equals(creator, that.creator)
        && Objects.equals(qualifiedRelation, that.qualifiedRelation)
        && Objects.equals(repositoryItemRef, that.repositoryItemRef);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        naturalPersonID,
        legalPersonID,
        legalRepresentativeID,
        description,
        title,
        distribution,
        IDs,
        issued,
        language,
        lastModified,
        validFrom,
        validTo,
        creator,
        qualifiedRelation,
        repositoryItemRef);
  }

  public DatasetPojo getAsDatasetPojo() {
    DatasetPojo.Builder datasetPojoBuilder =
        DatasetPojo.builder()
            .addDescription(description)
            .ids(IDs)
            .issued(issued)
            .addTitle(title)
            .language(language)
            .lastModified(lastModified)
            .validFrom(validFrom)
            .validTo(validTo);
    if (distribution != null) {
      datasetPojoBuilder.distribution(
          DocumentReferencePojo.builder()
              .addDocumentDescription(distribution.documentDescription)
              .documentType(distribution.documentType)
              .documentURI(distribution.documentURI)
              .build());
    }
    if (qualifiedRelation != null) {
      datasetPojoBuilder.qualifiedRelations(
          qualifiedRelation.stream()
              .map(
                  qr ->
                      QualifiedRelationPojo.builder()
                          .description(qr.description)
                          .id(qr.id)
                          .title(qr.title)
                          .build())
              .collect(Collectors.toList()));
    }
    if (creator != null) {
      datasetPojoBuilder.creator(
          AgentPojo.builder()
              .address(AddressPojo.builder().streetName(creator.postName))
              .id(creator.id)
              .name(creator.name)
              .idSchemeID(creator.idScheme)
              .build());
    }
    return datasetPojoBuilder.build();
  }
}
