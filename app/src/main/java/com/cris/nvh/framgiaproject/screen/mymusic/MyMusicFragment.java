package com.cris.nvh.framgiaproject.screen.mymusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.LibraryAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.screen.home.HomeFragment;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.List;

import static com.cris.nvh.framgiaproject.screen.listtracks.ListTracksActivity.getListTracksActivityIntent;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.LOCAL;
import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.getPlayActivityIntent;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_TRACKS;
import static com.cris.nvh.framgiaproject.service.PlayMusicService.getMyServiceIntent;


public class MyMusicFragment extends Fragment implements View.OnClickListener,
	LibraryAdapter.OnClickItemListener, TracksAdapter.OnClickItemTrackListener {
	private static final int DEFAULT_TOTAL_SONGS = 0;
	private static final String TAG = "DIALOG";
	private static String[] sOptions = {"Delete", "Add to favorite"};
	private RecyclerView mRecyclerViewLibrary;
	private RecyclerView mRecyclerViewRecent;
	private EditText mEditSearch;
	private ImageView mButtonSearch;
	private ServiceConnection mConnection;
	private List<Track> mTracks;
	private PlayMusicService mService;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater
			.from(container.getContext())
			.inflate(R.layout.fragment_my_music, container, false);
		initView(view);
		bindToService();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unbindService(mConnection);
	}

	@Override
	public void clickItemTrackListener(int position) {
		mService.setTracks(mTracks);
		mService.createTrack(position);
		startActivity(getPlayActivityIntent(getActivity()));
	}

	@Override
	public void showDialogFeatureTrack(int position) {
	}

	@Override
	public void clickItem(int position) {
		startActivity(getListTracksActivityIntent(getActivity(), getLocalTracks(), LOCAL));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_search:
				startActivity(HomeFragment
					.getSearchActivityIntent(getActivity(), mEditSearch.getText().toString()));
				break;
			default:
				break;
		}
	}

	public static MyMusicFragment newInstance() {
		MyMusicFragment myMusicFragment = new MyMusicFragment();
		return myMusicFragment;
	}

	private void bindToService() {
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
				mService = binder.getService();
				mService.setTracks(getLocalTracks());
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				getActivity().unbindService(mConnection);
			}
		};
		getActivity().bindService(getMyServiceIntent(getActivity()),
			mConnection, Context.BIND_AUTO_CREATE);
	}

	private void initView(View view) {
		mTracks = getLocalTracks();
		mRecyclerViewLibrary = view.findViewById(R.id.recycler_library);
		mRecyclerViewRecent = view.findViewById(R.id.recycler_recent);
		mButtonSearch = view.findViewById(R.id.image_search);
		mEditSearch = view.findViewById(R.id.edit_search);
		mRecyclerViewRecent.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerViewRecent.setAdapter(new TracksAdapter(mTracks, this));
		mRecyclerViewLibrary.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerViewLibrary.setAdapter(new LibraryAdapter(new int[]{mTracks.size(),
			DEFAULT_TOTAL_SONGS, DEFAULT_TOTAL_SONGS}, this));
		mButtonSearch.setOnClickListener(this);
	}

	private List<Track> getLocalTracks() {
		if (getArguments() != null) {
			List<Track> tracks = getArguments().getParcelableArrayList(EXTRA_TRACKS);
			return tracks;
		}
		return null;
	}
}
