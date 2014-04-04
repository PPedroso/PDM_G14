package com.example.pdm_serie1;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pdm_serie1.adapters.NormalListCustomTextArrayAdapter;
import com.example.pdm_serie1.model.TClass;

public class MainActivity extends ListActivity {
	
	//Constants for startActivityForResult
	private static final int CLASSES_LIST = 0;
	private static final int SEMESTERS_LIST = 1; 
	
	//Constants for context menu
	private static final int MENU_CLASS_INFO = 0;
	private static final int MENU_ASSIGNMENTS = 1;
	
	//list of selected classes
	private List<TClass> selectedClasses;
	
	private ListView lv;
	private SharedPreferences sharedPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lv = getListView();
		registerForContextMenu(lv);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(sharedPrefs.contains("classesList")){
			String[] classesAsStr = sharedPrefs.getString("classesList", "").split(",");
			selectedClasses = TClass.fromSharedPreferences(classesAsStr);
			ArrayAdapter<TClass> adapter = new NormalListCustomTextArrayAdapter<TClass>(
										this,
										android.R.layout.simple_list_item_1,
										selectedClasses.toArray(new TClass[selectedClasses.size()])
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
		startActivityForResult(intent, SEMESTERS_LIST);
	}
	
	public void launchSemesterClassesActivity(View view){
		Intent intent = new Intent(this, SemesterClassesActivity.class);
		intent.putExtra("initialClasses", sharedPrefs.getString("classesList", ""));
		startActivityForResult(intent, CLASSES_LIST);;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK) {
			Editor e = sharedPrefs.edit();
			
			if(requestCode == CLASSES_LIST){
				String allClassesString =  data.getStringExtra("classesList");
				e.putString("classesList", allClassesString);
				
				selectedClasses = TClass.fromSharedPreferences(allClassesString.split(",")); 
				ArrayAdapter<TClass> adapter = new NormalListCustomTextArrayAdapter<TClass>(
												  this,
												  android.R.layout.simple_list_item_1,
												  selectedClasses.toArray(new TClass[selectedClasses.size()])
											  );
				lv.setAdapter(adapter);
			} else if(requestCode == SEMESTERS_LIST) {
				String str = data.getStringExtra("semester");
				e.putString("currentSemester", str);
			}
			e.apply();
		}
	}	
	
}
//String.format("http://thoth.cc.e.ipl.pt/classes/%s/%s/%s/info", info[0], info[1], info[2]);