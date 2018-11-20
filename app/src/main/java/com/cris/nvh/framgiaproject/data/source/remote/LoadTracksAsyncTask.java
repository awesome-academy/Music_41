package com.cris.nvh.framgiaproject.data.source.remote;

import android.content.Context;
import android.os.AsyncTask;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.source.TracksDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class LoadTracksAsyncTask extends AsyncTask<String[], Integer, ArrayList<Genre>> {
	private TracksDataSource.LoadDataCallBack<Genre> mCallback;
	private static final String ARTWORK_URL = "artwork_url";
	private static final String ID = "id";
	private static final String KEY_USER = "user";
	private static final String TITLE = "title";
	private static final String TRACK = "track";
	private static final String COLLECTION = "collection";
	private static final String PERMALINK_URL = "permalink_url";
	private static final String DOWNLOADABLE = "downloadable";
	private static final String DOWNLOAD_URL = "download_url";
	private static final String DURATION = "duration";
	private static final String ARTIST_NAME = "full_name";
	private static final String ARTIST_IMAGE = "avatar_url";
	private static final String REQUEST_METHOD = "GET";
	private static final int CONNECT_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 10000;
	private Context mContext;

	public LoadTracksAsyncTask(Context context, TracksDataSource.LoadDataCallBack<Genre> callback) {
		mContext = context;
		mCallback = callback;
	}

	@Override
	protected ArrayList<Genre> doInBackground(String[]... strings) {
		ArrayList<Genre> genres = new ArrayList<>();
		for (int i = 0; i < strings[0].length; i++) {
			String jsonString = getJsonStringData((strings[0][i]));
			genres.add(convertJsonToObject(jsonString));
		}
		return genres;
	}

	@Override
	protected void onPostExecute(ArrayList<Genre> genres) {
		mCallback.onSuccess(genres);
	}

	private String getJsonStringData(String stringUrl) {
		StringBuilder builder = new StringBuilder();
		try {
			URL url = new URL(stringUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(REQUEST_METHOD);
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			connection.connect();
			InputStreamReader inputStream = new InputStreamReader(
					connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStream);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line).append("\n");
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (MalformedURLException e) {
			mCallback.onFail(mContext.getString(R.string.get_json_string_failed));
		} catch (IOException e) {
			mCallback.onFail(mContext.getString(R.string.get_json_string_failed));
		}
		return builder.toString();
	}

	private Genre convertJsonToObject(String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			return initGenre(jsonObject);
		} catch (JSONException e) {
			mCallback.onFail(mContext.getString(R.string.json_to_object_failed));
		}
		return null;
	}

	private Genre initGenre(JSONObject jsonGenre) {
		Genre genre = new Genre();
		try {
			JSONArray listTracks = jsonGenre.getJSONArray(COLLECTION);
			ArrayList<Track> tracks = new ArrayList<>();
			for (int i = 0; i < listTracks.length(); i++) {
				JSONObject jsonTrack = listTracks.getJSONObject(i).getJSONObject(TRACK);
				tracks.add(initTrack(jsonTrack));
			}
			genre.setTracks(tracks);
			return genre;
		} catch (JSONException e) {
			mCallback.onFail(mContext.getString(R.string.json_to_object_failed));
		}
		return null;
	}

	private Track initTrack(JSONObject jsonTrack) {
		Track track = new Track();
		try {
			track.setId(jsonTrack.getInt(ID));
			track.setTitle(jsonTrack.getString(TITLE));
			track.setArtworkUrl(jsonTrack.getString(ARTWORK_URL));
			track.setDownloadable(jsonTrack.getBoolean(DOWNLOADABLE));
			track.setDownloadUrl(jsonTrack.getString(DOWNLOAD_URL));
			track.setPermalinkUrl(jsonTrack.getString(PERMALINK_URL));
			track.setDuration(jsonTrack.getInt(DURATION));
			track.setArtist(jsonTrack.getJSONObject(KEY_USER).getString(ARTIST_NAME));
			track.setArtistImage(jsonTrack.getJSONObject(KEY_USER).getString(ARTIST_IMAGE));
			return track;
		} catch (JSONException e) {
			mCallback.onFail(mContext.getString(R.string.json_to_object_failed));
		}
		return null;
	}
}
