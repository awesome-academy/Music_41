package com.cris.nvh.framgiaproject.service;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	DownloadRequest.PREPARE, DownloadRequest.UPDATE,
	DownloadRequest.FINISH, DownloadRequest.ERROR
})
public @interface DownloadRequest {
	int PREPARE = 5;
	int UPDATE = 6;
	int FINISH = 7;
	int ERROR = 8;
}
