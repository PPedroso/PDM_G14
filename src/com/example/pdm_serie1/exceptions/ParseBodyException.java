package com.example.pdm_serie1.exceptions;

@SuppressWarnings("serial")
public class ParseBodyException extends MyHttpException {

	public ParseBodyException() {
	}
	
	public ParseBodyException(String message) {
		super(message);
	}
	
	public ParseBodyException(String message, Exception cause) {
		super(message, cause);
	}
}
