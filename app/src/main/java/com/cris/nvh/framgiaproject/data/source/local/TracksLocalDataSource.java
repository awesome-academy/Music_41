package com.cris.nvh.framgiaproject.data.source.local;

import android.content.Context;

import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

public class TracksLocalDataSource implements TracksDataSource.Local {
	private static TracksLocalDataSource sTracksLocalDataSource;
	private LocalTracksManager mLocalTracksManager;
	private FavoriteDbManager mFavoriteDbManager;
	private Context mContext;

	public TracksLocalDataSource(Context context) {
		mLocalTracksManager = LocalTracksManager.getInstance(context);
		mFavoriteDbManager = new FavoriteDbManager(context);
		mContext = context;
	}

	@Override
	public void getLocalTracks(TracksDataSource.LoadDataCallBack<Track> callback) {
		mLocalTracksManager.getLocalTrackDetails(callback);
	}

	@Override
	public void getFavoriteTracks(TracksDataSource.LoadDataCallBack<Track> callback) {
		mFavoriteDbManager.getTracks(callback);
	}

	@Override
	public void addFavoriteTrack(Track track, TracksDataSource.LoadDataCallBack<String> callback) {
		mFavoriteDbManager.insertTrack(track, callback);
	}

	@Override
	public boolean isAddedToFavorite(Track track) {
		return mFavoriteDbManager.isFavoriteTrack(track);
	}

	@Override
	public void deleteFavoriteTrack(Track track, TracksDataSource.LoadDataCallBack<String> callback) {
		mFavoriteDbManager.deleteTrack(track, callback);
	}

	@Override
	public void getRecentTrack(TracksDataSource.LoadDataCallBack<Track> callback) {
	}

	public static TracksLocalDataSource getInstance(Context context) {
		if (sTracksLocalDataSource == null)
			sTracksLocalDataSource = new TracksLocalDataSource(context);
		return sTracksLocalDataSource;
	}
}
