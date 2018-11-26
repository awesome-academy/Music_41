package com.cris.nvh.framgiaproject.ui.my_music_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.LibraryAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class MyMusicFragment extends Fragment implements TracksAdapter.OnClickItemSongListener {
	private static final int DEFAULT_TOTAL_SONGS = 0;
	private String trackKey = "tracks";
	private RecyclerView mRecyclerViewLibrary;
	private RecyclerView mRecyclerViewRecent;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater
				.from(container.getContext())
				.inflate(R.layout.fragment_my_music, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ArrayList<Track> tracks = getLocalTracks();
		mRecyclerViewLibrary = view.findViewById(R.id.recycler_library);
		mRecyclerViewRecent = view.findViewById(R.id.recycler_recent);
		mRecyclerViewLibrary.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerViewRecent.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerViewLibrary.setAdapter(new LibraryAdapter(new int[]{tracks.size(),
				DEFAULT_TOTAL_SONGS, DEFAULT_TOTAL_SONGS}));
		mRecyclerViewRecent.setAdapter(new TracksAdapter(tracks, this));
	}

	private ArrayList<Track> getLocalTracks() {
		if (getArguments() != null) {
			ArrayList<Track> tracks = (ArrayList<Track>)
					getArguments().getSerializable(trackKey);
			return tracks;
		}
		return null;
	}

	@Override
	public void clickItemSongListener(int position) {
	}

	@Override
	public void showDialogFeatureTrack(int position) {
	}
}
