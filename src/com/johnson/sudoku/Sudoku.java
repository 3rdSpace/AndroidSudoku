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
	
	private static final String TAG = Sudoku.class.getName();

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
	
	@Override
	public void onResume() {
		super.onResume();
		Music.play(this, R.raw.main);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Music.stop(this);
	}
	
	
	/**
	 * OnclickListener
	 */
	private final OnClickListener btnClickListener = new OnClickListener() {
		
		private Activity activity = Sudoku.this;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.continue_btn:
				this.openContinueGameDialog();
				break;
			case R.id.new_game_btn:
				this.openNewGameDialog();
				break;
			case R.id.about_btn:
				Intent i = new Intent(Sudoku.this, About.class);
				activity.startActivity(i);
				break;
			case R.id.exit_btn:
				activity.finish();
				break;
			}
		}
		
		/**
		 * Open a difficulty selecting dialog for new game
		 */
		private void openNewGameDialog() {
			new AlertDialog.Builder(activity)
					.setTitle(R.string.new_game_title)
					.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startGame(which);
						}
					})
					.show();
		}
		
		private void openContinueGameDialog() {
			new AlertDialog.Builder(activity)
					.setTitle(R.string.continue_game_title)
					.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							continueGame(which);
						}
					})
					.show();
		}
		
		/**
		 * Start new game
		 * @param option
		 * 		selected difficulty option
		 */
		private void startGame(int option) {
			Log.d(TAG, " new game: clicked on " + option);
			Intent in = new Intent(activity, Game.class);
			in.putExtra(Game.KEY_DIFFICULTY, option);
			activity.startActivity(in);
		}
		
		/**
		 * Continue old game
		 * @param option
		 * 		selected difficulty option
		 */
		private void continueGame(int option) {
			Log.d(TAG, " continue: clicked on " + option);
			Intent in = new Intent(activity, Game.class);
			in.putExtra(Game.KEY_DIFFICULTY, option);
			in.putExtra(Game.KEY_CONTINUE, true);
			activity.startActivity(in);
		}
	};
	
	
}	
