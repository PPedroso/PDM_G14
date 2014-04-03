package com.example.pdm_serie1.asynctaskrelated;

public interface IAsyncTaskResult<T> {
	
	T getResult();
	
	Exception getError();
		
}
