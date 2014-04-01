package com.example.pdm_serie1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SemesterClassesActivity extends Activity {

	private String THOTH_API = "http://thoth.cc.e.ipl.pt/api/v1";
	String currentSemester = "";
	boolean semesterIsSet = false;
	LinkedList<String> currentClasses;
	
	AsyncTask<String, Integer, LinkedList<String>> a;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesterclasses);
		
		Resources res = getResources();
		
		currentSemester = PreferenceManager.getDefaultSharedPreferences(this).getString("currentSemester", res.getString(R.string.noSemester));
		
		semesterIsSet = (currentSemester != res.getString(R.string.noSemester));
		TextView semView = (TextView)findViewById(R.id.SemesterClassesActivity_CurrSemesterView);
		semView.setText(currentSemester);
		
		getClasses();
				
	}
	
	/**
	 * Creates a background task that gets all classes for the set semester. 
	 * Will not do anything if semester is not set.
	 */
	public void getClasses(){
		final Activity act = this;
		
		if(!semesterIsSet) return;
		
		a = new AsyncTask<String, Integer, LinkedList<String>>(){
			@Override
			protected LinkedList<String> doInBackground(String ... params){
				try {
					InputStream is = new URL(THOTH_API+"/classes").openStream();
											
					BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				    
					StringBuilder sb = new StringBuilder();
				    int cp;
				    while ((cp = rd.read()) != -1) {
				      sb.append((char) cp);
				    }
				    is.close();
				    rd.close();
				    
				    LinkedList<String> classesList= new LinkedList<String>();
					
					JSONArray jArray = new JSONObject(sb.toString()).getJSONArray("classes");
					
					for(int i=0; i < jArray.length(); ++i){
						
						String lectiveSemester = (String)((JSONObject)jArray.get(i)).get("lectiveSemesterShortName");
						
						if(lectiveSemester.compareTo(currentSemester) == 0){
							String fullName = (String)((JSONObject)jArray.get(i)).get("fullName");
							String id =(((JSONObject)jArray.get(i)).get("id")).toString();
							String finalString = id + ":"  + fullName;
							classesList.add(finalString);
						}
					}
					
					return classesList;
				    
				} catch (MalformedURLException e) {
					Log.i("URL","URL malformed");
				} catch (IOException e) {
					Log.i("IO", "IO Error");
				} catch (JSONException e) {
					Log.i("JSON","JSON Exception");
				} catch(Exception e){
					Log.i("General Exception",e.getMessage());
				}
				return null;
			}
						
			@Override
			protected void onPostExecute(LinkedList<String> result){				
				currentClasses = result;
				ListView lv = (ListView)findViewById(R.id.SemesterClasses_classList);
				ProgressBar pb = (ProgressBar)findViewById(R.id.SemesterClassesActivity_ProgressBar);
				pb.setVisibility(View.GONE);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_multiple_choice,currentClasses.toArray(new String[] {}));
				lv.setAdapter(adapter);
				
			}
		}.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semester_classes, menu);
		return true;
	}
	
	public void confirmSelection(View view){
		ListView lv = (ListView)findViewById(R.id.SemesterClasses_classList);
		SparseBooleanArray a = lv.getCheckedItemPositions();
		LinkedList<String> resultList = new LinkedList<String>();
		int resultCount = lv.getAdapter().getCount();
		
		for(int i=0; i<resultCount ;++i){
			if(a.get(i)){
				resultList.add(lv.getItemAtPosition(i).toString());
			}
		}

		Intent intent = new Intent();
		intent.putExtra("classesList",resultList.toString());
		setResult(RESULT_OK,intent);
		finish();
	}
}
