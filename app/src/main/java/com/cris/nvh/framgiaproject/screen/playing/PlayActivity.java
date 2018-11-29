package com.cris.nvh.framgiaproject.screen.playing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.ViewPagerAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;

import static com.cris.nvh.framgiaproject.screen.mymusic.MyMusicFragment.EXTRA_TRACK;

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
		Track track = getIntent().getExtras().getParcelable(EXTRA_TRACK);
		mViewPager = findViewById(R.id.view_pager);
		initFragments(track);
	}

	private void initFragments(Track track) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(EXTRA_TRACK, track);
		PlayFragment playFragment = PlayFragment.newInstance();
		playFragment.setArguments(bundle);
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPagerAdapter.addFragment(playFragment);
		viewPagerAdapter.addFragment(NowPlayingFragment.newInstance());
		mViewPager.setAdapter(viewPagerAdapter);
	}
}
