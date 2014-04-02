package com.example.pdm_serie1.http;

public class ThothEndPoints {

	//-------------------- Singleton Related --------------------------
	private static volatile ThothEndPoints endPoints;
	
	public static ThothEndPoints get() {
		if(endPoints == null) {
			synchronized(ThothEndPoints.class) {
				if(endPoints == null) {
					endPoints = new ThothEndPoints();
				}
			}
		}
		return endPoints;
	}
	
	//---------------- Actual Implementation --------------------------	
	
	private final String thothApiRoot = "http://thoth.cc.e.ipl.pt/api/v1";
	
	private ThothEndPoints() {
	}
	
	public String getSemesters() {
		return String.format("%s/lectivesemesters", thothApiRoot);
	}
	
	public String getSemester(int semesterId) {
		return String.format("%s/lectivesemesters/%s", thothApiRoot, semesterId);
	}
	
	public String getClasses() {
		return String.format("%s/classes", thothApiRoot);
	}
	
	public String getClasse(int classId) {
		return String.format("%s/classes/%s", thothApiRoot, classId);
	}
	
	public String getClassWorkItems(int classId) {
		return String.format("%s/classes/%s/workitems", thothApiRoot, classId);
	}
	
	public String getWorkItem(int workItemId) {
		return String.format("%s/workItems/%s", thothApiRoot, workItemId);
	}
}
