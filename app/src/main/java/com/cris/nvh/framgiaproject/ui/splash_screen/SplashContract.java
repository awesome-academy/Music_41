package com.cris.nvh.framgiaproject.splash_screen;

import android.content.Context;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public interface SplashContract {
	interface View{
		public void onLoadApiFailed(String message);

		public void onLoadLocalSongFailed(String message);

		public void onloadApiSuccess(ArrayList<Genre> genres);

		public void onLoadLocalSongSuccess(ArrayList<Track> tracks);
	}

	interface Presenter{
		public void loadApiData(String[] urls);

		public void loadLocalSongs(Context context);
	}
}
