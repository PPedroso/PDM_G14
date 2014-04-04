package com.example.pdm_serie1.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

public class Assignment implements IModelItem<Assignment> {

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private final int id;
	private final String acronym;
	private final String title;
	private final boolean requiresGroupSubmission;
	private final Date startDate;
	private final Date dueDate;
	
	public Assignment(int id, String acronym, String title, boolean requiresGroupSubmission, 
					  Date startDate, Date dueDate) {
		this.id = id;
		this.acronym = acronym;
		this.title = title;
		this.requiresGroupSubmission = requiresGroupSubmission;
		this.startDate = startDate;
		this.dueDate = dueDate;
	}
	
	public static Assignment fromSharedPreferences(String str) throws ParseException {
		String[] arrayStr = str.split(":");
		return new Assignment(Integer.parseInt(arrayStr[0]),
							  arrayStr[1],
							  arrayStr[2],
							  Boolean.getBoolean(arrayStr[3]),
							  dateFormatter.parse(arrayStr[4]),
							  dateFormatter.parse(arrayStr[5]));
	}
	
	public static Assignment fromJSONObject(JSONObject jsonObj) throws JSONException, ParseException {
		return new Assignment(jsonObj.getInt("id"),
							  jsonObj.getString("acronym"),
							  jsonObj.getString("title"),
							  jsonObj.getBoolean("requiresGroupSubmission"),
							  dateFormatter.parse(jsonObj.getString("startDate")),
							  dateFormatter.parse(jsonObj.getString("dueDate")));
	}
	
	@Override
	public String toListItemString() {
		return title;
	}

	@Override
	public String toSharedPreferencesString() {
		return String.format("%s:%s:%s:%s:%s:%s", id, acronym, title, requiresGroupSubmission, 
						     dateFormatter.format(startDate), dateFormatter.format(dueDate));
	}

	@Override
	public boolean representsSameItem(Assignment t) {
		return id == t.getId();
	}	
	
	//------------------------ getters --------------------------------
	public int getId(){
		return id;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public String getTitle(){
		return title;
	}
	
	public boolean getRequiresGroupSubmission() {
		return requiresGroupSubmission;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getDueDate(){
		return dueDate;		
	}
}
