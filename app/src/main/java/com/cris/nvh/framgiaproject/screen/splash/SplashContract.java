package com.cris.nvh.framgiaproject.screen.splash;

import android.content.Context;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public interface SplashContract {
	interface View {
		public void onLoadApiFailed(String message);

		public void onLoadLocalTrackFailed(String message);

		public void onloadApiSuccess(ArrayList<Genre> genres);

		public void onLoadLocalTrackSuccess(ArrayList<Track> tracks);
	}

	interface Presenter {
		public void initRepository(TrackRepository trackRepository);

		public void loadApiData(String[] urls);

		public void loadLocalTracks(Context context);
	}
}
