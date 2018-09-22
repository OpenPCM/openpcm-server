package org.openpcm.exceptions;

public class DataViolationException extends OpenPCMServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718397525117355955L;

	public DataViolationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DataViolationException(String message) {
		super(message);
	}
}
