package com.cris.nvh.framgiaproject.screen.favorite;

import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class FavoriteContract {
	interface View {
		void onGetTracksSuccess(List<Track> tracks);

		void onFail(String message);

		void onDeleteTracksSuccess(String message);
	}

	interface Presenter {
		void getFavotiteTracks();

		void deleteFavoriteTrack(Track track);
	}
}
