package com.cris.nvh.framgiaproject.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.cris.nvh.framgiaproject.data.model.Track;

import java.io.IOException;
import java.util.List;

public class MediaPlayerManager extends MediaPlayerSetting implements PlayMusic,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

	private static MediaPlayerManager sInstance;
	private Context mContext;
	private MediaPlayer mMediaPlayer;
	private List<Track> mTracks;
	private int mCurrentIndex;
	private OnLoadingTrackListener mListener;

	private MediaPlayerManager(Context context) {
		mContext = context;
		mListener = (OnLoadingTrackListener) context;
	}

	public static MediaPlayerManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new MediaPlayerManager(context);
		}
		return sInstance;
	}

	@Override
	public void create(int index) {
	}

	@Override
	public void prepare() {
	}

	@Override
	public void start() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void stop() {
	}

	@Override
	public int getDuration() {
		return 0;
	}

	@Override
	public int getCurrrentPosition() {
		return 0;
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void seek(int possition) {
	}

	@Override
	public void loop(boolean isLoop) {
	}

	@Override
	public int getTrack() {
		return 0;
	}

	@Override
	public void next() {
	}

	@Override
	public void previous() {
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		start();
	}

	public List<Track> getTracks() {
		return mTracks;
	}

	public void setTracks(List<Track> tracks) {
		mTracks = tracks;
	}

	private void initOnline() {
	}

	private void initOffline(Track track) {
	}

	public interface OnLoadingTrackListener {
		void onStartLoading(int index);

		void onLoadingFail(String message);

		void onLoadingSuccess();

		void onTrackPaused();

		void onTrackStopped();
	}

	private void saveRecentTrack() {
	}
}
