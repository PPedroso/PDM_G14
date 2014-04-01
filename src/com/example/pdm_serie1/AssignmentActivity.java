package com.example.pdm_serie1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class AssignmentActivity extends Activity {
	
	AsyncTask<String, Integer, LinkedList<String>> a;
	final Activity act = this;
	
	private String THOTH_API = "http://thoth.cc.e.ipl.pt/api/v1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment);
		Intent intent = getIntent();
		String classId = intent.getStringExtra("classId");
		getAssignmentInfo(classId);
	}

	private void getAssignmentInfo(final String classId){
		a = new AsyncTask<String, Integer, LinkedList<String>>(){
			@Override
			protected LinkedList<String> doInBackground(String ... params){
				try {
					
					URL url = new URL(THOTH_API+"/classes/" + classId + "/workitems");
					InputStream is = url.openStream();
											
					BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				    
					StringBuilder sb = new StringBuilder();
				    int cp;
				    while ((cp = rd.read()) != -1) {
				      sb.append((char) cp);
				    }
				    
				    is.close();
				    rd.close();
					
				    JSONArray a = new JSONObject(sb.toString()).getJSONArray("workItems");
					
					
					LinkedList<String> resultList = new LinkedList<String>();
					
					for(int i=0;i<a.length();++i){
						resultList.add(((JSONObject)a.get(i)).get("title").toString());
					}
					
					return resultList;
				    
				} catch (MalformedURLException e) {
					Log.i("URL",e.getMessage());
				} catch (IOException e) {
					Log.i("IO", e.getMessage());
				} catch (JSONException e) {
					Log.i("JSON",e.getMessage());
				} catch(Exception e){
					Log.i("General Exception",e.getMessage());
				}
				return null;
			}
						
			@Override
			protected void onPostExecute(LinkedList<String> result){								
				ListView lv = (ListView)findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_multiple_choice,result.toArray(new String[] {}));
				lv.setAdapter(adapter);
			}
		}.execute();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignment, menu);
		return true;
	}

}
