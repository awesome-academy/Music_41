package com.cris.nvh.framgiaproject.screen.listtracks;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;

import static com.cris.nvh.framgiaproject.MainActivity.getMyServiceIntent;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_TRACKS;

public class ListTracksActivity extends AppCompatActivity implements View.OnClickListener, TracksAdapter.OnClickItemTrackListener {
	private RecyclerView mRecyclerView;
	private PlayMusicService mService;
	private ArrayList<Track> mTracks;
	private ServiceConnection mConnection;
	private ImageView mButtonBack;
	private ImageView mButtonSearch;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_tracks);
		bindToService();
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	@Override
	public void clickItemTrackListener(int position) {
	}

	@Override
	public void showDialogFeatureTrack(int position) {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_view:
				super.onBackPressed();
				break;
			case R.id.image_search:
				break;
			default:
				break;
		}
	}

	private void bindToService() {
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
				mService = binder.getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				unbindService(mConnection);
			}
		};
		Intent serviceIntent = getMyServiceIntent(this);
		bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
	}

	private void initView() {
		mRecyclerView = findViewById(R.id.recycler_tracks);
		mButtonSearch = findViewById(R.id.image_search);
		mButtonBack = findViewById(R.id.image_view);
		mButtonBack.setImageResource(R.drawable.ic_back);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mTracks = getIntent().getParcelableArrayListExtra(EXTRA_TRACKS);
		TracksAdapter tracksAdapter = new TracksAdapter(mTracks, this);
		mRecyclerView.setAdapter(tracksAdapter);
		mButtonBack.setOnClickListener(this);
		mButtonSearch.setOnClickListener(this);
	}
}
