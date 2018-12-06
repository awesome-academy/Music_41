package com.cris.nvh.framgiaproject.screen.home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.MiniMediaPlayer;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.GenreAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksSlideAdapter;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.screen.search.SearchActivity;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cris.nvh.framgiaproject.screen.favorite.FavoriteActivity.getFavoriteActivityIntent;
import static com.cris.nvh.framgiaproject.screen.listtracks.ListTracksActivity.getListTracksActivityIntent;
import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.getPlayActivityIntent;
import static com.cris.nvh.framgiaproject.screen.search.SearchActivity.getSearchActivityIntent;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_GENRES;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_TRACKS;
import static com.cris.nvh.framgiaproject.service.PlayMusicService.getMyServiceIntent;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class HomeFragment extends Fragment implements
	GenreAdapter.GenreClickListener, View.OnClickListener,
	TracksAdapter.OnClickItemTrackListener {
	public static final String EXTRA_SEARCH =
		"com.cris.nvh.framgiaproject.screen.home.EXTRA_SEARCH";
	private static final int DURATION = 8000;
	private static final int FIRST_PAGE = 0;
	private static final int PLUS = 1;
	public static MiniMediaPlayer sMiniMediaPlayer;
	private PlayMusicService mService;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private RecyclerView mRecyclerView;
	private ImageView mImageSearch;
	private EditText mEditSearch;
	private List<Genre> mGenres;
	private List<Track> mTracks;
	private ServiceConnection mConnection;
	private Activity mActivity;
	private TracksAdapter mAdapter;
	private View mViewMiniMediaPlayer;
	private Button mButtonFavorite;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater
			.from(container.getContext())
			.inflate(R.layout.fragment_home, container, false);
		initView(view);
		bindToService();
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unbindService(mConnection);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_search:
				startActivity(getSearchActivityIntent(getActivity(),
					mEditSearch.getText().toString()));
			case R.id.button_favorite:
				startActivity(getFavoriteActivityIntent(getActivity()));
			default:
				break;
		}
	}

	@Override
	public void onItemClick(int index) {
		mService.setTracks(mGenres.get(index).getTracks());
		startActivity(getListTracksActivityIntent(getActivity(),
			mGenres.get(index).getTracks(), index));
	}

	@Override
	public void onTrackClick(int genreIndex, int trackIndex) {
		mService.setTracks(mGenres.get(genreIndex).getTracks());
		mService.createTrack(trackIndex);
		mAdapter.setRecentTracks(true);
		startActivity(getPlayActivityIntent(getActivity()));
	}

	@Override
	public void clickItemTrackListener(int position) {
	}

	@Override
	public void showDialogFeatureTrack(int position) {
	}

	@Override
	public void deleteFromFavorite(int position) {
	}

	public static HomeFragment newInstance() {
		HomeFragment homeFragment = new HomeFragment();
		return homeFragment;
	}

	private void initView(View view) {
		mButtonFavorite = view.findViewById(R.id.button_favorite);
		mViewPager = view.findViewById(R.id.pager_images);
		mTabLayout = view.findViewById(R.id.indicator);
		mRecyclerView = view.findViewById(R.id.recycler_genres);
		mImageSearch = view.findViewById(R.id.image_search);
		mEditSearch = view.findViewById(R.id.edit_search);
		mViewMiniMediaPlayer = view.findViewById(R.id.mini_mediaplayer);
		sMiniMediaPlayer = new MiniMediaPlayer(mViewMiniMediaPlayer);
		mAdapter = new TracksAdapter(this);
		initImageSlide();
		initData();
		mImageSearch.setOnClickListener(this);
		mButtonFavorite.setOnClickListener(this);
	}

	private void initImageSlide() {
		TracksSlideAdapter tracksSlideAdapter = new TracksSlideAdapter();
		mViewPager.setAdapter(tracksSlideAdapter);
		mTabLayout.setupWithViewPager(mViewPager, true);
		pageSwitcher(DURATION);
	}

	private void initData() {
		if (getArguments() != null) {
			if (mTracks == null)
				mTracks = getLocalTracks();
			initGenreAdapter();
		}
	}

	private void initGenreAdapter() {
		mGenres = getArguments().getParcelableArrayList(EXTRA_GENRES);
		if (mGenres != null) {
			mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			GenreAdapter genreAdapter = new GenreAdapter(mGenres, this);
			mRecyclerView.setAdapter(genreAdapter);
		}
	}

	private List<Track> getLocalTracks() {
		return getArguments().getParcelableArrayList(EXTRA_TRACKS);
	}

	private void pageSwitcher(int time) {
		Timer timer;
		timer = new Timer();
		timer.scheduleAtFixedRate(new RemindTask(), 0, time);
	}

	private void bindToService() {
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
				mService = binder.getService();
				sMiniMediaPlayer.setService(mService);
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				getActivity().unbindService(mConnection);
			}
		};
		getActivity().bindService(getMyServiceIntent(getActivity()),
			mConnection, Context.BIND_AUTO_CREATE);
	}

	private class RemindTask extends TimerTask {
		@Override
		public void run() {
			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					if (mViewPager.getCurrentItem() == mViewPager.getChildCount()) {
						mViewPager.setCurrentItem(FIRST_PAGE);
						return;
					}
					mViewPager.setCurrentItem(mViewPager.getCurrentItem() + PLUS);
				}
			});
		}
	}
}
