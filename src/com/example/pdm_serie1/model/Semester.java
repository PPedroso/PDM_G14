package com.example.pdm_serie1.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Semester implements IModelItem<Semester> {

	private final int lectiveSemesterId;
	private final String shortName;
	private final int startYear;
	private final int term;
	private final String termName;
	
	public Semester(int lectiveSemesterId, String shortName, int startYear, int term, String termName) {
		this.lectiveSemesterId = lectiveSemesterId;
		this.shortName = shortName;
		this.startYear = startYear;
		this.term = term;
		this.termName = termName;
	}	

	@Override
	public String toListItemString() {
		return shortName;
	}

	@Override
	public String toSharedPreferencesString() {
		return String.format("%s:%s:%s:%s:%s", lectiveSemesterId, shortName, startYear, term, termName); 
	}
	
	public static Semester fromSharedPreferences(String str) {
		String[] splittedStr = str.split(":");
		return new Semester(Integer.parseInt(splittedStr[0]), 
						    splittedStr[1], 
						    Integer.parseInt(splittedStr[2]), 
						    Integer.parseInt(splittedStr[3]), 
						    splittedStr[4]);
	}
	
	public static Semester fromJSONObject(JSONObject jsonObj) throws JSONException {
		return new Semester(jsonObj.getInt("lectiveSemesterId"),
						    jsonObj.getString("shortName"),
						    jsonObj.getInt("startYear"),
						    jsonObj.getInt("term"),
						    jsonObj.getString("termName"));
	}
	
	@Override
	public boolean representsSameItem(Semester sem) {
		return this.lectiveSemesterId == sem.getLectiveSemesterId();
	}
	
	//----------------------- getters --------------------------------
	
	public int getLectiveSemesterId() {
		return lectiveSemesterId;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public int getStartYear() {
		return startYear;
	}
	
	public int getTerm() {
		return term;
	}
	
	public String getTermName() {
		return termName;
	}
}
