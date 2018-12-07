package com.cris.nvh.framgiaproject.screen.search;

import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SearchContract {
	interface Presenter {
		void searchTracks(String api);
	}

	interface View {
		void showResult(List<Track> tracks);

		void showNoResult(String message);
	}
}
