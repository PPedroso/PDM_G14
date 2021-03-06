package com.example.pdm_serie1.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TClass implements IModelItem<TClass> {

	private final int id;
	private final String fullName;
	private final String courseUnitShortName;
	private final String lectiveSemesterShortName;
	private final String className;
	private final String mainTeacherShortName;
	
	public TClass(int id, String fullName, String courseUnitShortName, String lectiveSemesterShortName,
				 String className, String mainTeacherShortName) {
		this.id = id;
		this.fullName = fullName;
		this.courseUnitShortName = courseUnitShortName;
		this.lectiveSemesterShortName = lectiveSemesterShortName;
		this.className = className;
		this.mainTeacherShortName = mainTeacherShortName;
	}
	
	@Override
	public String toListItemString() {
		return String.format("%s - %s", courseUnitShortName, className);
	}

	@Override
	public String toSharedPreferencesString() {
		return String.format("%s:%s:%s:%s:%s:%s", id, fullName, courseUnitShortName,
				lectiveSemesterShortName, className, mainTeacherShortName);
	}				

	public static TClass fromSharedPreferences(String str) {
		String[] fields = str.split(":");
		return new TClass(Integer.parseInt(fields[0]),
						  fields[1], 
						  fields[2],
						  fields[3],
						  fields[4],
						  fields[5]);
	}
	
	public static TClass[] fromSharedPreferences(String[] strArray) {
		TClass[] retArray = new TClass[strArray.length];
		for(int i = 0; i < retArray.length; ++i) {
			retArray[i] = fromSharedPreferences(strArray[i]);
		}
		return retArray;
	}
	
	public static TClass fromJSONObject(JSONObject jsonObj) throws JSONException {
		return new TClass(jsonObj.getInt("id"),
						  jsonObj.getString("fullName"),
						  jsonObj.getString("courseUnitShortName"),
						  jsonObj.getString("lectiveSemesterShortName"),
						  jsonObj.getString("className"),
						  jsonObj.getString("mainTeacherShortName"));
	}
	
	@Override
	public boolean representsSameItem(TClass t) {
		return this.getId() == t.getId();
	}
	
	//Incompolete equals, no verificaion besides the initial, is made for NullPointerExceptions.
	@Override
	public boolean equals(Object arg) {
		if(arg == null || !(arg instanceof TClass)) {
			return false;
		}
		TClass other = (TClass) arg;
		return this.id == other.id &&
			   this.fullName.equals(other.fullName) &&
			   this.courseUnitShortName.equals(other.courseUnitShortName) &&
			   this.lectiveSemesterShortName.equals(other.lectiveSemesterShortName) &&
			   this.className.equals(other.className) &&
			   this.mainTeacherShortName.equals(other.mainTeacherShortName); 
	}
	
	//------------------------------ Getters -------------------------------
	
	public int getId() {
		return id;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getCourseUnitShortName() {
		return courseUnitShortName;
	}
	
	public String getLectiveSemesterShortName() {
		return lectiveSemesterShortName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getMainTeacherShortName() {
		return mainTeacherShortName;
	}
}
