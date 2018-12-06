package com.cris.nvh.framgiaproject.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public final class FavoriteDbContract {

	public static class FavoriteEntry implements BaseColumns {
		public static final String TABLE_NAME = "favorite";
		public static final String COLUMN_NAME_TRACK_ID = "track_id";
		public static final String COLUMN_NAME_IS_OFFLINE = "is_offline";
		public static final String COLUMN_NAME_ARTWORK_URL = "artwork_url";
		public static final String COLUMN_NAME_USER_NAME = "artist";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_DOWNLOADABLE = "downloadable";
		public static final String COLUMN_NAME_DOWNLOAD_URL = "download_url";
		public static final String COLUMN_NAME_SOURCE = "source";
	}
}
