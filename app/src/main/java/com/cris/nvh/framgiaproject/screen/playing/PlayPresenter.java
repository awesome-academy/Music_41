package com.cris.nvh.framgiaproject.screen.playing;

import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class PlayPresenter implements PlayContract.Presenter {
	private PlayContract.View mView;
	private TrackRepository mRepository;

	public PlayPresenter(TrackRepository repository, PlayContract.View view) {
		mView = view;
		mRepository = repository;
	}

	@Override
	public void addFavoriteTracks(Track track) {
		mRepository.addFavoriteTrack(track, new TracksDataSource.LoadDataCallBack<String>() {
			@Override
			public void onSuccess(List<String> results) {
				mView.onAddTracksSuccess(results.get(0));
			}

			@Override
			public void onFail(String msg) {
				mView.onFail(msg);
			}
		});
	}

	@Override
	public boolean isFavoriteTrack(Track track) {
		return mRepository.isAddedToFavorite(track);
	}

	@Override
	public void deleteFavoriteTrack(Track track) {
		mRepository.deleteFavoriteTrack(track, new TracksDataSource.LoadDataCallBack<String>() {
			@Override
			public void onSuccess(List<String> results) {
				mView.onAddTracksSuccess(results.get(0));
			}

			@Override
			public void onFail(String msg) {
				mView.onFail(msg);
			}
		});
	}
}
