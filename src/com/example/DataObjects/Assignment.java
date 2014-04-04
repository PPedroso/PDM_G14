package com.example.DataObjects;

public class Assignment {

	private int id;
	private String title;
	private String dueDate;
	private String semester;
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getDueDate(){
		return dueDate;		
	}

	public void setDueDate(String dueDate){
		this.dueDate = dueDate;
	}
	
	public void setSemester(String semester){
		this.semester = semester;
	}
	
	public String getSemester(){
		return semester;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	@Override
	public String toString(){
			return title;	
	}
	
}
