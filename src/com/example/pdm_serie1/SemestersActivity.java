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
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SemestersActivity extends ListActivity {

	AsyncTask<String, Integer, LinkedList<String>> a;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);
		
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		final ListView list = getListView();
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Editor edit = sharedPrefs.edit();
				if(sharedPrefs.contains("currentSemester")){
					edit.remove("currentSemester");
				}
				edit.putString("currentSemester", list.getItemAtPosition(position).toString());
				edit.apply();
				
				finish();
			}
		});
		getInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semesters, menu);
		return true;
	}
	
	//Gathers the information relative to the semesters and puts it on the listview
	public void getInfo(){
		final Activity act = this;
		a = new AsyncTask<String, Integer, LinkedList<String>>(){
			@Override
			protected LinkedList<String> doInBackground(String ... params){
				try {
					InputStream is = new URL("http://thoth.cc.e.ipl.pt/api/v1/lectivesemesters").openStream();
											
					BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				    
					StringBuilder sb = new StringBuilder();
				    int cp;
				    while ((cp = rd.read()) != -1) {
				      sb.append((char) cp);
				    }
				    is.close();
				    rd.close();
				    
				    LinkedList<String> resultList = new LinkedList<String>();
					
					JSONArray a = new JSONObject(sb.toString()).getJSONArray("lectiveSemesters");
					
					for(int i=0;i<a.length();++i){
						resultList.add((String)((JSONObject)a.get(i)).get("shortName"));
					}
					
					return resultList;
				    
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
				ListView lv = (ListView)findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_1,result.toArray(new String[] { }));
				lv.setAdapter(adapter);
			}
		}.execute();
	}

}
