package com.cris.nvh.framgiaproject.data.source.remote;

import android.content.Context;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

public class TracksRemoteDataSource implements TracksDataSource.Remote {
	private static TracksRemoteDataSource sRemoteTask;
	private Context mContext;

	public TracksRemoteDataSource(Context context) {
		mContext = context;
	}

	@Override
	public void getListGenres(String[] urls, TracksDataSource.LoadDataCallBack<Genre> callback) {
		new LoadTracksAsyncTask(mContext, callback).execute(urls);
	}

	@Override
	public void getOnlineTracks(Genre genre, int limit, int offSet,
	                            TracksDataSource.LoadDataCallBack<Track> callback) {

	}

	@Override
	public void searchTracks(String api, TracksDataSource.LoadDataCallBack<Track> callback) {

	}

	@Override
	public void getDetailTrack(String api, TracksDataSource.LoadDataCallBack<Track> callback) {

	}

	public static TracksRemoteDataSource getInstance(Context context) {
		if (sRemoteTask == null)
			sRemoteTask = new TracksRemoteDataSource(context);
		return sRemoteTask;
	}
}
