package com.example.bla.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.bla.R;

public class MyPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
	}
	
	
	

}
