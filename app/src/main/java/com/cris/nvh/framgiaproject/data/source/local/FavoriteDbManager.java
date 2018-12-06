package com.cris.nvh.framgiaproject.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_ARTWORK_URL;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_DOWNLOADABLE;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_DOWNLOAD_URL;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_IS_OFFLINE;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_SOURCE;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_TITLE;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_TRACK_ID;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.COLUMN_NAME_USER_NAME;
import static com.cris.nvh.framgiaproject.data.source.local.FavoriteDbContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class FavoriteDbManager extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String COMMA = ",";
	public static final String DROP_IF_EXIST = "DROP TABLE IF EXISTS ";
	public static final String DATABASE_NAME = "FeedReader.db";
	private static final String LIKE_ARG = " LIKE ?";
	private static final String QUESTION_ARG = " = ?";
	private static final String OPEN_PARENTHESIS = " (";
	private static final String CLOSE_PARENTHESIS = ")";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String PRIMARY_KEY = " PRIMARY KEY";
	private static final String TEXT_TYPE = " TEXT";
	private static final String CREATE_TABLE = "CREATE TABLE ";
	private Context mContext;

	public FavoriteDbManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTable = append(CREATE_TABLE,
			TABLE_NAME, OPEN_PARENTHESIS,
			BaseColumns._ID, INTEGER_TYPE, PRIMARY_KEY, COMMA,
			COLUMN_NAME_TRACK_ID, TEXT_TYPE, COMMA,
			COLUMN_NAME_ARTWORK_URL, TEXT_TYPE, COMMA,
			COLUMN_NAME_USER_NAME, TEXT_TYPE, COMMA,
			COLUMN_NAME_TITLE, TEXT_TYPE, COMMA,
			COLUMN_NAME_DOWNLOADABLE, TEXT_TYPE, COMMA,
			COLUMN_NAME_DOWNLOAD_URL, TEXT_TYPE, COMMA,
			COLUMN_NAME_IS_OFFLINE, TEXT_TYPE, COMMA,
			COLUMN_NAME_SOURCE, TEXT_TYPE,
			CLOSE_PARENTHESIS);
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String deleteTable = append(DROP_IF_EXIST, TABLE_NAME);
		db.execSQL(deleteTable);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public void insertTrack(Track track, TracksDataSource.LoadDataCallBack<String> callback) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_TRACK_ID, track.getId());
		values.put(COLUMN_NAME_IS_OFFLINE, track.isOffline());
		values.put(COLUMN_NAME_TITLE, track.getTitle());
		values.put(COLUMN_NAME_USER_NAME, track.getArtist());
		values.put(COLUMN_NAME_ARTWORK_URL, track.getArtworkUrl());
		values.put(COLUMN_NAME_DOWNLOADABLE, track.isDownloadable());
		values.put(COLUMN_NAME_DOWNLOAD_URL, track.getDownloadUrl());
		if (track.isOffline())
			values.put(COLUMN_NAME_SOURCE, track.getPermalinkUrl());
		else values.put(COLUMN_NAME_SOURCE, track.getStreamUrl());
		long id = db.insert(TABLE_NAME, null, values);
		db.close();
		if (id > 0) {
			callback.onSuccess(Collections
				.singletonList(mContext.getString(R.string.add_favorite_successfull)));
			return;
		}
		callback.onFail(mContext.getString(R.string.add_favorite_failed));

	}

	public void getTracks(TracksDataSource.LoadDataCallBack<Track> callback) {
		SQLiteDatabase sqLiteDatabase = getReadableDatabase();
		String[] projection = {
			COLUMN_NAME_TRACK_ID,
			COLUMN_NAME_IS_OFFLINE,
			COLUMN_NAME_TITLE,
			COLUMN_NAME_USER_NAME,
			COLUMN_NAME_ARTWORK_URL,
			COLUMN_NAME_DOWNLOADABLE,
			COLUMN_NAME_DOWNLOAD_URL,
			COLUMN_NAME_SOURCE,
		};
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection,
			null, null, null, null, null);
		List<Track> tracks = new ArrayList<>();
		while (cursor.moveToNext()) {
			tracks.add(getTrack(cursor));
		}
		sqLiteDatabase.close();
		callback.onSuccess(tracks);
	}

	private Track getTrack(Cursor cursor) {
		Track track = new Track();
		long id = cursor.getInt(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_TRACK_ID));
		int isOffline = cursor.getInt(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_IS_OFFLINE));
		String title = cursor.getString(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
		String artist = cursor.getString(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_USER_NAME));
		String artworkUrl = cursor.getString(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_ARTWORK_URL));
		int downloadable = cursor.getInt(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_DOWNLOADABLE));
		String downloadUrl = cursor.getString(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_DOWNLOAD_URL));
		String sourceUrl = cursor.getString(
			cursor.getColumnIndexOrThrow(COLUMN_NAME_SOURCE));
		track.setId((int) id);
		track.setArtworkUrl(artworkUrl);
		track.setDownloadable(downloadable == 1);
		track.setDownloadUrl(downloadUrl);
		track.setOffline(isOffline == 1);
		track.setTitle(title);
		track.setArtist(artist);
		if (track.isOffline()) track.setPermalinkUrl(sourceUrl);
		else track.setStreamUrl(sourceUrl);
		return track;
	}

	public void deleteTrack(Track track, TracksDataSource.LoadDataCallBack<String> callback) {
		int trackId = track.getId();
		SQLiteDatabase db = getReadableDatabase();
		String selection = append(COLUMN_NAME_TRACK_ID, LIKE_ARG);
		String[] selectionArgs = {String.valueOf(trackId)};
		int deletedRows = db.delete(
			TABLE_NAME,
			selection,
			selectionArgs);
		db.close();
		if (deletedRows > 0) {
			callback.onSuccess(Collections
				.singletonList(mContext.getString(R.string.text_delete_success)));
			return;
		}
		callback.onFail(mContext.getString(R.string.delete_failed));
	}

	public boolean isFavoriteTrack(Track track) {
		long trackId = track.getId();
		SQLiteDatabase sqLiteDatabase = getReadableDatabase();
		String[] projections = {
			COLUMN_NAME_TRACK_ID,
		};
		String selections = append(COLUMN_NAME_TRACK_ID, QUESTION_ARG);
		String[] selectionArgs = {String.valueOf(trackId)};
		Cursor cursor = sqLiteDatabase.query(
			TABLE_NAME,
			projections,
			selections,
			selectionArgs,
			null,
			null,
			null);
		return (cursor.getCount() > 0);
	}

	private String append(String... strings) {
		StringBuilder builder = new StringBuilder();
		for (String string : strings) {
			builder.append(string);
		}
		return builder.toString();
	}
}
