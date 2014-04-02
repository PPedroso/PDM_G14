package com.example.pdm_serie1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DataObjects.Assignment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AssignmentActivity extends Activity {
	
	AsyncTask<String, Integer, LinkedList<Assignment>> a;
	final Activity act = this;
	
	private String classUri;
	
	private final int ASSIGNMENT_PAGE = 0;
	private final int ASSIGNMENT_SCHEDULE = 1;
	
	private String THOTH_API = "http://thoth.cc.e.ipl.pt/api/v1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment);
		
		Intent intent 	= getIntent();
		String classId 	= intent.getStringExtra("classId");
		this.classUri  	= intent.getStringExtra("classUri");
		getAssignmentInfo(classId);
		
		registerForContextMenu(findViewById(android.R.id.list));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
				
		AdapterView.AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) item.getMenuInfo();
		Assignment assignment = getAssignmentFromListView(adapter.position);

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
		String workItemUri = classUri + "/workItems/" + assignment.getId();
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(workItemUri));
		startActivity(intent);
	}
	
	private void scheduleAssignment(Assignment assignment){
		
		String[] dateTime = assignment.getDueDate().split("T");
		String[] date = dateTime[0].split("-");
		String[] time = dateTime[1].split(":");
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 0, 0, 0);		
		Calendar endTime = Calendar.getInstance();
		endTime.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
				
		String uriString = "content://com.android.calendar/events";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_INSERT, uri);
		intent.putExtra("Events.TITLE", assignment.getTitle());
		startActivity(intent);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		
		int position = ((AdapterContextMenuInfo)menuInfo).position;		
		Assignment assignment = getAssignmentFromListView(position);
		
		menu.setHeaderTitle("Due Date: " + assignment.getDueDate()); //TODO: Store assignment due date
		menu.add(0, ASSIGNMENT_PAGE, Menu.NONE, "Go to Page");
		menu.add(0, ASSIGNMENT_SCHEDULE, Menu.NONE, "Schedule on calendar");
	}
	
	private Assignment getAssignmentFromListView(int position){
		ListView lv = (ListView)findViewById(android.R.id.list);
		return (Assignment)lv.getItemAtPosition(position);
	}
	
	private void getAssignmentInfo(final String classId){
		a = new AsyncTask<String, Integer, LinkedList<Assignment>>(){
			@Override
			protected LinkedList<Assignment> doInBackground(String ... params){
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
					
					
					LinkedList<Assignment> resultList = new LinkedList<Assignment>();
					
					for(int i=0;i<a.length();++i){
						Assignment assignment = new Assignment();
						assignment.setTitle(((JSONObject)a.get(i)).get("title").toString());
						assignment.setDueDate(((JSONObject)a.get(i)).get("dueDate").toString());
						assignment.setId(Integer.parseInt(((JSONObject)a.get(i)).get("id").toString()));
						resultList.add(assignment);
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
			protected void onPostExecute(LinkedList<Assignment> result){								
				ListView lv = (ListView)findViewById(android.R.id.list);
				ArrayAdapter<Assignment> adapter = new ArrayAdapter<Assignment>(act,android.R.layout.simple_list_item_multiple_choice,result.toArray(new Assignment[] {}));
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
