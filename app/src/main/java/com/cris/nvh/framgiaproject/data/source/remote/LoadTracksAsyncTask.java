package com.cris.nvh.framgiaproject.data.source.remote;

import android.os.AsyncTask;

import com.cris.nvh.framgiaproject.BuildConfig;
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
import java.util.List;

import static com.cris.nvh.framgiaproject.Constants.BASE_URL_TRACK;
import static com.cris.nvh.framgiaproject.Constants.NAME_STREAM;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_ID;

public class LoadTracksAsyncTask extends AsyncTask<String[], String, List<Genre>> {
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

	public LoadTracksAsyncTask(TracksDataSource.LoadDataCallBack<Genre> callback) {
		mCallback = callback;
	}

	@Override
	protected List<Genre> doInBackground(String[]... strings) {
		List<Genre> genres = new ArrayList<>();
		if (strings[0][1] != null) {
			for (int i = 0; i < strings[0].length; i++) {
				String jsonString = getJsonStringData((strings[0][i]));
				genres.add(convertJsonToObject(jsonString));
			}
			return genres;
		}
		String jsonString = getJsonStringData((strings[0][0]));
		genres.add(convertJsonToObject(jsonString));
		return genres;
	}

	@Override
	protected void onPostExecute(List<Genre> genres) {
		mCallback.onSuccess(genres);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		mCallback.onFail(values[0]);
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

	private Genre convertJsonToObject(String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			return initGenre(jsonObject);
		} catch (JSONException e) {
			publishProgress(e.getMessage());
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
			publishProgress(e.getMessage());
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
			track.setStreamUrl(initStreamUrl(jsonTrack.getInt(ID)));
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
}
