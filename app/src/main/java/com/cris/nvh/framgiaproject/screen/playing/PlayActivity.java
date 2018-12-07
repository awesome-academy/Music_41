package com.cris.nvh.framgiaproject.screen.playing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.ViewPagerAdapter;
import com.cris.nvh.framgiaproject.mediaplayer.MediaRequest;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import static com.cris.nvh.framgiaproject.service.PlayMusicService.getMyServiceIntent;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class PlayActivity extends AppCompatActivity {
	public static final String EXTRA_PLAY_TRACKS =
		"com.cris.nvh.framgiaproject.screen.playing.EXTRA_TRACKS";
	public static final String EXTRA_PLAY_INDEX =
		"com.cris.nvh.framgiaproject.screen.playing.EXTRA_INDEX";
	public static final String EXTRA_SERVICE =
		"com.cris.nvh.framgiaproject.screen.playing.EXTRA_SERVICE";
	public static final int UPDATE_SEEKBAR = 283;
	private static final int PLAY_FRAGMENT_INDEX = 0;
	private static final int NOWPLAYING_FRAGMENT_INDEX = 1;
	private static final int DEFAULT = 0;
	private ViewPager mViewPager;
	private Handler mHandler;
	private ServiceConnection mConnection;
	private PlayMusicService mService;
	private ViewPagerAdapter mViewPagerAdapter;
	private PlayFragment mPlayFragment;
	private NowPlayingFragment mNowPlayingFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		initHandler();
		initView();
		bindToService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	public static Intent getPlayActivityIntent(Context context) {
		return new Intent(context, PlayActivity.class);
	}

	private void initView() {
		mViewPager = findViewById(R.id.view_pager);
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPagerAdapter.addFragment(PlayFragment.newInstance());
		mViewPagerAdapter.addFragment(NowPlayingFragment.newInstance());
		mViewPager.setAdapter(mViewPagerAdapter);
	}

	private void bindToService() {
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
				mService = binder.getService();
				mService.setUIHandler(mHandler);
				getNowPlayingFragment();
				getPlayFragment();
				mPlayFragment.setHandler(mHandler);
				mPlayFragment.setService(mService);
				mNowPlayingFragment.setService(mService);
				mViewPager.setAdapter(mViewPagerAdapter);
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
			}
		};
		bindService(getMyServiceIntent(this), mConnection, BIND_AUTO_CREATE);
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case MediaRequest.LOADING:
						if (!isDestroyed()) {
							mPlayFragment.startLoading();
							mNowPlayingFragment.updateNowPlaying();
						}
						break;
					case MediaRequest.SUCCESS:
						mPlayFragment.loadingSuccess();
						break;
					case MediaRequest.UPDATE_MINI_PLAYER:
						mNowPlayingFragment.updateNowPlaying();
						break;
					case MediaRequest.PAUSED:
						mPlayFragment.getButtonChangeState().setImageResource(R.drawable.ic_play);
						break;
					case MediaRequest.FAILURE:
						Toast.makeText(PlayActivity.this, (String) msg.obj,
							Toast.LENGTH_SHORT).show();
						break;
					case MediaRequest.STOPPED:
						mPlayFragment.getButtonChangeState().setImageResource(R.drawable.ic_play);
						break;
					case UPDATE_SEEKBAR:
						mPlayFragment.requestUpdateSeekBar();
					default:
						break;
				}
			}
		};
	}

	private void getPlayFragment() {
		mPlayFragment = (PlayFragment)
			mViewPagerAdapter.getItem(PLAY_FRAGMENT_INDEX);
	}

	private void getNowPlayingFragment() {
		mNowPlayingFragment = (NowPlayingFragment)
			mViewPagerAdapter.getItem(NOWPLAYING_FRAGMENT_INDEX);
	}
}
