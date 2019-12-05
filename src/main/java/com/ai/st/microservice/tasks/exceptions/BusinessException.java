package com.ai.st.microservice.tasks.exceptions;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 4026596642770023028L;

	private String messageError;

	public BusinessException(String message) {
		super();
		this.messageError = message;
	}

	public String getMessageError() {
		return messageError;
	}
	
	@Override
	public String getMessage() {
		return this.getMessageError();
	}

}
