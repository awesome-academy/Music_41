package com.cris.nvh.framgiaproject.screen.listtracks;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.ALL_AUDIO;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.ALL_MUSIC;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.ALTERNATIVE;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.AMBIENT;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.CLASSICAL;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.COUNTRY;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.DOWNLOAD;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.FAVORITES;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.LOCAL;
import static com.cris.nvh.framgiaproject.service.PlayMusicService.getMyServiceIntent;

public class ListTracksActivity extends AppCompatActivity implements View.OnClickListener, TracksAdapter.OnClickItemTrackListener {
	public static final String EXTRA_TRACK =
		"com.cris.nvh.framgiaproject.screen.listtracks.EXTRA_TRACKS";
	public static final String EXTRA_TITLE =
		"com.cris.nvh.framgiaproject.screen.home.EXTRA_TITLE";
	private static final int DEFAULT = -1;
	private RecyclerView mRecyclerView;
	private PlayMusicService mService;
	private ArrayList<Track> mTracks;
	private ServiceConnection mConnection;
	private ImageView mButtonBack;
	private ImageView mButtonSearch;
	private TextView mTextTitle;
	private String[] mTitles = {"All music", "All audio", "Alternative",
		"Ambient", "Classical", "Country", "Local", "Downloads", "Favorites"};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_tracks);
		bindToService();
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	@Override
	public void clickItemTrackListener(int position) {
	}

	@Override
	public void showDialogFeatureTrack(int position) {

	}

	@Override
	public void deleteFromFavorite(int position) {
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_view:
				super.onBackPressed();
				break;
			case R.id.image_search:
				break;
			default:
				break;
		}
	}

	public static Intent getListTracksActivityIntent(Context context, List<Track> tracks, int index) {
		Intent intent = new Intent(context, ListTracksActivity.class);
		intent.putParcelableArrayListExtra(EXTRA_TRACK,
			(ArrayList<? extends Parcelable>) tracks);
		intent.putExtra(EXTRA_TITLE, index);
		return intent;
	}

	private void bindToService() {
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
				PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
				mService = binder.getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				unbindService(mConnection);
			}
		};
		Intent serviceIntent = getMyServiceIntent(this);
		bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
	}

	private void initView() {
		mRecyclerView = findViewById(R.id.recycler_tracks);
		mButtonSearch = findViewById(R.id.image_search);
		mButtonBack = findViewById(R.id.image_view);
		mTextTitle = findViewById(R.id.text_title);
		mButtonBack.setImageResource(R.drawable.ic_back);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mTracks = getIntent().getParcelableArrayListExtra(EXTRA_TRACK);
		setTitle();
		TracksAdapter tracksAdapter = new TracksAdapter(mTracks, this);
		mRecyclerView.setAdapter(tracksAdapter);
		mButtonBack.setOnClickListener(this);
		mButtonSearch.setOnClickListener(this);
	}

	private void setTitle() {
		int intExtra = getIntent().getIntExtra(EXTRA_TITLE, DEFAULT);
		switch (intExtra) {
			case ALL_MUSIC:
				mTextTitle.setText(mTitles[ALL_MUSIC]);
				break;
			case ALL_AUDIO:
				mTextTitle.setText(mTitles[ALL_AUDIO]);
				break;
			case ALTERNATIVE:
				mTextTitle.setText(mTitles[ALTERNATIVE]);
				break;
			case AMBIENT:
				mTextTitle.setText(mTitles[AMBIENT]);
				break;
			case CLASSICAL:
				mTextTitle.setText(mTitles[CLASSICAL]);
				break;
			case COUNTRY:
				mTextTitle.setText(mTitles[COUNTRY]);
				break;
			case LOCAL:
				mTextTitle.setText(mTitles[LOCAL]);
				break;
			case DOWNLOAD:
				mTextTitle.setText(mTitles[DOWNLOAD]);
				break;
			case FAVORITES:
				mTextTitle.setText(mTitles[FAVORITES]);
				break;
			default:
				break;
		}
	}
}
