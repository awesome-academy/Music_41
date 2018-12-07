package com.cris.nvh.framgiaproject.screen.listtracks;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cris.nvh.framgiaproject.BuildConfig;
import com.cris.nvh.framgiaproject.LoadMoreAbstract;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.local.TracksLocalDataSource;
import com.cris.nvh.framgiaproject.data.source.remote.TracksRemoteDataSource;
import com.cris.nvh.framgiaproject.screen.splash.Anotation;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

import static com.cris.nvh.framgiaproject.Constants.BASE_URL_GENRES;
import static com.cris.nvh.framgiaproject.Constants.CLIENT_ID;
import static com.cris.nvh.framgiaproject.Constants.LIMIT;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_LIMIT;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_OFFSET;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.ALL_AUDIO;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.ALL_MUSIC;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.ALTERNATIVE;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.AMBIENT;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.CLASSICAL;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.COUNTRY;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.DOWNLOAD;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.FAVORITES;
import static com.cris.nvh.framgiaproject.screen.listtracks.TrackTypes.LOCAL;
import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.getPlayActivityIntent;
import static com.cris.nvh.framgiaproject.screen.search.SearchActivity.getSearchActivityIntent;
import static com.cris.nvh.framgiaproject.service.PlayMusicService.getMyServiceIntent;

public class ListTracksActivity extends LoadMoreAbstract implements View.OnClickListener,
	TracksAdapter.OnClickItemTrackListener, SwipeRefreshLayout.OnRefreshListener, ListTracksContract.View {
	public static final String EXTRA_TRACK =
		"com.cris.nvh.framgiaproject.screen.listtracks.EXTRA_TRACKS";
	public static final String EXTRA_TITLE =
		"com.cris.nvh.framgiaproject.screen.home.EXTRA_TITLE";
	private static final int DEFAULT = -1;
	private static final int START_OFFSET = 0;
	private int mOffset;
	private PlayMusicService mService;
	private ArrayList<Track> mTracks;
	private TracksAdapter mAdapter;
	private ServiceConnection mConnection;
	private ImageView mButtonBack;
	private ImageView mButtonSearch;
	private TextView mTextTitle;
	private EditText mEditSearch;
	private String mGenreKey;
	private String mGenreApi;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListTracksContract.Presenter mPresenter;
	private String[] mTitles = {"All music", "All audio", "Alternative",
		"Ambient", "Classical", "Country", "Local", "Downloads", "Favorites"};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_tracks);
		initView();
		initViewLoadMore();
		initData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindToService();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(mConnection);
	}

	@Override
	public void clickItemTrackListener(int position) {
		if (mService != null) {
			mService.setTracks(mTracks);
			mService.createTrack(position);
			startActivity(getPlayActivityIntent(this));
		}
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
				startActivity(getSearchActivityIntent(this,
					mEditSearch.getText().toString()));
				break;
			default:
				break;
		}
	}

	@Override
	public void loadMoreData() {
		mIsScrolling = false;
		mProgressBar.setVisibility(View.VISIBLE);
		String api = initGenreApi(mGenreKey, ++mOffset);
		mPresenter.getTracks(api);
	}

	@Override
	public void initViewLoadMore() {
		mTracks = new ArrayList<>();
		mAdapter = new TracksAdapter(mTracks, this);
		mRecyclerView.setAdapter(mAdapter);
		mLinearLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		mProgressBar = findViewById(R.id.proress_load_more);
		mProgressBar.setVisibility(View.GONE);
		setLoadMore();
	}

	@Override
	public void onRefresh() {
		if (mGenreKey != null && !mGenreKey.isEmpty()) {
			mOffset = START_OFFSET;
			String api = initGenreApi(mGenreKey, mOffset);
			mPresenter.getTracks(api);
		}
	}

	@Override
	public void onLoadTracksSuccess(List<Track> tracks) {
		if (mOffset == START_OFFSET) {
			mAdapter.updateTracks(tracks);
			mSwipeRefreshLayout.setRefreshing(false);
		} else {
			mAdapter.addTracks(tracks);
			mProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoadTracksFail(String message) {
		Toast.makeText(ListTracksActivity.this, message, Toast.LENGTH_SHORT).show();
		mProgressBar.setVisibility(View.GONE);
		mSwipeRefreshLayout.setRefreshing(false);
	}

	public static Intent getListTracksActivityIntent(Context context, List<Track> tracks, int index) {
		Intent intent = new Intent(context, ListTracksActivity.class);
		intent.putParcelableArrayListExtra(EXTRA_TRACK,
			(ArrayList<? extends Parcelable>) tracks);
		intent.putExtra(EXTRA_TITLE, index);
		return intent;
	}

	private void initPresenter() {
		TrackRepository repository = TrackRepository
			.getInstance(TracksLocalDataSource.getInstance(this),
				TracksRemoteDataSource.getInstance(this));
		mPresenter = new ListTracksPresenter(repository, this);
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
		initPresenter();
		mRecyclerView = findViewById(R.id.recycler_search);
		mButtonSearch = findViewById(R.id.image_search);
		mButtonBack = findViewById(R.id.image_view);
		mTextTitle = findViewById(R.id.text_title);
		setTitle();
		mEditSearch = findViewById(R.id.edit_search);
		mButtonBack.setImageResource(R.drawable.ic_back);
		mProgressBar = findViewById(R.id.proress_load_more);
		mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
		mButtonBack.setOnClickListener(this);
		mButtonSearch.setOnClickListener(this);
		mSwipeRefreshLayout.setOnRefreshListener(this);
	}

	private void setTitle() {
		int intExtra = getIntent().getIntExtra(EXTRA_TITLE, DEFAULT);
		switch (intExtra) {
			case ALL_MUSIC:
				mGenreKey = Anotation.ListGenres.ALL_MUSIC;
				mTextTitle.setText(mTitles[ALL_MUSIC]);
				break;
			case ALL_AUDIO:
				mGenreKey = Anotation.ListGenres.ALL_AUDIO;
				mTextTitle.setText(mTitles[ALL_AUDIO]);
				break;
			case ALTERNATIVE:
				mGenreKey = Anotation.ListGenres.ALTERNATIVE;
				mTextTitle.setText(mTitles[ALTERNATIVE]);
				break;
			case AMBIENT:
				mGenreKey = Anotation.ListGenres.AMBIENT;
				mTextTitle.setText(mTitles[AMBIENT]);
				break;
			case CLASSICAL:
				mGenreKey = Anotation.ListGenres.CLASSICAL;
				mTextTitle.setText(mTitles[CLASSICAL]);
				break;
			case COUNTRY:
				mGenreKey = Anotation.ListGenres.COUNTRY;
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

	private String initGenreApi(String genre, int offset) {
		return new StringBuffer()
			.append(BASE_URL_GENRES).append(genre)
			.append(CLIENT_ID).append(BuildConfig.API_KEY)
			.append(PARAMETER_LIMIT).append(LIMIT*2)
			.append(PARAMETER_OFFSET).append(offset)
			.toString();
	}

	private void initData() {
		mOffset++;
		Intent intent = getIntent();
		if (!mTextTitle.getText().equals(mTitles[LOCAL])) {
			mGenreApi = initGenreApi(mGenreKey, mOffset);
			mPresenter.getTracks(mGenreApi);
			return;
		}
		mTracks = intent.getParcelableArrayListExtra(EXTRA_TRACK);
		mAdapter = new TracksAdapter(mTracks, this);
		mRecyclerView.setAdapter(mAdapter);
	}
}
