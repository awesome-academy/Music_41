package com.cris.nvh.framgiaproject.screen.favorite;

import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class FavoritePresenter implements FavoriteContract.Presenter {
	private FavoriteContract.View mView;
	private TrackRepository mRepository;

	public FavoritePresenter(TrackRepository repository, FavoriteContract.View view) {
		mView = view;
		mRepository = repository;
	}

	@Override
	public void getFavotiteTracks() {
		mRepository.getFavoriteTracks(new TracksDataSource.LoadDataCallBack<Track>() {
			@Override
			public void onSuccess(List<Track> datas) {
				mView.onGetTracksSuccess(datas);
			}

			@Override
			public void onFail(String mesage) {
				mView.onFail(mesage);
			}
		});
	}

	@Override
	public void deleteFavoriteTrack(Track track) {
		mRepository.deleteFavoriteTrack(track, new TracksDataSource.LoadDataCallBack<String>() {
			@Override
			public void onSuccess(List<String > datas) {
				mView.onDeleteTracksSuccess(datas.get(0));
			}

			@Override
			public void onFail(String msg) {
				mView.onFail(msg);
			}
		});
	}
}
