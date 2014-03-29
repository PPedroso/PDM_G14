package com.example.pdm_serie1;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	final private int CLASSES_LIST = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(sharedPrefs.contains("classesList")){
			ListView lv = getListView();
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
