package com.example.pdm_serie1.http;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.pdm_serie1.exceptions.ParseBodyException;

public class JsonObjectHttpExecuter extends AbstractHttpExecuter<JSONObject>{

	@Override
	public JSONObject parseContent(BufferedReader reader) throws ParseBodyException {
		try {
			StringBuilder builder = new StringBuilder();
			for(String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			return new JSONObject(builder.toString());
		} catch(IOException e) {
			throw new ParseBodyException("Unexpected error while reading body contetn", e);
		} catch(JSONException e) {
			throw new ParseBodyException("Unexpected error while parsing body content to JSON object", e);			
		}
		
	}

}
