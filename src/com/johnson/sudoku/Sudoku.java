package com.johnson.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.main);
		
		// add button click listener
		View continueBtn = super.findViewById(R.id.continue_btn);
		continueBtn.setOnClickListener(this);
		View newGameBtn = super.findViewById(R.id.new_game_btn);
		newGameBtn.setOnClickListener(this);
		View aboutBtn = super.findViewById(R.id.about_btn);
		aboutBtn.setOnClickListener(this);
		View exitBtn = super.findViewById(R.id.exit_btn);
		exitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_btn:
			Intent i = new Intent(this, About.class);
			super.startActivity(i);
			break;
		case R.id.exit_btn:
			System.exit(0);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = super.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			super.startActivity(new Intent(this, Prefs.class));
			return true;
		}
		return false;
	}
}	
