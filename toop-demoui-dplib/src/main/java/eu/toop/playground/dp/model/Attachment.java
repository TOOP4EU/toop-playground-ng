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
package eu.toop.playground.dp.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Attachment {
  private final byte[] attachment;
  private final String attachmentCid;

  public Attachment(File attachedFile, String attachmentCid) throws IOException {
    this.attachment = Files.readAllBytes(attachedFile.toPath());
    this.attachmentCid = attachmentCid;
  }

  public Attachment(byte[] attachment, String attachmentCid) {
    this.attachment = attachment;
    this.attachmentCid = attachmentCid;
  }

  public Attachment(DocumentDataset documentDataset) throws IOException {
    if (documentDataset.getAttachmentLocation() != null) {
      this.attachment = Files.readAllBytes(Paths.get(documentDataset.getAttachmentLocation()));
    } else this.attachment = new byte[0];
    attachmentCid = documentDataset.getAttachmentCid();
  }

  public String getAttachedFileCid() {
    return attachmentCid;
  }

  public byte[] getAttachment() {
    return attachment;
  }
}
