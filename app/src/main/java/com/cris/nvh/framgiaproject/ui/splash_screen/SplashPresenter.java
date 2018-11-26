package com.cris.nvh.framgiaproject.ui.splash_screen;

import android.content.Context;

import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SplashPresenter implements SplashContract.Presenter {
	private SplashContract.View mView;
	private TrackRepository mTrackRepository;

	public SplashPresenter(SplashContract.View view) {
		mView = view;
	}

	@Override
	public void initRepository(TrackRepository trackRepository) {
		mTrackRepository = trackRepository;
	}

	@Override
	public void loadApiData(String[] urls) {
		mTrackRepository.getListGenres(urls, new TracksDataSource.LoadDataCallBack<Genre>() {
			@Override
			public void onSuccess(List<Genre> genres) {
				mView.onloadApiSuccess((ArrayList<Genre>) genres);
			}
			@Override
			public void onFail(String msg) {
				mView.onLoadApiFailed(msg);
			}
		});
	}

	public void loadLocalTracks(Context context) {
		mTrackRepository.getLocalTracks(new TracksDataSource.LoadDataCallBack<Track>() {
			@Override
			public void onSuccess(List<Track> tracks) {
				mView.onLoadLocalTrackSuccess((ArrayList<Track>) tracks);
			}

			@Override
			public void onFail(String msg) {
				mView.onLoadLocalTrackFailed(msg);
			}
		});
	}
}
