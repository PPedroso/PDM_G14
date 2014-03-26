package com.example.pdm_serie1.exceptions;

@SuppressWarnings("serial")
public class MyHttpException extends Exception {

	public MyHttpException() {
	}
	
	public MyHttpException(String message) {
		super(message);
	}
	
	public MyHttpException(String message, Exception cause) {
		super(message, cause);
	}
	
}
