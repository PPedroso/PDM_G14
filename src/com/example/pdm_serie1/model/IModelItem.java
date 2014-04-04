package com.example.pdm_serie1.model;

public interface IModelItem<T> {

	String toListItemString();
	String toSharedPreferencesString();
	boolean representsSameItem(T t);
	
}
