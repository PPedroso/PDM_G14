package com.example.pdm_serie1;


import java.util.LinkedList;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	
	LinkedList<String> classesList = new LinkedList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView lv = getListView();
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String classes = sharedPrefs.getString("classesList", "");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,classes.split(","));
		lv.setAdapter(adapter);
		
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
		startActivity(intent);
	}
	
}
