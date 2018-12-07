package com.cris.nvh.framgiaproject.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.cris.nvh.framgiaproject.BuildConfig;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.cris.nvh.framgiaproject.Constants.PARAMETER_ID;
import static com.cris.nvh.framgiaproject.service.DownloadRequest.ERROR;
import static com.cris.nvh.framgiaproject.service.DownloadRequest.FINISH;
import static com.cris.nvh.framgiaproject.service.DownloadRequest.PREPARE;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class DownloadService extends IntentService implements DownloadListener {
	public static final String EXTRA_DOWNLOAD_TRACK =
		"com.cris.nvh.framgiaproject.service.DownloadService";
	public static final String EXTRA_TRACK_TITLE =
		"com.cris.nvh.framgiaproject.service.EXTRA_TITLE";
	public static final String EXTRA_ERROR =
		"com.cris.nvh.framgiaproject.service.EXTRA_ERROR";
	private static final String TAG = "DownloadService";
	private static final String ROOT_FOLDER = "storage/emulated/0/download/";
	private static final String MP3_FORMAT = ".mp3";
	private static final String LOACATION_HEADER = "Location";
	private static final String PERCENT = "%";
	private static final int NOTIFICATION_ID = 10;
	private static final int INVALID = -1;
	private static final int OFFSET = 0;
	private static final int PROGRESS_MAX = 100;
	private static final int PERCENT_UNIT = 100;
	private static final int SIZE_UNIT = 1024;
	private static final int COMPLETE = 99;
	private NotificationManager mNotificationManager;
	private Notification.Builder mBuilder;
	private ResultReceiver mResultReceiver;

	public DownloadService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, R.string.notify_downloading, Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Track track = intent.getParcelableExtra(EXTRA_DOWNLOAD_TRACK);
		String urlDownload = new StringBuilder()
			.append(track.getDownloadUrl())
			.append(PARAMETER_ID)
			.append(BuildConfig.API_KEY)
			.toString();
		mResultReceiver = new DownloadReceiver(this, new Handler(Looper.getMainLooper()));
		try {
			URL url = new URL(urlDownload);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			boolean redirect = false;
			int status = connection.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
			}
			if (redirect) {
				String newUrl = connection.getHeaderField(LOACATION_HEADER);
				connection = (HttpURLConnection) new URL(newUrl).openConnection();
			}
			InputStream input = new BufferedInputStream(connection.getInputStream());
			downloadFile(input, track.getTitle(), connection.getContentLength());
			input.close();
			connection.disconnect();
		} catch (IOException e) {
			sendRequest(EXTRA_ERROR, e.getMessage(), ERROR);
		}
	}

	@Override
	public void onPrepare(String title) {
		Toast.makeText(this,
			new StringBuilder().append(R.string.notify_downloading).append(title),
			Toast.LENGTH_LONG).show();
		createNotification(title);
	}

	@Override
	public void onDownloading(int progress) {
		updateNotification(progress);
	}

	@Override
	public void onSuccess() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mBuilder.setProgress(0, 0, false)
				.setContentText(getString(R.string.notify_download_complete))
				.setOngoing(false)
				.setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
				.setPriority(Notification.PRIORITY_MAX);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}

	@Override
	public void onFailure(String message) {
		Toast.makeText(this, R.string.error_download, Toast.LENGTH_SHORT).show();
	}

	public static Intent getDownloadIntent(Context context, Track track) {
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(EXTRA_DOWNLOAD_TRACK, track);
		return intent;
	}

	private void downloadFile(InputStream input, String title, int fileLength) {
		try {
			String fileName = new StringBuilder()
				.append(ROOT_FOLDER)
				.append(title)
				.append(MP3_FORMAT)
				.toString();
			OutputStream output = new FileOutputStream(fileName);
			byte data[] = new byte[SIZE_UNIT];
			long total = 0;
			int count;
			sendRequest(EXTRA_TRACK_TITLE, title, PREPARE);
			while ((count = input.read(data)) != INVALID) {
				total += count;
				int progress = (int) (total * PERCENT_UNIT / fileLength);
				updateNotification(progress);
				if (progress >= COMPLETE) sendRequest(null, null, FINISH);
				output.write(data, OFFSET, count);
			}
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.fromFile(new File(fileName))));
			output.flush();
			output.close();
		} catch (IOException e) {
			sendRequest(EXTRA_ERROR, e.getMessage(), ERROR);
		}
	}

	private void initNotification() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new Notification.Builder(this);
		mBuilder.setSmallIcon(R.drawable.ic_download)
			.setContentText(getString(R.string.notify_downloading))
			.setOngoing(true);
	}

	private void createNotification(String title) {
		mBuilder.setContentTitle(title);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}

	private void updateNotification(int progress) {
		String contentText = new StringBuilder()
			.append(getString(R.string.notify_downloading))
			.append(String.valueOf(progress))
			.append(PERCENT)
			.toString();
		mBuilder.setContentText(contentText)
			.setProgress(PROGRESS_MAX, progress, false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}

	private void sendRequest(String key, String value, @DownloadRequest int requestCode) {
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		mResultReceiver.send(requestCode, bundle);
	}
}
