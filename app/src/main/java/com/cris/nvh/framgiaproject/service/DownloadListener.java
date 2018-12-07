package com.cris.nvh.framgiaproject.service;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public interface DownloadListener {
	void onPrepare(String title);

	void onDownloading(int progress);

	void onSuccess();

	void onFailure(String message);
}
