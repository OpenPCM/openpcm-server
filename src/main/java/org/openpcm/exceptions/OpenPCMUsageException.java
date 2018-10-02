package org.openpcm.exceptions;

public abstract class OpenPCMUsageException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -8441921681660261085L;

    public OpenPCMUsageException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public OpenPCMUsageException(String message) {
        super(message);
    }
}
