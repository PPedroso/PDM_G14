package com.example.pdm_serie1.asynchandlers;

public interface IAsyncTaskResult<T> {
	
	T getResult();
	
	Exception getError();
		
}
