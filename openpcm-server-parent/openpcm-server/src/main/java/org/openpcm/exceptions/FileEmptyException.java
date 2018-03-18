package org.openpcm.exceptions;

public class FileEmptyException extends OpenPCMServiceException{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5323501363200852155L;

	public FileEmptyException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public FileEmptyException(String message) {
		super(message);
	}
}
