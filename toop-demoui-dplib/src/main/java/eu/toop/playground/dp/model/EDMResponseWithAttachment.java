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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import eu.toop.edm.EDMResponse;

public class EDMResponseWithAttachment {
    private final EDMResponse edmResponse;
    private final List<Attachment> attachmentList = new ArrayList<>();

    public EDMResponseWithAttachment(EDMResponse edmResponse, Attachment attachment) {
        this.edmResponse = edmResponse;
        this.attachmentList.add(attachment);
    }

    public EDMResponseWithAttachment(EDMResponse edmResponse, List<Attachment> attachmentList) {
        this.edmResponse = edmResponse;
        this.attachmentList.addAll(attachmentList);
    }

    public EDMResponseWithAttachment(EDMResponse edmResponse) {
        this.edmResponse = edmResponse;
    }

    public EDMResponse getEdmResponse() {
        return edmResponse;
    }

    public Optional<Attachment> getAttachment() {
        return attachmentList.stream().findFirst();
    }

    public List<Attachment> getAllAttachments(){
        return attachmentList;
    }

}
