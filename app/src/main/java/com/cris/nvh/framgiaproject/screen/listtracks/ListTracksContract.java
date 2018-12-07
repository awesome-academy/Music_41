package com.cris.nvh.framgiaproject.screen.listtracks;

import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class ListTracksContract {
	interface View {
		void onLoadTracksSuccess(List<Track> tracks);

		void onLoadTracksFail(String message);
	}

	interface Presenter {
		void getTracks(String api);
	}
}
