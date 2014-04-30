package com.johnson.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class Game extends Activity {
	
	private static final String TAG = Game.class.getName();
	
	public static final String KEY_DIFFICULTY = "com.johnson.sudoku.difficulty";
	public static final String KEY_CONTINUE = "com.johnson.sudoku.continue";
	public static final String KEY_ANSWER = "com.johnson.sudoku.answer";
	
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	private int currentDifficulty = DIFFICULTY_EASY;
	
	protected Puzzle puzzle;
	
	private final String easyPuzzle =
			"360000000004230800000004200" +
			"070460003820000014500013020" +
			"001900000007048300000000045" ;
	private final String mediumPuzzle =
			"650000070000506000014000005" +
			"007009000002314700000700800" +
			"500000630000201000030000097" ;
	private final String hardPuzzle =
			"009000000080605020501078000" +
			"000000700706040102004000000" +
			"000720903090301080000000600" ;
	
	private PuzzleView puzzleView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		// keep screen high light
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// initialize puzzle
		int difficulty = super.getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		String puzStr = getPuzzleStr(difficulty);
		String answerStr = puzStr;
		if (getIntent().getBooleanExtra(KEY_CONTINUE, false)) {
			answerStr = getPreferences(MODE_PRIVATE).getString(generateAnswerKey(difficulty), puzStr);
		}
		puzzle = Prefs.isHintsOn(this) ? 
				new HintedPuzzle(puzStr, answerStr) : new Puzzle(puzStr, answerStr);
		currentDifficulty = difficulty;
		
		// set view
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.game);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		Music.stop(this);
		
		// save current puzzle
		super.getPreferences(MODE_PRIVATE)
				.edit()
				.putString(generateAnswerKey(currentDifficulty), puzzle.getAnswerStr())
				.commit();
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
	
	public void showKeypadOrError(int x, int y) {
		int[] tiles = getUsedTiles(x, y);
		if (tiles.length == 9) {
			Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Log.d(TAG, "showKeypad: used=" + Puzzle.toPuzzleString(tiles));
			Dialog v = new Keypad(this, tiles, puzzleView);
			v.show();
		}
	}
	
	public int[] getUsedTiles(int x, int y) {
		return puzzle.getUsedTiles(x, y);
	}
	
	private String getPuzzleStr(int difficulty) {
		String puz;
		switch (difficulty) {
		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICULTY_EASY:
		default:
			puz = easyPuzzle;
			break;
		}
		return puz;
	}
	
	protected String getTileString(int x, int y) {
		int v = puzzle.getTile(x, y);
		if (v == 0) {
			return "";
		} else {
			return String.valueOf(v);
		}
	}
	
	private String generateAnswerKey(int difficulty) {
		return KEY_ANSWER + '_' + difficulty;
	}
}
