package com.johnson.sudoku;

import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Game extends Activity {
	
	private static final String TAG = Game.class.getName();
	
	public static final String KEY_DIFFICULTY = "com.johnson.sudoku.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	private int currentDifficulty = DIFFICULTY_EASY;
	private int[][] originPuzzle = new int[9][];
	
	private int puzzle[][] = new int[9][9];
	private final int used[][][] = new int[9][9][];
	
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
		Log.d(Game.class.getName(), "onCreate");
		
		int difficulty = super.getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(difficulty);
		// record
		this.currentDifficulty = difficulty;
		this.originPuzzle = getPuzzle(difficulty);
		// calculate
		calculateUsedTiles();
		
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}
	
	public void showKeypadOrError(int x, int y) {
		int[] tiles = getUsedTiles(x, y);
		if (tiles.length == 9) {
			Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
			Dialog v = new Keypad(this, tiles, puzzleView);
			v.show();
		}
	}
	
	private String toPuzzleString(int[] tiles) {
		StringBuilder str = new StringBuilder();
		str.append("{");
		for (int tile : tiles) {
			str.append(tile).append(',');
		}
		if (tiles.length > 0) {
			str.setCharAt(str.length() - 1, '}');
		} else {
			str.append('}');
		}
		return str.toString();
	}
	
	public boolean setTileIfValid(int x, int y, int value) {
		int[] tiles = getUsedTiles(x, y);
		if (value != 0) {
			for (int tile : tiles) {
				if (tile == value) {
					return false;
				}
			}
		}
		setTile(x, y, value);
		calculateUsedTiles();
		return true;
	}
	
	public int[] getUsedTiles(int x, int y) {
		return used[x][y];
	}
	
	private void calculateUsedTiles() {
		for (int x=0; x<9; x++) {
			for (int y=0; y<9; y++) {
				used[x][y] = calculateUsedTiles(x, y);
			}
		}
	}
	
	private int[] calculateUsedTiles(int x, int y) {
		int[] c = new int[9];
		// horizontal
		for (int i=0; i<9; i++) {
			if (i == y) {
				continue;
			}
			int t = getTile(x, i);
			if (t != 0) {
				c[t - 1] = t;
			}
		}
		// vertical
		for (int i=0; i<9; i++) {
			if (i == x) {
				continue;
			}
			int t = getTile(i, y);
			if (t != 0) {
				c[t - 1] = t;
			}
		}
		// same cell block
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;
		for (int i=startx; i<startx+3; i++) {
			for (int j=starty; y<starty+3; y++) {
				if (i == x && j == y) {
					continue;
				}
				int t = getTile(i, j);
				if (t != 0) {
					c[t - 1] = t;
				}
			}
		}
		// compress
		int nused = 0;
		for (int t : c) {
			if (t != 0) {
				nused++;
			}
		}
		int[] c1 = new int[nused];
		nused = 0;
		for (int t: c) {
			if (t != 0) {
				c1[nused++] = t;
			}
		}
		return c1;
	}
	
	private int[][] getPuzzle(int difficulty) {
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
		return fromPuzzleString(puz);
	}
	
	protected boolean isImmutableTile(int x, int y) {
		return originPuzzle[x][y] != 0;
	}
	
	private static String toPuzzleString(int[][] puz) {
		StringBuilder buf = new StringBuilder();
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				buf.append(puz[i][j]);
			}
		}
		return buf.toString();
	}
	
	private static int[][] fromPuzzleString(String string) {
		int[][] puz = new int[9][9];
		for (int i=0; i<string.length(); i++) {
			puz[i%9][i/9] = string.charAt(i) - '0';
		}
		return puz;
	}
	
	private int getTile(int x, int y) {
		return puzzle[x][y];
	}
	
	private void setTile(int x, int y, int value) {
		puzzle[x][y] = value;
	}
	
	protected String getTileString(int x, int y) {
		int v = getTile(x, y);
		if (v == 0) {
			return "";
		} else {
			return String.valueOf(v);
		}
	}
}
