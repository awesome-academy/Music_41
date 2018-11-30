package com.cris.nvh.framgiaproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cris.nvh.framgiaproject.screen.home.HomeFragment;
import com.cris.nvh.framgiaproject.screen.playing.PlayActivity;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

public class MiniMediaPlayer implements View.OnClickListener {
	private static PlayMusicService sService;
	private TextView mTrackName;
	private TextView mTrackSinger;
	private ImageView mImageNext;
	private ImageView mImageChangeState;
	private ImageView mImagePrevious;
	private ImageView mTrackImage;
	private View mView;

	public MiniMediaPlayer(View view) {
		mView = view;
		initView();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_next:
				next();
				break;
			case R.id.button_previous:
				previous();
				break;
			case R.id.button_change_state:
				if (!sService.isPlaying()) {
					play();
					return;
				}
				pause();
				break;
			case R.id.image_track:
				mView.getContext()
						.startActivity(getPlayActivityIntent(mView.getContext()));
				break;
			default:
				break;
		}
	}

	private Intent getPlayActivityIntent(Context context) {
		Intent intent = new Intent(context, PlayActivity.class);
		return intent;
	}

	public TextView getTrackName() {
		return mTrackName;
	}

	public TextView getTrackSinger() {
		return mTrackSinger;
	}

	public ImageView getTrackImage() {
		return mTrackImage;
	}

	public PlayMusicService getService() {
		return sService;
	}

	public void setService(PlayMusicService service) {
		sService = service;
	}

	public void pause() {
		sService.pauseTrack();
		mImageChangeState.setBackgroundResource(R.drawable.ic_play);
	}

	public void play() {
		sService.startTrack();
		mImageChangeState.setBackgroundResource(R.drawable.ic_pause);
	}

	public void next() {
		sService.nextTrack(0);
	}

	public void previous() {
		sService.previousTrack(0);
	}

	private void initView() {
		mImageChangeState = mView.findViewById(R.id.button_change_state);
		mTrackName = mView.findViewById(R.id.text_song_name);
		mTrackSinger = mView.findViewById(R.id.text_singer_name);
		mImageNext = mView.findViewById(R.id.button_next);
		mImagePrevious = mView.findViewById(R.id.button_previous);
		mTrackImage = mView.findViewById(R.id.image_track);
		mImageChangeState.setOnClickListener(this);
		mImageNext.setOnClickListener(this);
		mImagePrevious.setOnClickListener(this);
		mTrackImage.setOnClickListener(this);
		mView.setOnClickListener(this);
	}
}
