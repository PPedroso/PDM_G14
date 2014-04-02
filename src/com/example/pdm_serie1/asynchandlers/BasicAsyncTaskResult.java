package com.example.pdm_serie1.asynchandlers;

public class BasicAsyncTaskResult<T> implements IAsyncTaskResult<T> {
	
	private T result;
	private Exception error;

	public T getResult() {
	    return result;
	}
	public Exception getError() {
	    return error;
	}

	public BasicAsyncTaskResult(T result) {
	    this.result = result;
	}

	public BasicAsyncTaskResult(Exception error) {
	    this.error = error;
	}
}
