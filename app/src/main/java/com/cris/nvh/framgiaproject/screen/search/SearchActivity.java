package com.cris.nvh.framgiaproject.screen.search;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cris.nvh.framgiaproject.BuildConfig;
import com.cris.nvh.framgiaproject.LoadMoreAbstract;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.local.TracksLocalDataSource;
import com.cris.nvh.framgiaproject.data.source.remote.TracksRemoteDataSource;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.util.List;

import static com.cris.nvh.framgiaproject.Constants.BASE_URL_TRACK;
import static com.cris.nvh.framgiaproject.Constants.LIMIT;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_ID;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_LIMIT;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_OFFSET;
import static com.cris.nvh.framgiaproject.Constants.PARAMETER_SEARCH;
import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.getPlayActivityIntent;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SearchActivity extends LoadMoreAbstract implements SearchContract.View,
	TracksAdapter.OnClickItemTrackListener, View.OnClickListener, TextWatcher {
	public static final String EXTRA_SEARCH =
		"com.cris.nvh.framgiaproject.screen.home.EXTRA_SEARCH";
	private ImageView mButtonSearch;
	private EditText mEditSearch;
	private ImageView mButtonBack;
	private PlayMusicService mService;
	private ServiceConnection mConnection;
	private TracksAdapter mTrackAdapter;
	private int mOffset;
	private String mKeyword;
	private List<Track> mTracks;
	private SearchContract.Presenter mPresenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initPresenter();
		initConnection();
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mService != null) unbindService(mConnection);
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

	@Override
	public void showResult(List<Track> tracks) {
		Intent serviceIntent = PlayMusicService.getMyServiceIntent(SearchActivity.this);
		if (mService == null)
			bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
		if (mOffset == 0) {
			mTrackAdapter.updateTracks(tracks);
			mTracks = tracks;
			return;
		}
		mTrackAdapter.addTracks(tracks);
		mTracks.addAll(tracks);
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void showNoResult(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void loadMoreData() {
		mIsScrolling = false;
		mProgressBar.setVisibility(View.VISIBLE);
		mPresenter.searchTracks(initSearchApi(mKeyword, ++mOffset));
	}

	@Override
	public void initViewLoadMore() {
		mLinearLayoutManager = new LinearLayoutManager(this);
		mTrackAdapter = new TracksAdapter(this);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		mRecyclerView.setAdapter(mTrackAdapter);
		mProgressBar.setVisibility(View.GONE);
		setLoadMore();
	}

	@Override
	public void clickItemTrackListener(int position) {
		if (mService != null) {
			startActivity(getPlayActivityIntent(this));
			mService.setTracks(mTracks);
			mService.createTrack(position);
		}
	}

	@Override
	public void showDialogFeatureTrack(int position) {
	}

	@Override
	public void deleteFromFavorite(int position) {
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	@Override
	public void onTextChanged(CharSequence keyword, int i, int i1, int i2) {
		mOffset = 0;
		mKeyword = keyword.toString();
		mPresenter.searchTracks(initSearchApi(mKeyword, 0));
	}

	@Override
	public void afterTextChanged(Editable editable) {
	}

	public static Intent getSearchActivityIntent(Context context, String value) {
		Intent intent = new Intent(context, SearchActivity.class);
		intent.putExtra(EXTRA_SEARCH, value);
		return intent;
	}

	private void initConnection() {
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
	}

	private void initView() {
		mButtonSearch = findViewById(R.id.image_search);
		mEditSearch = findViewById(R.id.edit_search);
		mButtonBack = findViewById(R.id.image_view);
		mRecyclerView = findViewById(R.id.recycler_search);
		mButtonBack.setImageResource(R.drawable.ic_back);
		mProgressBar = findViewById(R.id.progress_search_more);
		mButtonSearch.setOnClickListener(this);
		mButtonBack.setOnClickListener(this);
		mEditSearch.addTextChangedListener(this);
		initViewLoadMore();
	}

	private String initSearchApi(String keyword, int offset) {
		return new StringBuilder().append(BASE_URL_TRACK)
			.append(PARAMETER_ID)
			.append(BuildConfig.API_KEY)
			.append(PARAMETER_SEARCH)
			.append(keyword)
			.append(PARAMETER_LIMIT)
			.append(String.valueOf(LIMIT * 2))
			.append(PARAMETER_OFFSET)
			.append(String.valueOf(offset))
			.toString();
	}

	private void initPresenter() {
		TrackRepository repository = TrackRepository
			.getInstance(TracksLocalDataSource.getInstance(this),
				TracksRemoteDataSource.getInstance(this));
		mPresenter = new SearchPresenter(repository, this);
	}
}
