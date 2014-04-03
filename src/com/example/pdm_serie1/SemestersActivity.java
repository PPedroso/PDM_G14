package com.example.pdm_serie1;

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
import android.widget.Toast;

import com.example.pdm_serie1.adapters.CustomTextArrayAdapter;
import com.example.pdm_serie1.asynctaskrelated.BasicAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.IAsyncTaskResult;
import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.exceptions.UnexpectedStatusCodeException;
import com.example.pdm_serie1.http.ThothEndPoints;
import com.example.pdm_serie1.http.exectypes.JsonObjectHttpExecuter;
import com.example.pdm_serie1.model.Semester;

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
				String str = ((Semester) list.getItemAtPosition(position)).toSharedPreferencesString();
				edit.putString("currentSemester", str);
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
		new AsyncTask<String, Integer, IAsyncTaskResult<Semester[]>>(){
			@Override
			protected IAsyncTaskResult<Semester[]> doInBackground(String ... params){
				try {
					JSONObject obj = new JsonObjectHttpExecuter()
													.executeGet(ThothEndPoints.get().getSemesters(), 
																200);
					JSONArray arrayObj = obj.getJSONArray("lectiveSemesters");
					Semester[] resultArray = new Semester[arrayObj.length()];
				    for(int i = 0; i < arrayObj.length(); ++i){
				    	resultArray[i] = Semester.fromJSONObject(arrayObj.getJSONObject(i));
					}
					return new BasicAsyncTaskResult<Semester[]>(resultArray);				
				} catch (JSONException e) {
					Log.e("JSON", 
						  "The parsing of GET request for all lective semesters isn't being handled propertly", 
						  e);
					return new BasicAsyncTaskResult<Semester[]>(e);
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
					return new BasicAsyncTaskResult<Semester[]>(e);
				}
			}
						
			@Override
			protected void onPostExecute(IAsyncTaskResult<Semester[]> asyncTaskResult){
				if(asyncTaskResult.getError() != null) {
					Toast.makeText(thisAct, "An unexpected error occured", Toast.LENGTH_SHORT).show();
					return;
				}
				Semester[] result = asyncTaskResult.getResult();
				ListView lv = (ListView)findViewById(android.R.id.list);
				ArrayAdapter<Semester> adapter 
						= new CustomTextArrayAdapter<Semester>(thisAct,
				  											   android.R.layout.simple_list_item_1,
															   result);
				lv.setAdapter(adapter);
			}
		}.execute();
	}

}
