package com.example.bla.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bla.R;
import com.example.bla.model.Point;
import com.example.bla.view.ViewPlusGrid;

public class MainActivity extends Activity {
	
	public static final int IDM_OPEN = 101;
	public static final int IDM_SAVE = 102;
	public static final int IDM_EXIT = 104;
	public static final int IDM_SETTINGS = 103;

	ViewPlusGrid viewPlusGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	void init() {
		viewPlusGrid = (ViewPlusGrid) findViewById(R.id.draw_field);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences preferences = 
				PreferenceManager.getDefaultSharedPreferences(this);
		
		viewPlusGrid.setFlagForScaleX(preferences.getBoolean(getString(R.string.pr_scaleX), true));
		viewPlusGrid.setFlagForScaleY(preferences.getBoolean(getString(R.string.pr_scaleY), true));
		viewPlusGrid.setFlagForScrollX(preferences.getBoolean(getString(R.string.pr_scrollX), true));
		viewPlusGrid.setFlagForScrollY(preferences.getBoolean(getString(R.string.pr_scrollY), true));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(Menu.NONE, IDM_OPEN, Menu.NONE, getString(R.string.menu_Open)).setIcon(
				R.drawable.open);
		menu.add(Menu.NONE, IDM_SAVE, Menu.NONE,getString(R.string.menu_Save)).setIcon(
				R.drawable.save);
		menu.add(Menu.NONE, IDM_EXIT, Menu.NONE, getString(R.string.menu_Something)).setIcon(
				R.drawable.exit);
		menu.add(Menu.NONE, IDM_SETTINGS, Menu.NONE, getString(R.string.menu_Setting)).setIcon(
				R.drawable.settings);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case IDM_OPEN:
			viewPlusGrid.changeMaxMin(new Point(40, 70), new Point(10, 30));
			
			break;
		case IDM_SAVE:
			viewPlusGrid.clearDrawFild(getApplicationContext());
			
			break;
		case IDM_SETTINGS:
			Intent intent = new Intent();
			intent.setClass(this, MyPreferenceActivity.class);
			startActivity(intent);
			break;
		case IDM_EXIT:
			
			break;

		}
		return true;
	}

	
}
