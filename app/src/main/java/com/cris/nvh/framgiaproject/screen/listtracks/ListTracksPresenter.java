package com.cris.nvh.framgiaproject.screen.listtracks;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class ListTracksPresenter implements ListTracksContract.Presenter {
	private ListTracksContract.View mView;
	private TrackRepository mRepository;

	public ListTracksPresenter(TrackRepository repository, ListTracksContract.View view) {
		mView = view;
		mRepository = repository;
	}

	@Override
	public void getTracks(String url) {
		String[] urls = {url, null};
		mRepository.getListGenres(urls, new TracksDataSource.LoadDataCallBack<Genre>() {
			@Override
			public void onSuccess(List<Genre> genres) {
				mView.onLoadTracksSuccess(genres.get(0).getTracks());
			}

			@Override
			public void onFail(String msg) {
				mView.onLoadTracksFail(msg);
			}
		});
	}
}
