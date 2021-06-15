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
