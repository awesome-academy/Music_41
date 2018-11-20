package com.cris.nvh.framgiaproject.data.repository;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class TrackRepository implements TracksDataSource.Local, TracksDataSource.Remote {
	private static TrackRepository sTrackRepository;
	private TracksDataSource.Local mLocal;
	private TracksDataSource.Remote mRemote;

	public TrackRepository(TracksDataSource.Local local, TracksDataSource.Remote remote) {
		mLocal = local;
		mRemote = remote;
	}

	public static TrackRepository getInstance(TracksDataSource.Local local,
	                                          TracksDataSource.Remote remote) {
		if (sTrackRepository == null)
			sTrackRepository = new TrackRepository(local, remote);
		return sTrackRepository;
	}

	@Override
	public void getLocalTracks(TracksDataSource.LoadDataCallBack<Track> callback) {
		mLocal.getLocalTracks(callback);
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

	@Override
	public void getListGenres(String[] urls, TracksDataSource.LoadDataCallBack<Genre> callback) {
		mRemote.getListGenres(urls, callback);
	}

	@Override
	public void getOnlineTracks(Genre genre, int limit, int offset, TracksDataSource.LoadDataCallBack<Track> callback) {
	}

	@Override
	public void searchTracks(String api, TracksDataSource.LoadDataCallBack<Track> callback) {

	}

	@Override
	public void getDetailTrack(String api, TracksDataSource.LoadDataCallBack<Track> callback) {
	}
}
