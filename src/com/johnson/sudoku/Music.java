package com.johnson.sudoku;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	
	private static MediaPlayer player = null;
	
	public static void play(Context context, int resource) {
		stop(context);
		if (Prefs.isMusicOn(context)) {
			player = MediaPlayer.create(context, resource);
			player.setLooping(true);
			player.start();
		}
	}
	
	public static void stop(Context context) {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
	}

}
