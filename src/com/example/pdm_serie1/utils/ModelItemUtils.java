package com.example.pdm_serie1.utils;

import java.util.Iterator;

import com.example.pdm_serie1.model.IModelItem;

public class ModelItemUtils {

	public static <T extends IModelItem> String concatIModelItemSharedPref(Iterable<T> iter, 
																		   String concatStr) {
		Iterator<T> it = iter.iterator();
		if(!it.hasNext()) {
			return "";
		}
		StringBuilder builder = new StringBuilder(it.next().toSharedPreferencesString());
		while(it.hasNext()) {
			builder.append(concatStr).append(it.next());
		}
		return builder.toString();		
	}
}
