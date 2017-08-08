package com.davidango.error.exception;

public class LookupErrorException extends Exception {

	private static final long serialVersionUID = 3280027801312897043L;
	
	@SuppressWarnings("unused")
	private String errorMessage;
	
	
	public LookupErrorException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
