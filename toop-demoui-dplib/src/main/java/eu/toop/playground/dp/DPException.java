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
package eu.toop.playground.dp;

import eu.toop.edm.EDMErrorResponse;

public class DPException extends Exception{

    private EDMErrorResponse edmErrorResponse;

    public DPException() {
    }

    public DPException(EDMErrorResponse e){
        this.edmErrorResponse = e;
    }

    public DPException(EDMErrorResponse e, String message){
        this(message);
        this.edmErrorResponse = e;
    }

    public DPException(String message) {
        super(message);
    }

    public DPException(String message, Throwable cause) {
        super(message, cause);
    }

    public DPException(Throwable cause) {
        super(cause);
    }

    public DPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EDMErrorResponse getEdmErrorResponse() {
        return edmErrorResponse;
    }
}
