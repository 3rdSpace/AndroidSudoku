package com.johnson.sudoku;

public class Puzzle {
	
	private static final String TAG = Puzzle.class.getName();

	private final int[][] originPuzzle = new int[9][9];
	private final int[][] answer = new int[9][9];
	
	public Puzzle(String puzzle) {
		initPuzzle(puzzle, puzzle);
	}
	
	public Puzzle(String puzzle, String answer) {
		initPuzzle(puzzle, answer);
	}
	
	public static String toPuzzleString(int[] tiles) {
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
	
	protected void initPuzzle(String puzzleStr, String answerStr) {
		if (!isValidPuzzleStr(puzzleStr) || !isValidPuzzleStr(answerStr)) {
			throw new IllegalArgumentException("Invalid puzzle string: "
						+ puzzleStr + "|" + answerStr);
		}
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				int idx = i * 9 + j;
				this.originPuzzle[i][j] = puzzleStr.charAt(idx) - '0';
				this.answer[i][j] = this.originPuzzle[i][j] > 0 ? 
						this.originPuzzle[i][j] : (answerStr.charAt(idx) - '0');
			}
		}
	}
	
	private boolean isValidPuzzleStr(String puzzle) {
		if (puzzle == null || puzzle.length() != 9*9) {
			return false;
		}
		for (int i=0, len=puzzle.length(); i<len; i++) {
			char ch = puzzle.charAt(i);
			if (ch < '0' || ch > '9') {
				return false;
			}
		}
		return true;
	}
	
	public static String matrixToString(int[][] puz) {
		StringBuilder buf = new StringBuilder();
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				buf.append(puz[i][j]);
			}
		}
		return buf.toString();
	}
	
	public String getPuzzleStr() {
		return matrixToString(originPuzzle);
	}
	
	public String getAnswerStr() {
		return matrixToString(answer);
	}	
	
	private static final int[] DEFAULT_USED_TILES = {}; 
	
	public int[] getUsedTiles(int x, int y) {
		if (x<0 || x>9 || y<0 || y>9) {
			throw new IllegalArgumentException(TAG + " getUsedTiles: " + x + ", " + y);
		}
		return DEFAULT_USED_TILES;
	}
	
	public int getTile(int x, int y) {
		if (originPuzzle[x][y] > 0) {
			return originPuzzle[x][y];
		}
		return answer[x][y];
	}
	
	public boolean setTile(int x, int y, int tile) {
		if (!isImmutableTile(x, y)) {
			answer[x][y] = tile;
			return true;
		}
		return false;
	}
	
	public boolean isImmutableTile(int x, int y) {
		return this.originPuzzle[x][y] > 0;
	}
	
	public boolean setTileIfValid(int x, int y, int value) {
		return setTile(x, y, value);
	}
}
