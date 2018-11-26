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
import android.widget.ImageButton;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.GenreAdapter;
import com.cris.nvh.framgiaproject.adapter.SongsSlideAdapter;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
	private ImageButton mButtonSearch;
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
		initImageSlide();
		RecyclerView recyclerView = view.findViewById(R.id.recycler_genres);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		GenreAdapter genreAdapter = new GenreAdapter(mGenres);
		recyclerView.setAdapter(genreAdapter);

		mConstraintLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		mButtonSearch = view.findViewById(R.id.button_search);
		mButtonSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
	}

	private void initImageSlide() {
		if (getArguments() != null) {
			mGenres = (ArrayList<Genre>)
					getArguments().getSerializable(keyGenres);
			initAdapter(mGenres);
			mTabLayout.setupWithViewPager(mViewPager, true);
			getArguments().remove(keyGenres);
		}
	}

	private void initAdapter(ArrayList<Genre> genres) {
		ArrayList<Track> tracks = new ArrayList<>();
		int[] number = randomNumber();
		for (int val : number)
			tracks.add(genres.get(ALL_MUSIC_INDEX).getTracks().get(val));
		SongsSlideAdapter songsSlideAdapter = new SongsSlideAdapter(tracks);
		mViewPager.setAdapter(songsSlideAdapter);
	}

	public int[] randomNumber() {
		Set<Integer> set = new HashSet();
		while (set.size() < MAX_IMAGES) {
			set.add(new Random().nextInt(SIZE_BOUND));
		}
		int[] numbers = new int[set.size()];
		int i = 0;
		for (int val : set) numbers[i++] = val;
		return numbers;
	}
}
