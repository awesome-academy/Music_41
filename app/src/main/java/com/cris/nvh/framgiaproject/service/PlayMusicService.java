package com.cris.nvh.framgiaproject.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.mediaplayer.MediaPlayerManager;

import java.util.List;

import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.FAILURE;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.LOADING;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.PAUSED;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.STOPPED;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.SUCCESS;
import static com.cris.nvh.framgiaproject.mediaplayer.MediaRequest.UPDATE_MINI_PLAYER;

public class PlayMusicService extends Service implements IService,
	MediaPlayerManager.OnLoadingTrackListener {

	public static final String EXTRA_REQUEST_CODE =
		"com.cris.nvh.framgiaproject.service.EXTRA.REQUEST_CODE";
	private static final int REQUEST_CREATE = 0;
	private static final int REQUEST_NEXT = 1;
	private static final int REQUEST_PREVIOUS = 2;
	private static final int REQUEST_START = 3;
	private static final int REQUEST_PAUSE = 4;
	private static final int REQUEST_SEEK = 5;
	private static final int REQUEST_PREPARE = 6;
	private static final String WORKER_THREAD_NAME = "ServiceThread";
	private static final int VALUE_NEXT_SONG = 4;
	private static final int VALUE_PREVIOUS_SONG = 5;
	private static final int VALUE_PLAY_SONG = 2;
	private static Handler mUIHandler;
	private final IBinder mBinder = new LocalBinder();
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private MediaPlayerManager mMediaPlayerManager;

	@Override
	public void onCreate() {
		super.onCreate();
		mMediaPlayerManager = MediaPlayerManager.getsInstance(this);
		HandlerThread thread = new HandlerThread(WORKER_THREAD_NAME);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			int request = intent.getIntExtra(EXTRA_REQUEST_CODE, 0);
			switch (request) {
				case VALUE_NEXT_SONG:
					nextTrack();
					break;
				case VALUE_PREVIOUS_SONG:
					previousTrack();
					break;
				case VALUE_PLAY_SONG:
					if (isPlaying()) {
						pauseTrack();
					} else {
						startTrack();
					}
					break;
				default:
					break;
			}
		}
		return START_STICKY;
	}

	@Override
	public void create(int index) {
		mMediaPlayerManager.create(index);
	}

	@Override
	public void prepare() {
		mMediaPlayerManager.prepare();
	}

	@Override
	public void start() {
		mMediaPlayerManager.start();
	}

	@Override
	public void pause() {
		mMediaPlayerManager.pause();
	}

	@Override
	public void stop() {
		mMediaPlayerManager.stop();
	}

	@Override
	public int getDuration() {
		return mMediaPlayerManager.getDuration();
	}

	@Override
	public int getCurrrentPosition() {
		return mMediaPlayerManager.getCurrrentPosition();
	}

	@Override
	public boolean isPlaying() {
		return mMediaPlayerManager.isPlaying();
	}

	@Override
	public void seek(int position) {
		mMediaPlayerManager.seek(position);
	}

	@Override
	public void loop(boolean isLoop) {
		mMediaPlayerManager.loop(isLoop);
	}

	@Override
	public int getTrack() {
		return mMediaPlayerManager.getTrack();
	}

	@Override
	public void next() {
		mMediaPlayerManager.next();
	}

	@Override
	public void previous() {
		mMediaPlayerManager.previous();
	}

	public static void setUIHandler(Handler uiHandler) {
		mUIHandler = uiHandler;
	}

	@Override
	public void onStartLoading(int index) {
		if (mUIHandler != null) {
			Message message = new Message();
			message.arg1 = index;
			message.what = LOADING;
			mUIHandler.sendMessage(message);
		}
	}

	@Override
	public void onLoadingFail(String message) {
		Message msg = new Message();
		msg.what = FAILURE;
		msg.obj = message;
		mUIHandler.sendMessage(msg);
	}

	@Override
	public void onLoadingSuccess() {
		mUIHandler.sendEmptyMessage(SUCCESS);
	}

	@Override
	public void onTrackPaused() {
		mUIHandler.sendEmptyMessage(PAUSED);
	}

	@Override
	public void onTrackStopped() {
		mUIHandler.sendEmptyMessage(STOPPED);
	}

	public void onChangeTrack() {
		mUIHandler.sendEmptyMessage(UPDATE_MINI_PLAYER);
	}


	public static Intent getMyServiceIntent(Context context) {
		Intent intent = new Intent(context, PlayMusicService.class);
		return intent;
	}

	public void createTrack(int index) {
		sendMessage(REQUEST_CREATE, index);
	}

	public void nextTrack() {
		mServiceHandler.sendEmptyMessage(REQUEST_NEXT);
	}

	public void previousTrack() {
		mServiceHandler.sendEmptyMessage(REQUEST_PREVIOUS);
	}

	public void startTrack() {
		mServiceHandler.sendEmptyMessage(REQUEST_START);
	}

	public void pauseTrack() {
		mServiceHandler.sendEmptyMessage(REQUEST_PAUSE);
	}

	public void seekTrack(int position) {
		sendMessage(REQUEST_SEEK, position);
	}

	public void requestPrepareAsync() {
		mServiceHandler.sendEmptyMessage(REQUEST_PREPARE);
	}

	public List<Track> getTracks() {
		return mMediaPlayerManager.getTracks();
	}

	public PlayMusicService setTracks(List<Track> tracks) {
		mMediaPlayerManager.setTracks(tracks);
		return this;
	}

	public MediaPlayerManager getMediaPlayerManager() {
		return mMediaPlayerManager;
	}

	public void sendMessage(int request, int index) {
		Message message = new Message();
		message.what = request;
		message.arg1 = index;
		mServiceHandler.sendMessage(message);
	}

	public class LocalBinder extends Binder {
		public PlayMusicService getService() {
			return PlayMusicService.this;
		}
	}

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case REQUEST_CREATE:
					create(msg.arg1);
					break;
				case REQUEST_NEXT:
					next();
					break;
				case REQUEST_PREVIOUS:
					previous();
					break;
				case REQUEST_START:
					start();
					break;
				case REQUEST_PAUSE:
					pause();
					break;
				case REQUEST_SEEK:
					seek(msg.arg1);
					break;
				case REQUEST_PREPARE:
					prepare();
					break;
				default:
					break;
			}
		}
	}
}
