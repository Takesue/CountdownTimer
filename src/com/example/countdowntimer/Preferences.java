package com.example.countdowntimer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.preferences_screen);
		this.getPreferenceManager().setSharedPreferencesName("CountdownTimerPrefs");
		this.addPreferencesFromResource(R.xml.preferences);
	}

	
}
