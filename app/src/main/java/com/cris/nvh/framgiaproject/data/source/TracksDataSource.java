package com.cris.nvh.framgiaproject.data.source;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.List;

public interface TracksDataSource {
	interface LoadDataCallBack<T> {
		void onSuccess(List<T> datas);

		void onFail(String msg);
	}

	interface Local {
		void getLocalTracks(LoadDataCallBack<Track> callback);

		void getFavoriteTracks(LoadDataCallBack<Track> callback);

		boolean isAddedToFavorite(Track track);

		void addFavoriteTrack(Track track, LoadDataCallBack<String> callback);

		void deleteFavoriteTrack(Track track, LoadDataCallBack<String> callback);

		void getRecentTrack(LoadDataCallBack<Track> callback);
	}

	interface Remote {
		void getListGenres(String[] urls, LoadDataCallBack<Genre> callback);

		void getOnlineTracks(String url, LoadDataCallBack<Track> callback);

		void searchTracks(String api, LoadDataCallBack<Track> callback);

		void getDetailTrack(String api, LoadDataCallBack<Track> callback);
	}
}
