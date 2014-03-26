package com.example.pdm_serie1.exceptions;

@SuppressWarnings("serial")
public class UnexpectedStatusCodeException extends MyHttpException {

	private final int recievedStatusCode;
	
	public UnexpectedStatusCodeException(int recievedStatusCode) {
		this.recievedStatusCode = recievedStatusCode;
	}
	
	public UnexpectedStatusCodeException(int recievedStatusCode, String message) {
		super(message);
		this.recievedStatusCode = recievedStatusCode;
	}
	
	public UnexpectedStatusCodeException(int recievedStatusCode, String message, Exception cause) {
		super(message, cause);
		this.recievedStatusCode = recievedStatusCode;
	}
	
	public int getRecievedStatusCode() {
		return recievedStatusCode;
	}
	
}
