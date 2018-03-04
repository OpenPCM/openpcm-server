package org.openpcm.exceptions;

public class NotFoundException extends OpenPCMServiceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7462908417882808906L;

	public NotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public NotFoundException(String message) {
		super(message);
	}
}
