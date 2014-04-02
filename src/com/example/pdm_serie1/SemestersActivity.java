package com.example.pdm_serie1;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pdm_serie1.asynchandlers.BasicAsyncTaskResult;
import com.example.pdm_serie1.asynchandlers.IAsyncTaskResult;
import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.exceptions.UnexpectedStatusCodeException;
import com.example.pdm_serie1.http.ThothEndPoints;
import com.example.pdm_serie1.http.exectypes.JsonObjectHttpExecuter;

public class SemestersActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);
		
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		final ListView list = getListView();
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Editor edit = sharedPrefs.edit();
				edit.putString("currentSemester", list.getItemAtPosition(position).toString());
				edit.apply();
				finish();
			}
		});
		getInfoAndUpdateListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semesters, menu);
		return true;
	}
	
	//Gathers the information relative to the semesters and puts it on the listview
	public void getInfoAndUpdateListView(){
		final Activity thisAct = this;
		new AsyncTask<String, Integer, IAsyncTaskResult<LinkedList<String>>>(){
			@Override
			protected IAsyncTaskResult<LinkedList<String>> doInBackground(String ... params){
				try {
					JSONObject obj = new JsonObjectHttpExecuter().executeGet(ThothEndPoints.get().getSemesters(), 
																			 200);
					JSONArray arrayObj = obj.getJSONArray("lectiveSemesters");
				    LinkedList<String> resultList = new LinkedList<String>();
				    for(int i=0;i<arrayObj.length();++i){
						resultList.add((String)((JSONObject)arrayObj.get(i)).get("shortName"));
					}
					return new BasicAsyncTaskResult<LinkedList<String>>(resultList);				
				} catch (JSONException e) {
					Log.e("JSON", 
						  "The parsing of GET request for all lective semesters isn't being handled propertly", 
						  e);
					return new BasicAsyncTaskResult<LinkedList<String>>(e);
				} catch (MyHttpException e) {
					if(e instanceof UnexpectedStatusCodeException) {
						Log.e("HTTP", 
							  String.format("The GET request to obtain the semesters list is returning is"
									  	    + " returning % status instead of 200", 
									  	    ((UnexpectedStatusCodeException)e).getRecievedStatusCode()),
							  e);
					} else {
						Log.w("HTTP",
						 	  "An error occured provoked by external causes occured while requesting"
							  + " the list of semesters",
							  e);
					}
					return new BasicAsyncTaskResult<LinkedList<String>>(e);
				}
			}
						
			@Override
			protected void onPostExecute(IAsyncTaskResult<LinkedList<String>> asyncTaskResult){
				if(asyncTaskResult.getError() != null) {
					//TODO -> arranjar uma view para os erros
					return;
				}
				ListView lv = (ListView)findViewById(android.R.id.list);
				ArrayAdapter<String> adapter 
						= new ArrayAdapter<String>(thisAct,
	  											   android.R.layout.simple_list_item_1,
												   asyncTaskResult.getResult().toArray(new String[] { }));
				lv.setAdapter(adapter);
			}
		}.execute();
	}

}
