package com.cris.nvh.framgiaproject.mediaplayer;

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
}
