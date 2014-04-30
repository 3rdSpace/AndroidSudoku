package com.johnson.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

public class PuzzleView extends View {
	
	private static final String TAG = PuzzleView.class.getName();
	
	private final Game game;
	private final Puzzle puzzle;

	
	private float width;	// width of one tile
	private float height;	// height of one tile
	float startX; // offset x
	float startY; // offset y
	private int selX;		// X index of selection
	private int selY;		// Y index of selection
	private final Rect selRect = new Rect();
	
	public PuzzleView(Context context) {
		super(context);
		this.game = (Game) context;
		this.puzzle = game.puzzle;
		super.setFocusableInTouchMode(true);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float l = w < h ? w : h;
		width = l / 9F;
		height = width;
		startX = (getWidth() - 9 * width) / 2;
		startY = (getHeight() - 9 * height) / 3;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width " + width + ", height " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (startX + x * width), (int) (startY + y * height), 
				(int) (startX + x * width + width), (int) (startY + y * width + width));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		// draw background
		Paint bgPaint = new Paint();
		bgPaint.setColor(super.getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, super.getWidth(), super.getHeight(), bgPaint);
		
		// draw board
		Paint dark = new Paint();
		dark.setColor(super.getResources().getColor(R.color.puzzle_dark));
		Paint hilite = new Paint();
		hilite.setColor(super.getResources().getColor(R.color.puzzle_hilite));
		Paint light = new Paint();
		light.setColor(super.getResources().getColor(R.color.puzzle_light));
		
		for (int i=0; i<10; i++) {
			// minor grid lines
			canvas.drawLine(startX, startY + i * height, startX + 9 * width, startY + i * height, light);
			canvas.drawLine(startX, startY + i * height + 1, startX + 9 * width, startY + i * height + 1, hilite);
			canvas.drawLine(startX + i * width, startY, startX + i * width, startY + 9 * height, light);
			canvas.drawLine(startX + i * width + 1, startY, startX + i * width + 1, startY + 9 * height, hilite);
			// major grid lines
			if (i % 3 == 0) {
				canvas.drawLine(startX, startY + i * height, startX + 9 * width, startY + i * height, dark);
				canvas.drawLine(startX, startY + i * height + 2, startX + 9 * width, startY + i * height + 2, hilite);
				canvas.drawLine(startX + i * width, startY, startX + i * width, startY + 9 * height, dark);
				canvas.drawLine(startX + i * width + 2, startY, startX + i * width + 2, startY + 9 * height, hilite);
			}
		}
		
		// pick a hint color based on #moves left
		if (Prefs.isHintsOn(getContext())) {
			Paint hint = new Paint();
			int[] c = {getResources().getColor(R.color.puzzle_hint_0),
					getResources().getColor(R.color.puzzle_hint_1),
					getResources().getColor(R.color.puzzle_hint_2)};
			Rect r = new Rect();
			for (int i=0; i<9; i++) {
				for (int j=0; j<9; j++) {
					int movesLeft = 9 - game.getUsedTiles(i, j).length;
					if (movesLeft < c.length) {
						getRect(i, j, r);
						hint.setColor(c[movesLeft]);
						canvas.drawRect(r, hint);
					}
				}
			}
		}
		
		// draw the selection
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected);
		
		// draw numbers
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		float x = width / 2;
		float y = height / 2 - fm.ascent / 2;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (game.getTileString(i, j).length() == 0) {
					continue;
				}
				if (puzzle.isImmutableTile(i, j)) {
					foreground.setColor(getResources().getColor(R.color.puzzle_fixed_tile));
				} else {
					foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
				}
				canvas.drawText(this.game.getTileString(i, j), 
							startX + i * width + x, startY + j * height + y, foreground);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown: keyCode=" + keyCode + ", event=" + event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX, selY - 1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX, selY + 1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1, selY);
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE:
			setSelectedTile(0); break;
		case KeyEvent.KEYCODE_1:
			setSelectedTile(1); break;
		case KeyEvent.KEYCODE_2:
			setSelectedTile(2); break;
		case KeyEvent.KEYCODE_3:
			setSelectedTile(3); break;
		case KeyEvent.KEYCODE_4:
			setSelectedTile(4); break;
		case KeyEvent.KEYCODE_5:
			setSelectedTile(5); break;
		case KeyEvent.KEYCODE_6:
			setSelectedTile(6); break;
		case KeyEvent.KEYCODE_7:
			setSelectedTile(7); break;
		case KeyEvent.KEYCODE_8:
			setSelectedTile(8); break;
		case KeyEvent.KEYCODE_9:
			setSelectedTile(9); break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeypadOrError(selX, selY);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}
		float x = event.getX() - startX;
		float y = event.getY() - startY;
		if (x < 0 || x > 9*width || y < 0 || y > 9*height) {
			return super.onTouchEvent(event);
		}
		int mappedX = (int) (x / width);
		int mappedY = (int) (y / height);
		if (puzzle.isImmutableTile(mappedX, mappedY)) {
			return super.onTouchEvent(event);
		}
		select(mappedX, mappedY);
		game.showKeypadOrError(selX, selY);
		Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
		return true;
	}
	
	private void select(int x, int y) {
		invalidate(selRect);
		this.selX = Math.min(Math.max(x, 0), 8);
		this.selY = Math.min(Math.max(y, 0), 8);
		getRect(this.selX, this.selY, selRect);
		invalidate(selRect);
	}
	
	protected void setSelectedTile(int tile) {
		if (puzzle.setTileIfValid(selX, selY, tile)) {
			invalidate();
		} else {
			Log.d(TAG, "setSelectedTile: invalid: " + tile);
			startAnimation(AnimationUtils.loadAnimation(game,  R.anim.shake));
		}
	}
}
