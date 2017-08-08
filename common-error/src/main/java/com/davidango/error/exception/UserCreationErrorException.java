package com.davidango.error.exception;

public class UserCreationErrorException extends Exception {

	private static final long serialVersionUID = 1359397879136182110L;
		
		@SuppressWarnings("unused")
		private String errorMessage;
				
		public UserCreationErrorException(String errorMessage) {
			super(errorMessage);
			this.errorMessage = errorMessage;
		}
	}
