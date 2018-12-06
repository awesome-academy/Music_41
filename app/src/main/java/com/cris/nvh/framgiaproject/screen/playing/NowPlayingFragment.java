package com.cris.nvh.framgiaproject.screen.playing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class NowPlayingFragment extends Fragment implements View.OnClickListener,
	TracksAdapter.OnClickItemTrackListener {
	private RecyclerView mRecyclerView;
	private ImageView mImageBack;
	private List<Track> mTracks;
	private TracksAdapter mTracksAdapter;
	private PlayMusicService mService;

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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_back_button:
				getActivity().onBackPressed();
				break;
			default:
				break;
		}
	}

	@Override
	public void clickItemTrackListener(int position) {

	}

	@Override
	public void showDialogFeatureTrack(int position) {

	}

	public void setService(PlayMusicService service) {
		mService = service;
	}

	public void updateNowPlaying() {
	}

	private void initView(View view) {
		mRecyclerView = view.findViewById(R.id.recycler_now_playing);
		mImageBack = view.findViewById(R.id.image_back_button);
		mImageBack.setOnClickListener(this);
	}
}
