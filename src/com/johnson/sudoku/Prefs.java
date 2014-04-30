package com.johnson.sudoku;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
	
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = true;
	private static final String OPT_HINTS = "hints";
	private static final boolean OPT_HINTS_DEF = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.addPreferencesFromResource(R.xml.settings);
	}
	
	public static boolean isMusicOn(Context context) {
		return getPref(context, OPT_MUSIC, OPT_MUSIC_DEF);
	}
	
	public static boolean isHintsOn(Context context) {
		return getPref(context, OPT_HINTS, OPT_HINTS_DEF);
	}
	
	private static boolean getPref(Context context, String key, boolean defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, defValue);
	}
}
