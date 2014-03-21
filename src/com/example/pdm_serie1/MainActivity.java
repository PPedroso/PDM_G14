package com.example.pdm_serie1;


import java.util.LinkedList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

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
		startActivityForResult(intent,SEMESTERS_ACTIVITY_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == SEMESTERS_ACTIVITY_CODE){
					currentSemester = data.getStringExtra("SemesterInfo");
					//Stores the selected semester
			}
			else if(resultCode == CLASSES_ACTIVITY_CODE){
				//NOT IMPLEMENTED
				//Store the list of chosen classes and update the ListView
			}
		}
	}
}
