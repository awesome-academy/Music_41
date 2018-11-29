package com.cris.nvh.framgiaproject.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

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

	void changeTrack();
}
