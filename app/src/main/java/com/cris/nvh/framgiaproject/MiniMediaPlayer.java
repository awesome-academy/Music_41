package com.cris.nvh.framgiaproject;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cris.nvh.framgiaproject.screen.home.HomeFragment;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class MiniMediaPlayer implements View.OnClickListener {
	private TextView mTrackName;
	private TextView mTrackSinger;
	private ImageButton mButtonNext;
	private ImageButton mButtonPlay;
	private ImageButton mButtonPrevious;
	private ImageView mTrackImage;
	private View mView;

	public MiniMediaPlayer(View view) {
		mView = view;
		initView();
	}

	private void initView() {
		mButtonPlay = mView.findViewById(R.id.button_play);
		mTrackName = mView.findViewById(R.id.text_song_name);
		mTrackSinger = mView.findViewById(R.id.text_singer_name);
		mButtonNext = mView.findViewById(R.id.button_next);
		mButtonPrevious = mView.findViewById(R.id.button_previous);
		mTrackImage = mView.findViewById(R.id.image_track);
		mButtonPlay.setOnClickListener(this);
		mButtonNext.setOnClickListener(this);
		mButtonPrevious.setOnClickListener(this);
		mTrackImage.setOnClickListener(this);
		mView.setOnClickListener(this);
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
			case R.id.button_play:
				play();
				break;
			case R.id.image_track:
				mView.getContext()
						.startActivity(HomeFragment.getPlayActivityIntent(mView.getContext()));
				break;
			default:
				break;
		}
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

	public void pause() {
		mButtonPlay.setImageResource(R.drawable.ic_play);
	}

	public void play() {
	}

	public void next() {
	}

	public void previous() {
	}
}
