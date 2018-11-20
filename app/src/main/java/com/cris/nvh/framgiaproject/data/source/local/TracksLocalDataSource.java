package com.cris.nvh.framgiaproject.data.source.local;

import android.content.Context;

import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class TracksLocalDataSource implements TracksDataSource.Local {
	private static TracksLocalDataSource sTracksLocalDataSource;
	private LocalTracksManager mLocalTracksManager;

	public TracksLocalDataSource(Context context) {
		mLocalTracksManager = LocalTracksManager.getInstance(context);
	}

	@Override
	public void getLocalTracks(TracksDataSource.LoadDataCallBack<Track> callback) {
		mLocalTracksManager.getLocalTrackDetails(callback);
	}

	@Override
	public void getFavotiteTracks(TracksDataSource.LoadDataCallBack<Track> callback) {

	}

	@Override
	public void addFavoriteTrack(Track track, TracksDataSource.LoadDataCallBack<Boolean> callback) {

	}

	@Override
	public void deleteFavoriteTrack(Track track, TracksDataSource.LoadDataCallBack<Boolean> callback) {

	}

	@Override
	public void getRecentTrack(TracksDataSource.LoadDataCallBack<Long> callback) {

	}

	public static TracksLocalDataSource getInstance(Context context) {
		if (sTracksLocalDataSource == null)
			sTracksLocalDataSource = new TracksLocalDataSource(context);
		return sTracksLocalDataSource;
	}
}
