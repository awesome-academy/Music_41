package com.cris.nvh.framgiaproject.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface PlayMusic {
	void create(int index);

	void prepare();

	void start();

	void pause();

	void stop();

	int getDuration();

	int getCurrrentPosition();

	boolean isPlaying();

	void seek(int possition);

	void loop(boolean isLoop);

	int getTrack();

	void next();

	void previous();

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({StatusPlayerType.IDLE, StatusPlayerType.INITIALIZED, StatusPlayerType.PREPARING,
			StatusPlayerType.STARTED, StatusPlayerType.PAUSED, StatusPlayerType.STOPPED,
			StatusPlayerType.END, StatusPlayerType.PLAYBACK_COMPLETED})
	@interface StatusPlayerType {
		int IDLE = 0;
		int INITIALIZED = 1;
		int PREPARING = 2;
		int STARTED = 3;
		int PAUSED = 4;
		int STOPPED = 5;
		int END = 6;
		int PLAYBACK_COMPLETED = 7;
	}
}
