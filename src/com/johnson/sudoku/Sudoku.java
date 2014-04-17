package com.johnson.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.main);
		
		// add button click listener
		super.findViewById(R.id.continue_btn).setOnClickListener(btnClickListener);
		super.findViewById(R.id.new_game_btn).setOnClickListener(btnClickListener);
		super.findViewById(R.id.about_btn).setOnClickListener(btnClickListener);
		super.findViewById(R.id.exit_btn).setOnClickListener(btnClickListener);
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
	
	/**
	 * OnclickListener
	 */
	private final OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.new_game_btn:
				openNewGameDialog();
				break;
			case R.id.about_btn:
				Intent i = new Intent(Sudoku.this, About.class);
				Sudoku.this.startActivity(i);
				break;
			case R.id.exit_btn:
				System.exit(0);
			}
		}
		
		/**
		 * Open a difficulty selecting dialog for new game
		 */
		private void openNewGameDialog() {
			new AlertDialog.Builder(Sudoku.this)
					.setTitle(R.string.new_game_title)
					.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startNewGame(which);
						}
					})
					.show();
		}
		
		/**
		 * Start new game
		 * @param option
		 * 		selected difficulty option
		 */
		private void startNewGame(int option) {
			Log.d(Sudoku.class.getName(), "clicked on " + option);
			// TODO: start game
		}
	};
	
	
}	
