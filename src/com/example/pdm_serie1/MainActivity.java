package com.example.pdm_serie1;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	private String THOTH_API = "http://thoth.cc.e.ipl.pt/api/v1";
	
	//Constants for startActivityForResult
	final private int CLASSES_LIST = 0;
	
	//Constants for context menu
	final private int MENU_CLASS_INFO = 0;
	final private int MENU_ASSIGNMENTS = 1;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		registerForContextMenu(getListView());
		
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(sharedPrefs.contains("classesList")){
			final ListView lv = getListView();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									this,
									android.R.layout.simple_list_item_1,
									sharedPrefs.getString("classesList", "").replace("[", "").replace("]", "").split(",")
									);
			lv.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, 
		ContextMenuInfo menuInfo) {
			
		//super.onCreateContextMenu(menu, v, menuInfo);    
		getMenuInflater().inflate(R.menu.context_menu, menu);
		menu.add(Menu.NONE, MENU_CLASS_INFO, Menu.NONE, "Info");
		menu.add(Menu.NONE, MENU_ASSIGNMENTS, Menu.NONE, "Assignments");
	}
	
	
	/**
	 * Construct the URI and call the browser
	 */
	private void showClassInfo(String uri){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(uri));
		startActivity(intent);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo acmi;
		
		ListView lv;
		switch (item.getItemId()) {
		
			//Show class homepage
			case MENU_CLASS_INFO:
		        
		    	showClassInfo(getClassUri(item) + "/info");
		        
		        return true;
		    
		    //Show assignment list for the specified class
			case MENU_ASSIGNMENTS:
				lv = getListView();
		    	acmi= (AdapterContextMenuInfo) item.getMenuInfo();
		    	String classId = lv.getItemAtPosition(acmi.position).toString();
		    	classId = classId.substring(0,classId.indexOf(":")).replace(" ", "");
		    	launchAssignmentActivity(classId, getClassUri(item));
				
		        return true;
		    default:
		        return super.onContextItemSelected(item);
		}
	}
	
	//Retorna o URI base para a turma (cadeira+semestre)
	private String getClassUri(MenuItem item){
		ListView lv = getListView();
		AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		String[] info = lv.getItemAtPosition(acmi.position).toString().split("/");
		String uri = ("http://thoth.cc.e.ipl.pt/classes/" + info[0].split(":")[1] + "/" + info[1] + "/" + info[2]).replace(" ", "");
		
		return uri;
	}
	
	public void launchAssignmentActivity(String classId, String uri){
		Intent intent = new Intent(MainActivity.this,AssignmentActivity.class);
		intent.putExtra("classId",classId);
		intent.putExtra("classUri", uri);
		startActivity(intent);
	}
	
	public void launchSemesterActivity(View view){
		Intent intent = new Intent(this,SemestersActivity.class);
		startActivity(intent);
	}
	
	public void launchSemesterClassesActivity(View view){
		Intent intent = new Intent(this, SemesterClassesActivity.class);
		startActivityForResult(intent,CLASSES_LIST);;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == CLASSES_LIST){
				String classesString =  data.getStringExtra("classesList");
				String[] classesList = classesString.replace("[", "").replace("]", "").split(",");
				
				final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
				Editor e = sharedPrefs.edit();
				e.putString("classesList", classesString);
				e.apply();
								
				ListView lv = getListView();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,classesList);
				lv.setAdapter(adapter);
				
			}
		}
	}
	
}
