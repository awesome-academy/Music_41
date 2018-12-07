package com.cris.nvh.framgiaproject.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import static android.support.v4.app.NotificationCompat.EXTRA_PROGRESS;
import static com.cris.nvh.framgiaproject.service.DownloadService.EXTRA_ERROR;
import static com.cris.nvh.framgiaproject.service.DownloadService.EXTRA_TRACK_TITLE;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class DownloadReceiver extends ResultReceiver {
	private DownloadListener mDownloadListener;

	public DownloadReceiver(DownloadListener listener, Handler handler) {
		super(handler);
		mDownloadListener = listener;
	}

	@Override
	protected void onReceiveResult(@DownloadRequest int resultCode, Bundle resultData) {
		super.onReceiveResult(resultCode, resultData);
		switch (resultCode) {
			case DownloadRequest.PREPARE:
				String title = resultData.getString(EXTRA_TRACK_TITLE);
				mDownloadListener.onPrepare(title);
				break;
			case DownloadRequest.UPDATE:
				int progress = resultData.getInt(EXTRA_PROGRESS);
				mDownloadListener.onDownloading(progress);
				break;
			case DownloadRequest.FINISH:
				mDownloadListener.onSuccess();
				break;
			case DownloadRequest.ERROR:
				String message = resultData.getString(EXTRA_ERROR);
				mDownloadListener.onFailure(message);
				break;
			default:
				break;
		}
	}
}
