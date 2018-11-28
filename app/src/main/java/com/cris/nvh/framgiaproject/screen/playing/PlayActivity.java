package com.cris.nvh.framgiaproject.screen.playing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.ViewPagerAdapter;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class PlayActivity extends AppCompatActivity {
	private ViewPager mViewPager;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		mViewPager = findViewById(R.id.view_pager);
		initFragments();
	}

	private void initFragments() {
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPagerAdapter.addFragment(PlayFragment.newInstance());
		viewPagerAdapter.addFragment(NowPlayingFragment.newInstance());
		mViewPager.setAdapter(viewPagerAdapter);
	}
}
