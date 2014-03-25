package com.example.pdm_serie1;


import java.util.LinkedList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private final int SEMESTERS_ACTIVITY_CODE = 1;
	private final int CLASSES_ACTIVITY_CODE = 2;
	private String currentSemester="";
	LinkedList<String> classesList = new LinkedList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
