package com.cris.nvh.framgiaproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.mediaplayer.PlayMusic;
import com.cris.nvh.framgiaproject.screen.playing.PlayActivity;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.List;

public class MiniMediaPlayer implements View.OnClickListener {
	private static final String NULL = "null";
	private static PlayMusicService sService;
	private TextView mTrackName;
	private TextView mTrackSinger;
	private ImageView mImageNext;
	private ImageView mImageChangeState;
	private ImageView mImagePrevious;
	private ImageView mTrackImage;
	private View mMiniMediaPlayer;

	public MiniMediaPlayer(View view) {
		mMiniMediaPlayer = view;
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
				mMiniMediaPlayer.getContext()
					.startActivity(getPlayActivityIntent(mMiniMediaPlayer.getContext()));
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
		mImageChangeState.setBackgroundResource(R.drawable.ic_play);
		sService.pauseTrack();
	}

	public void play() {
		mImageChangeState.setBackgroundResource(R.drawable.ic_pause);
		int mediaStatus = sService.getMediaPlayerManager().getState();
		if (mediaStatus == PlayMusic.StatusPlayerType.STOPPED) {
			sService.requestPrepareAsync();
			return;
		}
		sService.startTrack();
	}

	public void next() {
		sService.nextTrack();
	}

	public void previous() {
		sService.previousTrack();
	}

	public void loadingSuccess() {
	}

	public void startLoading(int index) {
		mMiniMediaPlayer.setVisibility(View.VISIBLE);
		Track track = sService.getTracks().get(index);
		mTrackSinger.setText(track.getArtist());
		mTrackName.setText(track.getTitle());
		Glide.with(mMiniMediaPlayer)
			.load(track.getArtworkUrl())
			.into(mTrackImage);
	}

	public void update() {
		List<Track> tracks = sService.getTracks();
		int index = sService.getTrack();
		updateCurrentTrack(tracks.get(index));
	}

	private void updateCurrentTrack(Track track) {
		mTrackName.setText(track.getTitle());
		mTrackSinger.setText(track.getArtist());
		setImageAlbum(track);
		if (sService.isPlaying()) {
			mImageChangeState.setBackgroundResource(R.drawable.ic_pause);
			return;
		}
		mImageChangeState.setBackgroundResource(R.drawable.ic_play);

	}

	private void setImageAlbum(Track track) {
		Object uri;
		if (track.getArtworkUrl() != null &&
			!track.getArtworkUrl().equals(NULL)) {
			uri = track.getArtworkUrl();
		} else {
			uri = R.drawable.default_album;
		}
		Glide.with(mMiniMediaPlayer)
			.load(uri)
			.apply(RequestOptions
				.circleCropTransform())
			.into(mTrackImage);
	}

	private void initView() {
		mImageChangeState = mMiniMediaPlayer.findViewById(R.id.button_change_state);
		mTrackName = mMiniMediaPlayer.findViewById(R.id.text_song_name);
		mTrackSinger = mMiniMediaPlayer.findViewById(R.id.text_singer_name);
		mImageNext = mMiniMediaPlayer.findViewById(R.id.button_next);
		mImagePrevious = mMiniMediaPlayer.findViewById(R.id.button_previous);
		mTrackImage = mMiniMediaPlayer.findViewById(R.id.image_track);
		mImageChangeState.setOnClickListener(this);
		mImageNext.setOnClickListener(this);
		mImagePrevious.setOnClickListener(this);
		mTrackImage.setOnClickListener(this);
		mMiniMediaPlayer.setOnClickListener(this);
	}
}
