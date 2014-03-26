package com.example.pdm_serie1.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.exceptions.ParseBodyException;
import com.example.pdm_serie1.exceptions.UnexpectedStatusCodeException;

public abstract class AbstractHttpExecuter<T> implements IHttpExecuter<T> {

	@Override
	public T executeGet(String url, int expectedStatusCode) throws MyHttpException {
		return executeGet(url, expectedStatusCode);
	}

	@Override
	public T executeGet(String url, int expectedStatusCode, Header... headers) throws MyHttpException {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		BufferedReader reader = null;
		for(Header header : headers) {
			httpGet.addHeader(header);
		}
		try {
			HttpResponse response = client.execute(httpGet);
			int recievedStatusCode = response.getStatusLine().getStatusCode();
			if(recievedStatusCode != expectedStatusCode) {
				throw new UnexpectedStatusCodeException(recievedStatusCode, "Recieved an unexpected status code");
			}
			InputStream inputStream = response.getEntity().getContent();
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            return parseContent(reader);           
		} catch (ClientProtocolException e) {
            throw new MyHttpException("Unexpected Error during http operation", e);
	    } catch (IOException e) {
	    	throw new MyHttpException("Unexpected Error during http operation", e);
	    } finally {
	    	if(reader != null) {
	    		try {
					reader.close();
				} catch (IOException e) {
					throw new MyHttpException("Unexpected Error during http operation", e);
				}
	    	}
	    }
	}
	
	public abstract T parseContent(BufferedReader reader) throws ParseBodyException;
}
