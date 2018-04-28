package by.drawgrid.library.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import by.drawgrid.library.R;


public class MyPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
	}
	
	
	

}
