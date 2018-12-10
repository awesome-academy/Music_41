package com.cris.nvh.framgiaproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.mediaplayer.PlayMusic;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.List;

import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.getPlayActivityIntent;

public class MiniMediaPlayer implements View.OnClickListener {
	private static final String NULL = "null";
	private PlayMusicService mService;
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
				if (!mService.isPlaying()) {
					play();
					return;
				}
				pause();
				break;
			case R.id.image_track:
				if (!mService.isPlaying())
					mService.createTrack(mService.getTrack());
				mMiniMediaPlayer.getContext()
					.startActivity(getPlayActivityIntent(mMiniMediaPlayer.getContext()));
				break;
			default:
				break;
		}
	}

	public PlayMusicService getService() {
		return mService;
	}

	public void setService(PlayMusicService service) {
		mService = service;
	}

	public void startLoading() {
		mImageChangeState.setClickable(false);
		mImageNext.setClickable(false);
		mImagePrevious.setClickable(false);
		Track track = mService.getTracks().get(mService.getTrack());
		mTrackSinger.setText(track.getArtist());
		mTrackName.setText(track.getTitle());
		setImageAlbum(track);
	}

	public void loadingSuccess() {
		mImageChangeState.setClickable(true);
		mImageNext.setClickable(true);
		mImagePrevious.setClickable(true);
	}

	public void update() {
		if (mService.getTracks() != null) {
			List<Track> tracks = mService.getTracks();
			int index = mService.getTrack();
			mTrackName.setText(tracks.get(index).getTitle());
			mTrackSinger.setText(tracks.get(index).getArtist());
			setImageAlbum(tracks.get(index));
			setPlayButton();
			loadingSuccess();
		}
	}

	public void setPlayButton() {
		if (mService.isPlaying()) {
			mImageChangeState.setBackgroundResource(R.drawable.ic_pause);
			return;
		}
		mImageChangeState.setBackgroundResource(R.drawable.ic_play);
	}

	private void pause() {
		mImageChangeState.setBackgroundResource(R.drawable.ic_play);
		mService.pauseTrack();
	}

	private void play() {
		mImageChangeState.setBackgroundResource(R.drawable.ic_pause);
		int mediaStatus = mService.getMediaPlayerManager().getState();
		Track currentTrack = mService.getTracks().get(mService.getTrack());
		if (!currentTrack.isOffline() && mediaStatus == PlayMusic.StatusPlayerType.STOPPED) {
			mService.requestPrepareAsync();
			return;
		}
		mService.startTrack();
	}

	private void next() {
		mService.nextTrack();
	}

	private void previous() {
		mService.previousTrack();
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
		mTrackName.setSelected(true);
		mTrackSinger.setSelected(true);
	}
}
