package com.cris.nvh.framgiaproject.ui.home_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.GenreAdapter;
import com.cris.nvh.framgiaproject.adapter.SongsSlideAdapter;
import com.cris.nvh.framgiaproject.data.model.Genre;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class HomeFragment extends Fragment {
	private String keyGenres = "genres";
	private static final int ALL_MUSIC_INDEX = 0;
	private static final int MAX_IMAGES = 3;
	private static final int SIZE_BOUND = 10;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private RecyclerView mRecyclerView;
	private ImageView mImageSearch;
	private ConstraintLayout mConstraintLayout;
	private ArrayList<Genre> mGenres;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater
				.from(container.getContext())
				.inflate(R.layout.fragment_home, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initView(View view) {
		mViewPager = view.findViewById(R.id.pager_images);
		mTabLayout = view.findViewById(R.id.indicator);
		mConstraintLayout = view.findViewById(R.id.mini_mediaplayer);
		mRecyclerView = view.findViewById(R.id.recycler_genres);
		initImageSlide();
		initGenreAdapter();
		mConstraintLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
	}

	private void initImageSlide() {
		SongsSlideAdapter songsSlideAdapter = new SongsSlideAdapter();
		mViewPager.setAdapter(songsSlideAdapter);
		mTabLayout.setupWithViewPager(mViewPager, true);
	}

	private void initGenreAdapter() {
		if (getArguments() != null) {
			mGenres = getArguments().getParcelableArrayList(keyGenres);
			mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			GenreAdapter genreAdapter = new GenreAdapter(mGenres);
			mRecyclerView.setAdapter(genreAdapter);
			getArguments().remove(keyGenres);
		}
	}
}
