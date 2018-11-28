package com.cris.nvh.framgiaproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.cris.nvh.framgiaproject.mediaplayer.MediaPlayerManager;

public class PlayMusicService extends Service implements IService,
		MediaPlayerManager.OnLoadingTrackListener {

	private static final int REQUEST_CREATE = 1;
	private static final int REQUEST_CHANGE_SONG = 2;
	private static final int REQUEST_START = 3;
	private static final int REQUEST_PAUSE = 4;
	private static final int REQUEST_SEEK = 5;
	private static final int REQUEST_PREPARE = 6;
	private static final String WORKER_THREAD_NAME = "ServiceThread";
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
	public void changeTrack() {
		mMediaPlayerManager.changeTrack();
	}

	public static void setUIHandler(Handler uiHandler) {
		mUIHandler = uiHandler;
	}

	@Override
	public void onStartLoading(int index) {
	}

	@Override
	public void onLoadingFail(String message) {
	}

	@Override
	public void onLoadingSuccess() {
	}

	@Override
	public void onTrackPaused() {
	}

	@Override
	public void onTrackStopped() {
	}

	public void createTrack(int index) {
		Message message = new Message();
		message.what = REQUEST_CREATE;
		message.arg1 = index;
		mServiceHandler.sendMessage(message);
	}

	public void nextTrack(int index){
	}

	public void previousTrack(int index){
	}

	public void startTrack() {
		mServiceHandler.sendEmptyMessage(REQUEST_START);
	}

	public void pauseTrack() {
		mServiceHandler.sendEmptyMessage(REQUEST_PAUSE);
	}

	public void seekTrack(int position) {
		Message message = new Message();
		message.arg1 = position;
		message.what = REQUEST_SEEK;
		mServiceHandler.sendMessage(message);
	}

	public void requestPrepareAsync() {
		mServiceHandler.sendEmptyMessage(REQUEST_PREPARE);
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
		}
	}
}
