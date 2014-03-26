package com.example.pdm_serie1.http;

import org.apache.http.Header;

import com.example.pdm_serie1.exceptions.MyHttpException;

public interface IHttpExecuter<T> {

	T executeGet(String url, int expectedStatusCode) throws MyHttpException;
	T executeGet(String url, int expectedStatusCode, Header...headers) throws MyHttpException;
	
}
