package com.cris.nvh.framgiaproject.screen.playing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class NowPlayingFragment extends Fragment implements TracksAdapter.OnClickItemSongListener {
	private RecyclerView mRecyclerView;
	private ImageView mImageBack;

	public static NowPlayingFragment newInstance() {
		return new NowPlayingFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(container.getContext())
				.inflate(R.layout.fragment_now_playing, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mRecyclerView = view.findViewById(R.id.recycler_now_playing);
		mImageBack = view.findViewById(R.id.image_back_button);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		TracksAdapter tracksAdapter = new TracksAdapter(this);
		mRecyclerView.setAdapter(tracksAdapter);
	}

	@Override
	public void clickItemSongListener(int position) {

	}

	@Override
	public void showDialogFeatureTrack(int position) {

	}
}
