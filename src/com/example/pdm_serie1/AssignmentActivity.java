package com.example.pdm_serie1;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.pdm_serie1.adapters.NormalListCustomTextArrayAdapter;
import com.example.pdm_serie1.asynctaskrelated.BasicAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.IAsyncTaskResult;
import com.example.pdm_serie1.asynctaskrelated.JsonHttpRequestAsyncTask;
import com.example.pdm_serie1.exceptions.MyHttpException;
import com.example.pdm_serie1.http.ThothEndPoints;
import com.example.pdm_serie1.http.exectypes.JsonObjectHttpExecuter;
import com.example.pdm_serie1.model.Assignment;

public class AssignmentActivity extends Activity {
	
	private String classUri;
	private ListView lv;
	
	private final int ASSIGNMENT_PAGE = 0;
	private final int ASSIGNMENT_SCHEDULE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment);
		
		lv = (ListView)findViewById(android.R.id.list);
		Intent intent 	= getIntent();
		int classId 	= intent.getIntExtra("classId", 0);
		this.classUri  	= intent.getStringExtra("classUri");
		registerForContextMenu(findViewById(android.R.id.list));

		getAssignmentInfo(classId);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignment, menu);
		return true;
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {				
		AdapterView.AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) item.getMenuInfo();
		Assignment assignment = (Assignment)lv.getItemAtPosition(adapter.position);
		switch(item.getItemId()){
			case ASSIGNMENT_PAGE:
				showAssignmentPage(assignment);
				return true;
			case ASSIGNMENT_SCHEDULE:
				scheduleAssignment(assignment);
				return true;
		}
		return false;
	}
	
	private void showAssignmentPage(Assignment assignment){
		String workItemUri = String.format("%s/workItems/%s", classUri,assignment.getId());
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(workItemUri));
		startActivity(intent);
	}
	
	private void scheduleAssignment(Assignment assignment){			
		Uri uri = Uri.parse("content://com.android.calendar/events");
		Intent intent = new Intent(Intent.ACTION_INSERT,uri);
		intent.putExtra(Events.TITLE, assignment.getTitle());
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, assignment.getStartDate().getTime());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, assignment.getDueDate().getTime());
		intent.putExtra(Events.DESCRIPTION, String.format("%s", assignment.getTitle()));
		startActivity(intent);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		
		int position = ((AdapterContextMenuInfo)menuInfo).position;		
		Assignment assignment = (Assignment)lv.getItemAtPosition(position);
		
		menu.setHeaderTitle("Due Date: " + assignment.getDueDate());
		menu.add(0, ASSIGNMENT_PAGE, Menu.NONE, "Go to Page");
		menu.add(0, ASSIGNMENT_SCHEDULE, Menu.NONE, "Schedule on calendar");
	}
	
	private void getAssignmentInfo(final int classId){
		new JsonHttpRequestAsyncTask<Void, Integer, Assignment[]>(this, "work items"){
			@Override
			protected IAsyncTaskResult<Assignment[]> actualBackgroundWork(Void... params) throws JSONException, MyHttpException {
				JSONObject obj = new JsonObjectHttpExecuter()
											.executeGet(ThothEndPoints.get().getClassWorkItems(classId), 
														200);
				JSONArray arrayObj = obj.getJSONArray("workItems");
				Assignment[] arrayAssigment = new Assignment[arrayObj.length()];
				try {
					for(int i = 0; i < arrayAssigment.length; ++i) {
						arrayAssigment[i] = Assignment.fromJSONObject(arrayObj.getJSONObject(i));
					}
					return new BasicAsyncTaskResult<Assignment[]>(arrayAssigment);
				} catch (ParseException e) {
					Log.e("HTTP", "The data format on work items has changed", e);
					return new BasicAsyncTaskResult<Assignment[]>(e);
				}
			}

			@Override
			protected void actualPostExecuteWork(IAsyncTaskResult<Assignment[]> result) {
				ProgressBar pb = (ProgressBar)findViewById(R.id.AssignmentActivity_ProgressBar);
				pb.setVisibility(View.GONE);
				ArrayAdapter<Assignment> adapter 
					= new NormalListCustomTextArrayAdapter<Assignment>(
													ctx,
													android.R.layout.simple_list_item_1,
													result.getResult()
												);
				lv.setAdapter(adapter);
			}
		}.execute();
	}
}
