package com.cris.nvh.framgiaproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cris.nvh.framgiaproject.adapter.ViewPagerAdapter;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.ui.home_screen.HomeFragment;
import com.cris.nvh.framgiaproject.ui.my_music_screen.MyMusicFragment;
import com.cris.nvh.framgiaproject.ui.setting_screen.SettingFragment;

import java.util.ArrayList;

import static com.cris.nvh.framgiaproject.ui.splash_screen.SplashActivity.INTENT_ACTION;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
		BottomNavigationView.OnNavigationItemSelectedListener {
	private static final int HOME_INDEX = 0;
	private static final int MY_MUSIC_INDEX = 1;
	private static final int SETTING_INDEX = 2;
	private String genreKey = "genres";
	private String trackKey = "tracks";
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private BottomNavigationView mBottomNavigationView;
	private ArrayList<Genre> mGenres;
	private ArrayList<Track> mTracks;
	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mViewPager = findViewById(R.id.view_pager);
		mBottomNavigationView = findViewById(R.id.navigation_container);
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mTracks = getIntent().getParcelableArrayListExtra(trackKey);
		initReceiver();
		initFragments(mTracks);
		LocalBroadcastManager
				.getInstance(this)
				.registerReceiver(mReceiver, new IntentFilter(INTENT_ACTION));
		mBottomNavigationView.setOnNavigationItemSelectedListener(this);
		mViewPager.setOnPageChangeListener(this);
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

	public void initFragments(ArrayList<Track> tracks) {
		HomeFragment homeFragment = new HomeFragment();
		MyMusicFragment myMusicFragment = new MyMusicFragment();
		SettingFragment settingFragment = new SettingFragment();
		Bundle bundleMyMusic = new Bundle();
		bundleMyMusic.putSerializable(trackKey, tracks);
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
				mGenres = (ArrayList<Genre>) intent.getSerializableExtra(genreKey);
				Bundle bundleHome = new Bundle();
				bundleHome.putSerializable(genreKey, mGenres);
				mViewPagerAdapter.getItem(HOME_INDEX).setArguments(bundleHome);
				mViewPager.setAdapter(mViewPagerAdapter);
			}
		};
	}
}
