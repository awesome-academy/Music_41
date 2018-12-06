package com.cris.nvh.framgiaproject.screen.favorite;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.local.TracksLocalDataSource;
import com.cris.nvh.framgiaproject.data.source.remote.TracksRemoteDataSource;
import com.cris.nvh.framgiaproject.mediaplayer.MediaRequest;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.getPlayActivityIntent;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class FavoriteActivity extends AppCompatActivity
	implements FavoriteContract.View, TracksAdapter.OnClickItemTrackListener {

	private RecyclerView mRecyclerView;
	private TracksAdapter mAdapter;
	private List<Track> mTracks;
	private FavoriteContract.Presenter mPresenter;
	private PlayMusicService mService;
	private int mDeletePosition;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
			mService = binder.getService();
			PlayMusicService.setUIHandler(mHandler);
			initData();
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			unbindService(mConnection);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MediaRequest.LOADING:
					break;
				case MediaRequest.SUCCESS:
					break;
				case MediaRequest.UPDATE_MINI_PLAYER:
					break;
				case MediaRequest.FAILURE:
					Toast.makeText(FavoriteActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
					break;
				case MediaRequest.PAUSED:
					break;
				case MediaRequest.STOPPED:
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		initPresenter();
		initView();
		Intent serviceIntent = PlayMusicService.getMyServiceIntent(FavoriteActivity.this);
		if (mService == null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(serviceIntent);
			} else {
				startService(serviceIntent);
			}
		}
		bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PlayMusicService.setUIHandler(mHandler);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	@Override
	public void onGetTracksSuccess(List<Track> tracks) {
		if (tracks != null && tracks.size() > 0) {
			mTracks.addAll(tracks);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFail(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDeleteTracksSuccess(String isSuccess) {
		Toast.makeText(this, R.string.text_delete_success, Toast.LENGTH_SHORT).show();
		mTracks.remove(mDeletePosition);
		mAdapter.notifyItemRemoved(mDeletePosition);
	}

	@Override
	public void deleteFromFavorite(int position) {
		mDeletePosition = position;
		mPresenter.deleteFavoriteTrack(mTracks.get(position));
	}

	@Override
	public void clickItemTrackListener(int position) {
		mService.setTracks(mTracks);
		mService.createTrack(position);
		startActivity(getPlayActivityIntent(this));
	}

	@Override
	public void showDialogFeatureTrack(int position) {
	}

	public static Intent getFavoriteActivityIntent(Context context) {
		return new Intent(context, FavoriteActivity.class);
	}

	private void initPresenter() {
		TrackRepository repository = TrackRepository.getInstance(
			TracksLocalDataSource.getInstance(this),
			TracksRemoteDataSource.getInstance(this));
		mPresenter = new FavoritePresenter(repository, this);
	}

	private void initData() {
		mPresenter.getFavotiteTracks();
	}

	private void initView() {
		mTracks = new ArrayList<>();
		mRecyclerView = findViewById(R.id.recycler_favorite);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = new TracksAdapter(mTracks, this);
		mAdapter.setFavorite(true);
		mRecyclerView.setAdapter(mAdapter);
	}
}
