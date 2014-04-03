package com.example.pdm_serie1;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdm_serie1.adapters.CheckedListCustomTextArrayAdapter;
import com.example.pdm_serie1.asynctaskrelated.BasicAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.IAsyncTaskResult;
import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.exceptions.UnexpectedStatusCodeException;
import com.example.pdm_serie1.http.ThothEndPoints;
import com.example.pdm_serie1.http.exectypes.JsonObjectHttpExecuter;
import com.example.pdm_serie1.model.IModelItem;
import com.example.pdm_serie1.model.Semester;
import com.example.pdm_serie1.model.TClass;
import com.example.pdm_serie1.utils.ModelItemUtils;

public class SemesterClassesActivity extends ListActivity {

	private Semester currentSemester;
	private ListView listView;
	private List<TClass> selectedClasses = new LinkedList<TClass>();
	private Button confirmButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesterclasses);
		
		confirmButton = (Button)findViewById(R.id.confirmButton);
		confirmButton.setEnabled(false);
		
		listView = getListView();
		listView.setOnItemClickListener(new	OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TClass thothClass = (TClass)listView.getItemAtPosition(position);
				if(listView.isItemChecked(position)) {
					selectedClasses.add(thothClass);
				} else {
					selectedClasses.remove(thothClass);
				}
				confirmButton.setEnabled(selectedClasses.size() != 0);
			}				
		});
		currentSemester = Semester.fromSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this)
																		  .getString("currentSemester", null));
		TextView semView = (TextView)findViewById(R.id.SemesterClassesActivity_CurrSemesterView);
		semView.setText(currentSemester.getShortName());
		getClasses();				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.semester_classes, menu);
		return true;
	}
	
	public void getClasses(){
		final Activity act = this;
		new AsyncTask<String, Integer, IAsyncTaskResult<List<TClass>>>(){
			@Override
			protected BasicAsyncTaskResult<List<TClass>> doInBackground(String ... params){
				try {
					JSONObject obj = new JsonObjectHttpExecuter()
													.executeGet(ThothEndPoints.get().getClasses(), 
																200);
				    JSONArray arrayObj = obj.getJSONArray("classes");
				    List<TClass> listClass = new LinkedList<TClass>();
				    for(int i=0; i < arrayObj.length(); ++i){
						TClass thothClass = TClass.fromJSONObject(arrayObj.getJSONObject(i)); 
				    	if(thothClass.getLectiveSemesterShortName().equals(currentSemester.getShortName())) {
				    		listClass.add(thothClass);
				    	}
					}					
					return new BasicAsyncTaskResult<List<TClass>>(listClass);				    
				} catch (JSONException e) {
					Log.e("JSON", 
						  "The parsing of GET request for all class isn't being handled propertly", 
						  e);
					return new BasicAsyncTaskResult<List<TClass>>(e);
				} catch (MyHttpException e) {
					if(e instanceof UnexpectedStatusCodeException) {
						Log.e("HTTP", 
							  String.format("The GET request to obtain the classes list is returning is"
									  	    + " returning % status instead of 200", 
									  	    ((UnexpectedStatusCodeException)e).getRecievedStatusCode()),
							  e);
					} else {
						Log.w("HTTP",
						 	  "An error occured provoked by external causes occured while requesting"
							  + " the list of classes",
							  e);
					}
					return new BasicAsyncTaskResult<List<TClass>>(e);
				}
			}
						
			@Override
			protected void onPostExecute(IAsyncTaskResult<List<TClass>> taskResult){				
				if(taskResult.getError() != null) {
					Toast.makeText(act, "An unexpected error occured", Toast.LENGTH_SHORT).show();
					return;
				}
				ProgressBar pb = (ProgressBar)findViewById(R.id.SemesterClassesActivity_ProgressBar);
				pb.setVisibility(View.GONE);
				List<TClass> result = taskResult.getResult();
				ArrayAdapter<IModelItem> adapter 
						= new CheckedListCustomTextArrayAdapter<TClass>(
													 act, 
												     android.R.layout.simple_list_item_multiple_choice, 
												     result.toArray(new TClass[result.size()])
												 );
				listView.setAdapter(adapter);				
			}
		}.execute();
	}
	
	public void confirmSelection(View view){		
		Intent intent = new Intent();
		String retStr = ModelItemUtils.concatIModelItemSharedPref(selectedClasses, ",");
		intent.putExtra("classesList", retStr);
		setResult(RESULT_OK, intent);
		finish();
	}
}
