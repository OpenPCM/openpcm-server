package org.openpcm.exceptions;

public abstract class OpenPCMServiceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 5749579434134022672L;

    public OpenPCMServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public OpenPCMServiceException(String message) {
        super(message);
    }

}
