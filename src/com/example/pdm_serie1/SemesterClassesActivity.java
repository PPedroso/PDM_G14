package com.example.pdm_serie1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pdm_serie1.adapters.CheckedListCustomInitAndTextArrayAdapter;
import com.example.pdm_serie1.asynctaskrelated.BasicAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.IAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.JsonHttpRequestAsyncTask;
import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.http.ThothEndPoints;
import com.example.pdm_serie1.http.exectypes.JsonObjectHttpExecuter;
import com.example.pdm_serie1.model.Semester;
import com.example.pdm_serie1.model.TClass;
import com.example.pdm_serie1.utils.ModelItemUtils;

public class SemesterClassesActivity extends ListActivity {

	private Semester currentSemester;
	private ListView listView;
	private List<TClass> selectedClasses = new LinkedList<TClass>();
	private List<TClass> initialClasses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesterclasses);
		
		final Button confirmButton = (Button)findViewById(R.id.confirmButton);
		String initialClassesStr = getIntent().getStringExtra("initialClasses");
		
		if(!initialClassesStr.equals("")) {
			initialClasses = Arrays.asList(TClass.fromSharedPreferences(initialClassesStr.split(",")));
			selectedClasses.addAll(initialClasses);
		} else {
			confirmButton.setEnabled(false);	
		}
		
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
		new JsonHttpRequestAsyncTask<String, Integer, List<TClass>>(this, "classes"){
			@Override
			protected IAsyncTaskResult<List<TClass>> actualBackgroundWork(String... params) throws JSONException, MyHttpException {
				JSONObject obj = new JsonObjectHttpExecuter().executeGet(ThothEndPoints.get().getClasses(), 
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
			}

			@Override
			protected void actualPostExecuteWork(IAsyncTaskResult<List<TClass>> taskResult) {
				ProgressBar pb = (ProgressBar)findViewById(R.id.SemesterClassesActivity_ProgressBar);
				pb.setVisibility(View.GONE);
				List<TClass> result = taskResult.getResult();
				ArrayAdapter<TClass> adapter = 
						new CheckedListCustomInitAndTextArrayAdapter<TClass>(
												  ctx, 
												  android.R.layout.simple_list_item_multiple_choice, 
												  result.toArray(new TClass[result.size()]),
												  initialClasses
											  ) {
								@Override
								protected void changeText(int position, View view) {
									CheckedTextView textView = (CheckedTextView) view.findViewById(android.R.id.text1);
									textView.setText(data[position].toListItemString());
								}					
				};
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
