package com.cris.nvh.framgiaproject.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.cris.nvh.framgiaproject.data.model.Track;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MediaPlayerManager extends MediaPlayerSetting implements PlayMusic,
	MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

	private static final int CHANGE_POSITION = 1;
	private static int sLoopType = 0;
	private static int sShuffleType;
	private static MediaPlayerManager sInstance;
	private Context mContext;
	private MediaPlayer mMediaPlayer;
	private List<Track> mTracks;
	private int mCurrentIndex;
	private int mState;
	private OnLoadingTrackListener mListener;

	private MediaPlayerManager(Context context) {
		mContext = context;
		mListener = (OnLoadingTrackListener) context;
		sLoopType = LoopType.NONE;
		sShuffleType = ShuffleType.OFF;
	}

	public static MediaPlayerManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new MediaPlayerManager(context);
		}
		return sInstance;
	}

	@Override
	public void create(int index) {
		mCurrentIndex = index;
		Track track = mTracks.get(mCurrentIndex);
		mListener.onStartLoading(index);
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mState = StatusPlayerType.IDLE;
		} else {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnCompletionListener(this);
		}
		if (!mTracks.isEmpty()) {
			if (track.isOffline()) initOffline(track);
			else initOnline(track);
		}
	}

	@Override
	public void prepare() {
		if (mMediaPlayer != null) {
			mState = StatusPlayerType.PREPARING;
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.prepareAsync();
		}
	}

	@Override
	public void start() {
		if (mMediaPlayer != null) {
			mMediaPlayer.start();
			mState = StatusPlayerType.STARTED;
			mListener.onLoadingSuccess();
			Track track = mTracks.get(mCurrentIndex);
			if (track.isOffline()) {
				saveRecentTrack();
			}
		} else {
			create(getTrack());
		}
	}

	@Override
	public void pause() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
			mState = StatusPlayerType.PAUSED;
			mListener.onTrackPaused();
		}
	}

	@Override
	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mState = StatusPlayerType.STOPPED;
			mListener.onTrackStopped();
		}
	}

	@Override
	public int getDuration() {
		if (mMediaPlayer != null && mState != StatusPlayerType.IDLE) {
			return mMediaPlayer.getDuration();
		}
		return 0;
	}

	@Override
	public int getCurrrentPosition() {
		if (mMediaPlayer != null && mState != StatusPlayerType.IDLE) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public boolean isPlaying() {
		return mMediaPlayer != null && mMediaPlayer.isPlaying();
	}

	@Override
	public void seek(int possition) {
		if (mMediaPlayer != null) {
			mMediaPlayer.seekTo(possition);
		}
	}

	@Override
	public void loop(boolean isLoop) {
		if (mMediaPlayer != null) {
			mMediaPlayer.setLooping(isLoop);
		}
	}

	@Override
	public int getTrack() {
		return mCurrentIndex;
	}

	@Override
	public void next() {
		if (MediaPlayerSetting.ShuffleType.ON == sShuffleType) {
			mCurrentIndex = ramdomSong();
			create(mCurrentIndex);
			mListener.onChangeTrack();
			return;
		}
		mCurrentIndex++;
		mCurrentIndex = mCurrentIndex == mTracks.size() ? 0 : mCurrentIndex;
		create(mCurrentIndex);
		mListener.onChangeTrack();
	}

	@Override
	public void previous() {
		if (mCurrentIndex == 0) {
			mCurrentIndex = getTracks().size() - CHANGE_POSITION;
			create(mCurrentIndex);
			mListener.onChangeTrack();
			return;
		}
		create(--mCurrentIndex);
		mListener.onChangeTrack();
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		switch (sLoopType) {
			case LoopType.NONE:
				if (mCurrentIndex == mTracks.size() - CHANGE_POSITION
					&& mState != StatusPlayerType.STOPPED) {
					stop();
				} else {
					next();
				}
				break;
			case LoopType.ALL:
				next();
				break;
			case LoopType.ONE:
				start();
				break;
			default:
				break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		mediaPlayer.start();
		mListener.onChangeTrack();
	}

	public List<Track> getTracks() {
		return mTracks;
	}

	public void setTracks(List<Track> tracks) {
		mTracks = tracks;
	}

	public MediaPlayer getMediaPlayer() {
		return mMediaPlayer;
	}

	public int getState() {
		return mState;
	}

	public MediaPlayerManager setState(int state) {
		this.mState = state;
		return this;
	}

	private int ramdomSong() {
		int currentSong = getTrack();
		Random r = new Random();
		int result = r.nextInt(getTracks().size());
		if (result == currentSong)
			return ++result;
		return result;
	}

	private void saveRecentTrack() {
	}

	private void initOffline(Track track) {
		mMediaPlayer = MediaPlayer.create(mContext,
			Uri.parse(track.getPermalinkUrl()));
		mMediaPlayer.setOnCompletionListener(this);
		start();
		mListener.onChangeTrack();
	}

	private void initOnline(Track track) {
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mMediaPlayer.setDataSource(track.getStreamUrl());
			mState = StatusPlayerType.INITIALIZED;
			prepare();
		} catch (IOException e) {
			mListener.onLoadingFail(e.getMessage());
		}
	}

	public interface OnLoadingTrackListener {
		void onStartLoading(int index);

		void onLoadingFail(String message);

		void onLoadingSuccess();

		void onChangeTrack();

		void onTrackPaused();

		void onTrackStopped();
	}
}
