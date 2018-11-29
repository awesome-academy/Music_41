package com.cris.nvh.framgiaproject.screen.home;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.MiniMediaPlayer;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.GenreAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksSlideAdapter;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.screen.genres.GenresActivity;
import com.cris.nvh.framgiaproject.screen.playing.PlayActivity;
import com.cris.nvh.framgiaproject.screen.search.SearchActivity;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.cris.nvh.framgiaproject.MainActivity.getMyServiceIntent;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_GENRES;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class HomeFragment extends Fragment implements GenreAdapter.GenreClickListener,
		TracksAdapter.OnClickItemTrackListener, View.OnClickListener {
	public static final String EXTRA_SEARCH =
			"com.cris.nvh.framgiaproject.screen.home.EXTRA_SEARCH";
	private static final int ALL_MUSIC_INDEX = 0;
	private static final int DURATION = 8000;
	private static final int SIZE_BOUND = 10;
	private static final int FIRST_PAGE = 0;
	private static final int PLUS = 1;
	private PlayMusicService mService;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private RecyclerView mRecyclerView;
	private ImageView mImageSearch;
	private EditText mEditSearch;
	private View mViewMiniMediaPlayer;
	private MiniMediaPlayer mMiniMediaPlayer;
	private ArrayList<Genre> mGenres;
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
			mService = binder.getService();
			mMiniMediaPlayer.setService(mService);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			getActivity().unbindService(mConnection);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater
				.from(container.getContext())
				.inflate(R.layout.fragment_home, container, false);
		initView(view);
		getActivity().bindService(getMyServiceIntent(getActivity()),
				mConnection, Context.BIND_AUTO_CREATE);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_search:
				startActivity(HomeFragment
						.getSearchActivityIntent(getActivity(), mEditSearch.getText().toString()));
			default:
				break;
		}
	}

	@Override
	public void onItemClick(Genre genre) {
		startActivity(HomeFragment.getGenresActivityIntent(getActivity(), genre));
	}

	@Override
	public void clickItemTrackListener(int position) {
		startActivity(HomeFragment.getPlayActivityIntent(getActivity()));
	}

	@Override
	public void showDialogFeatureTrack(int position) {
	}

	public static HomeFragment newInstance() {
		HomeFragment homeFragment = new HomeFragment();
		return homeFragment;
	}

	public static Intent getPlayActivityIntent(Context context) {
		Intent intent = new Intent(context, PlayActivity.class);
		return intent;
	}

	public static Intent getSearchActivityIntent(Context context, String value) {
		Intent intent = new Intent(context, SearchActivity.class);
		intent.putExtra(EXTRA_SEARCH, value);
		return intent;
	}

	public static Intent getGenresActivityIntent(Context context, Genre genre) {
		Intent intent = new Intent(context, GenresActivity.class);
		intent.putExtra(EXTRA_GENRES, genre);
		return intent;
	}

	private void initView(View view) {
		mViewPager = view.findViewById(R.id.pager_images);
		mTabLayout = view.findViewById(R.id.indicator);
		mRecyclerView = view.findViewById(R.id.recycler_genres);
		mImageSearch = view.findViewById(R.id.image_search);
		mEditSearch = view.findViewById(R.id.edit_search);
		mViewMiniMediaPlayer = view.findViewById(R.id.mini_mediaplayer);
		mMiniMediaPlayer = new MiniMediaPlayer(mViewMiniMediaPlayer);
		initImageSlide();
		initGenreAdapter();
		mImageSearch.setOnClickListener(this);
	}

	private void initImageSlide() {
		TracksSlideAdapter tracksSlideAdapter = new TracksSlideAdapter();
		mViewPager.setAdapter(tracksSlideAdapter);
		mTabLayout.setupWithViewPager(mViewPager, true);
		pageSwitcher(DURATION);
	}

	private void initGenreAdapter() {
		if (getArguments() != null) {
			mGenres = getArguments().getParcelableArrayList(EXTRA_GENRES);
			mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			GenreAdapter genreAdapter = new GenreAdapter(mGenres);
			mRecyclerView.setAdapter(genreAdapter);
			getArguments().remove(EXTRA_GENRES);
		}
	}

	private void pageSwitcher(int time) {
		Timer timer;
		timer = new Timer();
		timer.scheduleAtFixedRate(new RemindTask(), 0, time);
	}

	private class RemindTask extends TimerTask {
		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {
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
