package com.cris.nvh.framgiaproject.screen.playing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.ViewPagerAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class PlayActivity extends AppCompatActivity {
	public static final String EXTRA_PLAY_TRACKS =
			"com.cris.nvh.framgiaproject.screen.playing.EXTRA_TRACKS";
	public static final String EXTRA_PLAY_INDEX =
			"com.cris.nvh.framgiaproject.screen.playing.EXTRA_INDEX";
	private ViewPager mViewPager;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		ArrayList<Track> tracks = getIntent().getExtras().getParcelableArrayList(EXTRA_PLAY_TRACKS);
		int trackIndex = getIntent().getIntExtra(EXTRA_PLAY_INDEX, 0);
		getIntent().removeExtra(EXTRA_PLAY_INDEX);
		getIntent().removeExtra(EXTRA_PLAY_TRACKS);
		mViewPager = findViewById(R.id.view_pager);
		initFragments(tracks, trackIndex);
	}

	private void initFragments(ArrayList<Track> tracks, int index) {
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(EXTRA_PLAY_TRACKS, tracks);
		bundle.putInt(EXTRA_PLAY_INDEX, index);
		PlayFragment playFragment = PlayFragment.newInstance();
		playFragment.setArguments(bundle);
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPagerAdapter.addFragment(playFragment);
		viewPagerAdapter.addFragment(NowPlayingFragment.newInstance());
		mViewPager.setAdapter(viewPagerAdapter);
	}
}
