package com.cris.nvh.framgiaproject.data.source.remote;

import android.os.AsyncTask;

import com.cris.nvh.framgiaproject.BuildConfig;
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
import java.util.List;

import static com.cris.nvh.framgiaproject.Constants.BASE_URL_TRACK;
import static com.cris.nvh.framgiaproject.Constants.NAME_STREAM;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_ID;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SearchAsyncTask extends AsyncTask<String, String, List<Track>> {
	private static final String ARTWORK_URL = "artwork_url";
	private static final String ID = "id";
	private static final String DURATION = "duration";
	private static final String KEY_USER = "user";
	private static final String KEY_USER_NAME = "username";
	private static final String TITLE = "title";
	private static final String DOWNLOADABLE = "downloadable";
	private static final String REQUEST_METHOD = "GET";
	private static final int CONNECT_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 10000;
	private TracksDataSource.LoadDataCallBack<Track> mCallback;

	public SearchAsyncTask(TracksDataSource.LoadDataCallBack<Track> callback) {
		mCallback = callback;
	}

	@Override
	protected List<Track> doInBackground(String... strings) {
		String response = getJsonStringData(strings[0]);
		return convertJson(response);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		mCallback.onFail(values[0]);
	}

	@Override
	protected void onPostExecute(List<Track> tracks) {
		super.onPostExecute(tracks);
		mCallback.onSuccess(tracks);
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
			publishProgress(e.getMessage());
		} catch (IOException e) {
			publishProgress(e.getMessage());
		}
		return builder.toString();
	}

	private Track initTrack(JSONObject trackInfo) {
		try {
			int id = trackInfo.getInt(ID);
			String title = trackInfo.getString(TITLE);
			int duration = trackInfo.getInt(DURATION);
			String artworkUrl = trackInfo.getString(ARTWORK_URL);
			String artist = trackInfo.getJSONObject(KEY_USER)
					.getString(KEY_USER_NAME);
			boolean isDownloadable = trackInfo.getBoolean(DOWNLOADABLE);
			Track track = new Track(id);
			track.setArtworkUrl(artworkUrl);
			track.setDownloadable(isDownloadable);
			track.setTitle(title);
			track.setArtist(artist);
			track.setDuration(duration);
			track.setStreamUrl(initStreamUrl(id));
			return track;
		} catch (JSONException e) {
			publishProgress(e.getMessage());
		}
		return null;
	}

	private String initStreamUrl(int id) {
		return new StringBuilder()
				.append(BASE_URL_TRACK)
				.append(Integer.valueOf(id))
				.append(NAME_STREAM)
				.append(PARAMETER_ID)
				.append(BuildConfig.API_KEY)
				.toString();
	}

	private List<Track> convertJson(String response) {
		List<Track> tracks = new ArrayList<>();
		try {
			JSONArray result = new JSONArray(response);
			for (int i = 0; i < result.length(); i++) {
				JSONObject trackInfo = result.getJSONObject(i);
				tracks.add(initTrack(trackInfo));
			}
		} catch (JSONException e) {
			publishProgress(e.getMessage());
		}
		return tracks;
	}
}
