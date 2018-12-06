package com.cris.nvh.framgiaproject.screen.search;

import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SearchPresenter implements SearchContract.Presenter {
	private SearchContract.View mView;
	private TrackRepository mRepository;

	public SearchPresenter(TrackRepository repository, SearchContract.View view) {
		mView = view;
		mRepository = repository;
	}

	@Override
	public void searchTracks(String api) {
		mRepository.searchTracks(api, new TracksDataSource.LoadDataCallBack<Track>() {
			public void onSuccess(List<Track> tracks) {
				mView.showResult(tracks);
			}

			public void onFail(String msg) {
				mView.showNoResult(msg);
			}
		});
	}
}
