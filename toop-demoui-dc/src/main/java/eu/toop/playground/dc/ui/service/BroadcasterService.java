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
package eu.toop.playground.dc.ui.service;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.shared.Registration;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMResponse;
import eu.toop.playground.dc.ui.model.EDMResponseWithAttachment;

/** @author Konstantinos Raptis [kraptis at unipi.gr] on 18/5/2020. */
public enum BroadcasterService {
  INSTANCE();

  private final Logger LOGGER = LoggerFactory.getLogger(BroadcasterService.class);

  private Executor executor = Executors.newSingleThreadExecutor();
  private final LinkedList<Consumer<EDMResponse>> responseListeners = new LinkedList<>();
  private final LinkedList<Consumer<EDMErrorResponse>> errorResponseListeners = new LinkedList<>();
  private final LinkedList<Consumer<EDMResponseWithAttachment>> responseWithAttachmentListeners =
      new LinkedList<>();

  BroadcasterService() {}

  public synchronized Registration registerResponse(Consumer<EDMResponse> listener) {
    LOGGER.debug("Register {}", listener.toString());
    responseListeners.add(listener);

    return () -> {
      synchronized (BroadcasterService.class) {
        LOGGER.debug("Removing {}", listener.toString());
        responseListeners.remove(listener);
      }
    };
  }

  public synchronized Registration registerErrorResponse(Consumer<EDMErrorResponse> listener) {
    LOGGER.debug("Register {}", listener.toString());
    errorResponseListeners.add(listener);

    return () -> {
      synchronized (BroadcasterService.class) {
        LOGGER.debug("Removing {}", listener.toString());
        errorResponseListeners.remove(listener);
      }
    };
  }

  public synchronized Registration registerResponseWithAttachment(
      Consumer<EDMResponseWithAttachment> listener) {
    LOGGER.debug("Register {}", listener.toString());
    responseWithAttachmentListeners.add(listener);

    return () -> {
      synchronized (BroadcasterService.class) {
        LOGGER.debug("Removing {}", listener.toString());
        responseWithAttachmentListeners.remove(listener);
      }
    };
  }

  public synchronized void broadcast(EDMResponse response) {
    LOGGER.debug("Broadcasting {}", response.getRequestID());

    for (Consumer<EDMResponse> listener : responseListeners) {
      LOGGER.debug("Broadcasting to {}", listener);
      executor.execute(() -> listener.accept(response));
    }
  }

  public synchronized void broadcast(EDMErrorResponse response) {
    LOGGER.debug("Broadcasting {}", response.getRequestID());

    for (Consumer<EDMErrorResponse> listener : errorResponseListeners) {
      LOGGER.debug("Broadcasting to {}", listener);
      executor.execute(() -> listener.accept(response));
    }
  }

  public synchronized void broadcast(EDMResponseWithAttachment responseWithAttachment) {
    LOGGER.debug("Broadcasting {}", responseWithAttachment.getEdmResponse().getRequestID());

    for (Consumer<EDMResponseWithAttachment> listener : responseWithAttachmentListeners) {
      LOGGER.debug("Broadcasting to {}", listener);
      executor.execute(() -> listener.accept(responseWithAttachment));
    }
  }
}
