package com.cris.nvh.framgiaproject.data.model;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class Genre {
	private List<Track> mTracks;

	public Genre(){}

	public List<Track> getTracks() {
		return mTracks;
	}

	public void setTracks(List<Track> tracks) {
		mTracks = tracks;
	}
}
