package com.cris.nvh.framgiaproject.data.source;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public interface TracksDataSource {
	interface LoadDataCallBack<T> {
		void onSuccess(List<T> datas);

		void onFail(String msg);
	}

	interface Local {
		void getLocalTracks(LoadDataCallBack<Track> callback);

		void getFavotiteTracks(LoadDataCallBack<Track> callback);

		void addFavoriteTrack(Track track, LoadDataCallBack<Boolean> callback);

		void deleteFavoriteTrack(Track track, LoadDataCallBack<Boolean> callback);

		void getRecentTrack(LoadDataCallBack<Long> callback);
	}

	interface Remote {
		void getListGenres(String[] urls, LoadDataCallBack<Genre> callback);

		void getOnlineTracks(Genre genre, int limit, int offset, LoadDataCallBack<Track> callback);

		void searchTracks(String api, LoadDataCallBack<Track> callback);

		void getDetailTrack(String api, LoadDataCallBack<Track> callback);
	}
}
