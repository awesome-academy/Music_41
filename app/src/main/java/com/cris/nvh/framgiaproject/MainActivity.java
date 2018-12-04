package com.cris.nvh.framgiaproject;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cris.nvh.framgiaproject.adapter.ViewPagerAdapter;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.screen.home.HomeFragment;
import com.cris.nvh.framgiaproject.screen.mymusic.MyMusicFragment;
import com.cris.nvh.framgiaproject.screen.setting.SettingFragment;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;

import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.FAILURE;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.LOADING;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.PAUSED;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.STOPPED;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.SUCCESS;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.UPDATE_MINI_PLAYER;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.ACTION_LOAD_API;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_GENRES;
import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_TRACKS;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
	BottomNavigationView.OnNavigationItemSelectedListener {
	private static final int HOME_INDEX = 0;
	private static final int MY_MUSIC_INDEX = 1;
	private static final int SETTING_INDEX = 2;
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private BottomNavigationView mBottomNavigationView;
	private ArrayList<Genre> mGenres;
	private ArrayList<Track> mTracks;
	private BroadcastReceiver mReceiver;
	private PlayMusicService mService;
	private Handler mHandler;
	private ServiceConnection mConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initConnection();
		initService();
		initHandler();
		initReceiver();
		registerReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mService != null) {
			mService.setUIHandler(mHandler);
			updateMiniPlayer();
		}
	}

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.tab_home:
				mViewPager.setCurrentItem(HOME_INDEX);
				break;
			case R.id.tab_my_music:
				mViewPager.setCurrentItem(MY_MUSIC_INDEX);
				break;
			case R.id.tab_setting:
				mViewPager.setCurrentItem(SETTING_INDEX);
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public void onPageScrolled(int i, float v, int i1) {
	}

	@Override
	public void onPageSelected(int position) {
		mBottomNavigationView.getMenu().getItem(position).setChecked(true);
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	public static Intent getMyServiceIntent(Context context) {
		Intent intent = new Intent(context, PlayMusicService.class);
		return intent;
	}

	private void initService() {
		Intent serviceIntent = getMyServiceIntent(this);
		bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
		if (mService == null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(serviceIntent);
			} else {
				startService(serviceIntent);
			}
		}
	}

	private void initView() {
		mViewPager = findViewById(R.id.view_pager);
		mBottomNavigationView = findViewById(R.id.navigation_container);
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mTracks = getIntent().getParcelableArrayListExtra(EXTRA_TRACKS);
		initFragments(mTracks);
		mBottomNavigationView.setOnNavigationItemSelectedListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initFragments(ArrayList<Track> tracks) {
		HomeFragment homeFragment = HomeFragment.newInstance();
		MyMusicFragment myMusicFragment = MyMusicFragment.newInstance();
		SettingFragment settingFragment = SettingFragment.newInstance();
		Bundle bundleMyMusic = new Bundle();
		bundleMyMusic.putParcelableArrayList(EXTRA_TRACKS, tracks);
		myMusicFragment.setArguments(bundleMyMusic);
		mViewPagerAdapter.addFragment(homeFragment);
		mViewPagerAdapter.addFragment(myMusicFragment);
		mViewPagerAdapter.addFragment(settingFragment);
		mViewPager.setAdapter(mViewPagerAdapter);
	}

	private void initReceiver() {
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mGenres = intent.getParcelableArrayListExtra(EXTRA_GENRES);
				Bundle bundleHome = new Bundle();
				bundleHome.putParcelableArrayList(EXTRA_GENRES, mGenres);
				mViewPagerAdapter.getItem(HOME_INDEX).setArguments(bundleHome);
				mViewPager.setAdapter(mViewPagerAdapter);
				updateMiniPlayer();
			}
		};
	}

	private void registerReceiver() {
		LocalBroadcastManager
			.getInstance(this)
			.registerReceiver(mReceiver, new IntentFilter(ACTION_LOAD_API));
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case LOADING:
						HomeFragment.sMiniMediaPlayer.startLoading(msg.arg1);
						break;
					case SUCCESS:
						HomeFragment.sMiniMediaPlayer.loadingSuccess();
						break;
					case FAILURE:
						break;
					case PAUSED:
						break;
					case STOPPED:
						break;
					case UPDATE_MINI_PLAYER:
						HomeFragment.sMiniMediaPlayer.update();
						break;
					default:
						break;
				}
			}
		};
	}

	private void updateMiniPlayer() {
		Message message = new Message();
		message.what = UPDATE_MINI_PLAYER;
		mHandler.sendMessage(message);
	}

	private void initConnection() {
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
				mService = binder.getService();
				mService.setUIHandler(mHandler);
				mService.setTracks(mTracks);
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				unbindService(mConnection);
			}
		};
	}
}
