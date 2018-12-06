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
		new LoadTracksAsyncTask(callback).execute(urls);
	}

	@Override
	public void getOnlineTracks(String url, TracksDataSource.LoadDataCallBack<Track> callback) {
	}

	@Override
	public void searchTracks(String api, TracksDataSource.LoadDataCallBack<Track> callback) {
		new SearchAsyncTask(callback).execute(api);
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
