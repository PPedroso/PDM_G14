package com.example.pdm_serie1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pdm_serie1.adapters.NormalListCustomTextArrayAdapter;
import com.example.pdm_serie1.asynctaskrelated.BasicAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.IAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.JsonHttpRequestAsyncTask;
import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.http.ThothEndPoints;
import com.example.pdm_serie1.http.exectypes.JsonObjectHttpExecuter;
import com.example.pdm_serie1.model.Semester;

public class SemestersActivity extends ListActivity {

	private ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);
		
		list = getListView();
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				String retStr = ((Semester) list.getItemAtPosition(position)).toSharedPreferencesString();
				intent.putExtra("semester", retStr);
				setResult(RESULT_OK, intent);
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
		new JsonHttpRequestAsyncTask<String, Integer, Semester[]>(this, "semesters"){
			@Override
			protected IAsyncTaskResult<Semester[]> actualBackgroundWork(String...params) throws JSONException, MyHttpException {
				JSONObject obj = new JsonObjectHttpExecuter().executeGet(ThothEndPoints.get().getSemesters(), 
																		 200);
				JSONArray arrayObj = obj.getJSONArray("lectiveSemesters");
				Semester[] resultArray = new Semester[arrayObj.length()];
				for(int i = 0; i < arrayObj.length(); ++i){
					resultArray[i] = Semester.fromJSONObject(arrayObj.getJSONObject(i));
				}
				return new BasicAsyncTaskResult<Semester[]>(resultArray);
			}

			@Override
			protected void actualPostExecuteWork(IAsyncTaskResult<Semester[]> taskResult) {
				Semester[] result = taskResult.getResult();
				ArrayAdapter<Semester> adapter = new NormalListCustomTextArrayAdapter<Semester>(
															   ctx,
				  											   android.R.layout.simple_list_item_1,
															   result
															);
				list.setAdapter(adapter);
				
			}
			
		}.execute();
	}
}
