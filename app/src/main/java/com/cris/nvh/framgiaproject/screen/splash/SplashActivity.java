package com.cris.nvh.framgiaproject.screen.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cris.nvh.framgiaproject.BuildConfig;
import com.cris.nvh.framgiaproject.MainActivity;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.local.TracksLocalDataSource;
import com.cris.nvh.framgiaproject.data.source.remote.TracksRemoteDataSource;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.cris.nvh.framgiaproject.Constants.BASE_URL_GENRES;
import static com.cris.nvh.framgiaproject.Constants.CLIENT_ID;
import static com.cris.nvh.framgiaproject.Constants.LIMIT;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_LIMIT;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
	public static final String ACTION_LOAD_API = "ACTION_LOAD_API";
	public static final String EXTRA_TRACKS =
		"com.cris.nvh.framgiaproject.screen.splash.EXTRA_TRACKS";
	public static final String EXTRA_GENRES =
		"com.cris.nvh.framgiaproject.screen.splash.EXTRA_GENRES";
	private static final int REQUEST_CODE = 10;
	private static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
	private SplashContract.Presenter mSplashPresenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		requestPermission();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
				loadData();
			} else {
				Toast.makeText(this, R.string.permisson_not_granted, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onLoadLocalTrackSuccess(ArrayList<Track> tracks) {
		startActivity(getIntent(this, tracks));
	}

	@Override
	public void onLoadApiFailed(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadLocalTrackFailed(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onloadApiSuccess(ArrayList<Genre> genres) {
		Intent intent = new Intent(ACTION_LOAD_API)
			.putExtra(EXTRA_GENRES, genres);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		finish();
	}

	public static Intent getIntent(Context context, ArrayList<Track> tracks) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.putParcelableArrayListExtra(EXTRA_TRACKS, tracks);
		return intent;
	}

	private void requestPermission() {
		if (ContextCompat.checkSelfPermission(this, PERMISSION) != PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, REQUEST_CODE);
			return;
		}
		loadData();
	}

	private void loadData() {
		mSplashPresenter = new SplashPresenter(this);
		mSplashPresenter.initRepository(TrackRepository
			.getInstance(TracksLocalDataSource.getInstance(this),
				TracksRemoteDataSource.getInstance(this)));
		if (isNetworkConnected()) {
			String[] urls = generateApiUrl();
			mSplashPresenter.loadApiData(urls);
		} else {
			Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
		}
		mSplashPresenter.loadLocalTracks(this);
	}

	private String[] generateApiUrl() {
		String[] genres = {Anotation.ListGenres.ALL_MUSIC, Anotation.ListGenres.ALL_AUDIO,
			Anotation.ListGenres.ALTERNATIVE, Anotation.ListGenres.AMBIENT,
			Anotation.ListGenres.CLASSICAL, Anotation.ListGenres.COUNTRY
		};
		String[] urls = initGenres(genres);
		return urls;
	}

	private String[] initGenres(String[] genres) {
		String[] urls = new String[genres.length];
		for (int i = 0; i < genres.length; i++) {
			urls[i] = new StringBuffer()
				.append(BASE_URL_GENRES).append(genres[i])
				.append(CLIENT_ID).append(BuildConfig.API_KEY)
				.append(PARAMETER_LIMIT).append(LIMIT)
				.toString();
		}
		return urls;
	}

	private boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager)
			getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo() == null ? false : true;
	}
}
