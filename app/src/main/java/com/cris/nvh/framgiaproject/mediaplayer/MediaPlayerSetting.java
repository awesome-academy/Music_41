package com.cris.nvh.framgiaproject.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class MediaPlayerSetting {
	private int mLoopType;
	private int mShuffleType;

	public int getShuffleType() {
		return mShuffleType;
	}

	public MediaPlayerSetting setShuffleType(int shuffleType) {
		mShuffleType = shuffleType;
		return this;
	}

	public int getLoopType() {
		return mLoopType;
	}

	public void setLoopType(int loopType) {
		mLoopType = loopType;
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({LoopType.NONE, LoopType.ONE, LoopType.ALL})
	public @interface LoopType {
		int NONE = 0;
		int ONE = 1;
		int ALL = 2;
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({ShuffleType.ON, ShuffleType.OFF})
	public @interface ShuffleType {
		int OFF = 0;
		int ON = 1;
	}
}
