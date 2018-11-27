package com.cris.nvh.framgiaproject.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class LocalTracksManager {
	private static final String NOT_MUSIC = " != 0";
	private static final String EQUAL = "=?";
	private static LocalTracksManager sLocalTracksManager;
	private Context mContext;

	public LocalTracksManager(Context context) {
		mContext = context;
	}

	public static LocalTracksManager getInstance(Context context) {
		if (sLocalTracksManager == null)
			sLocalTracksManager = new LocalTracksManager(context);
		return sLocalTracksManager;
	}

	public void getLocalTrackDetails(final TracksDataSource.LoadDataCallBack<Track> callback) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				loadLocal(callback);
			}
		};
		thread.start();
	}

	private void loadLocal(TracksDataSource.LoadDataCallBack<Track> callback) {
		ArrayList<Track> tracks = new ArrayList<>();
		String selection = MediaStore.Audio.Media.IS_MUSIC + NOT_MUSIC;
		String[] projection = {
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM_ID,
				MediaStore.Audio.Media.DURATION
		};
		Cursor cursor = mContext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projection,
				selection,
				null,
				null);
		if (cursor == null) {
			callback.onFail(mContext.getString(R.string.load_local_failed));
			return;
		}
		while (cursor.moveToNext()) {
			tracks.add(initLocalSong(cursor));
		}
		cursor.close();
		callback.onSuccess(tracks);
	}

	private String getAlbumArt(int albumId) {
		String[] projections = {MediaStore.Audio.Albums._ID,
				MediaStore.Audio.Albums.ALBUM_ART};
		String selection = new StringBuilder()
				.append(MediaStore.Audio.Albums._ID)
				.append(EQUAL)
				.toString();
		String[] selectionArgs = {String.valueOf(albumId)};
		Cursor cursorAlbum = mContext.getContentResolver()
				.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
						projections, selection,
						selectionArgs,
						null);
		if (cursorAlbum == null) return null;
		String artwork = null;
		if (cursorAlbum.moveToFirst()) {
			artwork = cursorAlbum.getString(
					cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
		}
		cursorAlbum.close();
		return artwork;
	}


	private Track initLocalSong(Cursor cursor) {
		int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
		int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
		int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
		int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
		int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
		int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
		Track track = new Track(Integer.parseInt(cursor.getString(idIndex)));
		track.setArtist(cursor.getString(artistIndex));
		track.setTitle(cursor.getString(titleIndex));
		track.setPermalinkUrl(cursor.getString(dataIndex));
		track.setArtworkUrl(getAlbumArt(cursor.getInt(albumIdIndex)));
		track.setDuration(Integer.parseInt(cursor.getString(durationIndex)));
		return track;
	}
}
