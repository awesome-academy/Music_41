package com.cris.nvh.framgiaproject.screen.playing;

import com.cris.nvh.framgiaproject.data.model.Track;


public class PlayContract {
	interface View {
		void onFail(String message);

		void onAddTracksSuccess(String results);
	}

	interface Presenter {
		void addFavoriteTracks(Track track);

		boolean isFavoriteTrack(Track track);

		void deleteFavoriteTrack(Track track);
	}
}
