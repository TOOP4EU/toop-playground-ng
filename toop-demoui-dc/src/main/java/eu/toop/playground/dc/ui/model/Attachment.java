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
package eu.toop.playground.dc.ui.model;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 7/7/2020. */
public class Attachment {

  private byte[] attachment;
  private String contentID;
  private String mimeType;

  public Attachment(byte[] attachment, String contentID, String mimeType) {
    this.attachment = attachment;
    this.contentID = contentID;
    this.mimeType = mimeType;
  }

  public String getContentID() {
    return contentID;
  }

  public String getMimeType() {
    return mimeType;
  }

  public byte[] getAsByteArray() {
    return attachment;
  }
}
