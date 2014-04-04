package com.example.pdm_serie1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class CleanSharedPreferences {
	
	public static void clean(Context ctx) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		Editor e = sharedPrefs.edit();
		e.clear().apply();
	}
}
