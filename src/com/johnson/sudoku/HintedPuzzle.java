package com.johnson.sudoku;

public class HintedPuzzle extends Puzzle {
	
	private static final String TAG = HintedPuzzle.class.getName();
	
	private int[][][] usedTiles;
	
	public HintedPuzzle(String puzzle, String answer) {
		super(puzzle, answer);
	}
	
	@Override
	public int[] getUsedTiles(int x, int y) {
		if (x<0 || x>9 || y<0 || y>9) {
			throw new IllegalArgumentException(TAG + " getUsedTiles: " + x + ", " + y);
		}
		return usedTiles[x][y];
	}
	
	@Override
	protected void initPuzzle(String puzzleStr, String answerStr) {
		super.initPuzzle(puzzleStr, answerStr);
		usedTiles = new int[9][9][];
		calculateUsedTiles();
	}
	
	@Override
	public boolean setTileIfValid(int x, int y, int tile) {
		int[] tiles = calculateUsedTiles(x, y);
		if (tile != 0) {
			for (int t : tiles) {
				if (t == tile) {
					return false;
				}
			}
		}
		if (super.setTileIfValid(x, y, tile)) {
			calculateUsedTiles();
			return true;
		} else {
			return false;
		}
	}
	
	private void calculateUsedTiles() {
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				usedTiles[i][j] = calculateUsedTiles(i, j);
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
			for (int j=starty; j<starty+3; j++) {
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
}
