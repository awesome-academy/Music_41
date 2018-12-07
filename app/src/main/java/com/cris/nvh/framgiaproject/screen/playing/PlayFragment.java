package com.cris.nvh.framgiaproject.screen.playing;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.data.repository.TrackRepository;
import com.cris.nvh.framgiaproject.data.source.local.TracksLocalDataSource;
import com.cris.nvh.framgiaproject.data.source.remote.TracksRemoteDataSource;
import com.cris.nvh.framgiaproject.mediaplayer.MediaPlayerSetting;
import com.cris.nvh.framgiaproject.mediaplayer.PlayMusic;
import com.cris.nvh.framgiaproject.service.DownloadService;
import com.cris.nvh.framgiaproject.service.PlayMusicService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.cris.nvh.framgiaproject.adapter.GenreAdapter.ARTWORK_DEFAULT_SIZE;
import static com.cris.nvh.framgiaproject.adapter.GenreAdapter.ARTWORK_MAX_SIZE;
import static com.cris.nvh.framgiaproject.screen.playing.PlayActivity.UPDATE_SEEKBAR;

public class PlayFragment extends Fragment implements View.OnClickListener,
	PlayContract.View, SeekBar.OnSeekBarChangeListener, DialogInterface.OnClickListener {
	private static final int START_ANGLE = 0;
	private static final int START_POSITION = 0;
	private static final int END_ANGLE = 360;
	private static final int DURATION = 10000;
	private static final float PIVOT = 0.5f;
	private static final String ROOT_FOLDER = "storage/emulated/0/download/";
	private static final String MP3_FORMAT = ".mp3";
	private static final String NULL = "null";
	private static final String TIME_FORMAT = "%02d:%02d";
	private static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
	private static final long MESSAGE_UPDATE_DELAY = 1000;
	private static final int REQUEST_CODE = 283;
	private boolean mHasPermission;
	private ImageView mImageAlbum;
	private PlayMusicService mService;
	private ImageView mButtonBack;
	private ImageView mButtonDownload;
	private ImageView mButtonFavorite;
	private TextView mTextSinger;
	private TextView mTextSong;
	private ImageView mButtonPrevious;
	private ImageView mButtonNext;
	private ImageView mButtonChangeState;
	private ImageView mButtonShuffle;
	private ImageView mButtonLoop;
	private SeekBar mSeekBar;
	private TextView mTextCurrentPosition;
	private TextView mTextDuration;
	private Track mTrack;
	private Handler mHandler;
	private PlayContract.Presenter mPresenter;

	public static PlayFragment newInstance() {
		return new PlayFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(container.getContext())
			.inflate(R.layout.fragment_play, container, false);
		initPresenter();
		initView(view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mService != null)
			updateUI();
	}

	@Override
	public void onFail(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAddTracksSuccess(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_back_button:
				getActivity().onBackPressed();
				break;
			case R.id.image_download:
				checkPermission();
				if (mHasPermission && isAcceptDownload(mTrack.getTitle())) beginDownload();
				break;
			case R.id.image_loop:
				changeLoopType();
				break;
			case R.id.image_shuffle:
				changeShuffleType();
				break;
			case R.id.image_favorite:
				addToFavorites();
				break;
			case R.id.button_next:
				mService.nextTrack();
				break;
			case R.id.button_previous:
				mService.previousTrack();
				break;
			case R.id.button_change_state:
				if (!mService.isPlaying()) {
					play();
					return;
				}
				pause();
				break;
			default:
				break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
		if (fromUser)
			mService.seekTrack(i);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
	                                       int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE:
				if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					mHasPermission = true;
				} else checkPermission();
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int button) {
		switch (button) {
			case DialogInterface.BUTTON_POSITIVE:
				beginDownload();
				dialogInterface.dismiss();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				dialogInterface.dismiss();
				break;
			default:
				break;
		}
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public ImageView getButtonChangeState() {
		return mButtonChangeState;
	}

	public void setService(PlayMusicService service) {
		mService = service;
	}

	public void startLoading() {
		if (mPresenter.isFavoriteTrack(mService.getTracks().get(mService.getTrack())))
			mButtonFavorite.setImageResource(R.drawable.ic_favorite);
		else mButtonFavorite.setImageResource(R.drawable.ic_favorite_none);
		mSeekBar.setEnabled(false);
		mButtonChangeState.setClickable(false);
		mButtonNext.setClickable(false);
		mButtonPrevious.setClickable(false);
		mTrack = mService.getTracks().get(mService.getTrack());
		mTextSinger.setText(mTrack.getArtist());
		mTextSong.setText(mTrack.getTitle());
		mSeekBar.setProgress(START_POSITION);
		mTextCurrentPosition.setText(convertMilisecondToFormatTime(START_POSITION));
		mTextDuration.setText(convertMilisecondToFormatTime(START_POSITION));
		setImageAlbum(mTrack.getArtworkUrl());
		mButtonChangeState.setImageResource(R.drawable.ic_play);
	}

	public void loadingSuccess() {
		mSeekBar.setEnabled(true);
		int duration = mService.getDuration();
		mButtonChangeState.setClickable(true);
		mButtonPrevious.setClickable(true);
		mButtonNext.setClickable(true);
		mButtonChangeState.setImageResource(R.drawable.ic_pause);
		mSeekBar.setMax(duration);
		mTextDuration.setText(convertMilisecondToFormatTime(duration));
		mHandler.sendEmptyMessage(UPDATE_SEEKBAR);
	}

	public void requestUpdateSeekBar() {
		updateSeekBar();
	}

	private void initView(View view) {
		mHasPermission = false;
		mButtonBack = view.findViewById(R.id.image_back_button);
		mButtonDownload = view.findViewById(R.id.image_download);
		mButtonFavorite = view.findViewById(R.id.image_favorite);
		mTextSinger = view.findViewById(R.id.text_singer_name);
		mTextSong = view.findViewById(R.id.text_song_name);
		mButtonPrevious = view.findViewById(R.id.button_previous);
		mButtonNext = view.findViewById(R.id.button_next);
		mButtonChangeState = view.findViewById(R.id.button_change_state);
		mTextCurrentPosition = view.findViewById(R.id.text_current_position);
		mSeekBar = view.findViewById(R.id.seekbar);
		mTextDuration = view.findViewById(R.id.text_duration);
		mButtonShuffle = view.findViewById(R.id.image_shuffle);
		mButtonLoop = view.findViewById(R.id.image_loop);
		mImageAlbum = view.findViewById(R.id.image_album);
		mTextSong.setSelected(true);
		mTextSinger.setSelected(true);
		setListener();
	}

	private boolean isAcceptDownload(String title) {
		String fileName = new StringBuilder()
			.append(ROOT_FOLDER)
			.append(title)
			.append(MP3_FORMAT)
			.toString();
		File file = new File(fileName);
		if (!file.isDirectory() && file.exists()) {
			confirmDownload();
			return false;
		}
		return true;
	}

	private void confirmDownload() {
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setTitle(R.string.notify_file_exists)
			.setIcon(R.drawable.ic_download)
			.setPositiveButton(R.string.confirm_yes, this)
			.setNegativeButton(R.string.confirm_no, this)
			.create();
		dialog.show();
	}

	private void checkPermission() {
		if (ContextCompat
			.checkSelfPermission(getActivity(), PERMISSION) != PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
				new String[]{PERMISSION}, REQUEST_CODE);
			return;
		}
		mHasPermission = true;
	}

	private void setImageAlbum(String source) {
		Object uri;
		if (source != null && !source.equals(NULL)) {
			uri = source.replace(ARTWORK_DEFAULT_SIZE, ARTWORK_MAX_SIZE);
		} else {
			uri = R.drawable.default_album;
		}
		Glide.with(this)
			.load(uri)
			.apply(RequestOptions
				.circleCropTransform())
			.into(mImageAlbum);
	}

	private void setImageAnimation() {
		RotateAnimation rotate = new RotateAnimation(START_ANGLE, END_ANGLE,
			Animation.RELATIVE_TO_SELF, PIVOT, Animation.RELATIVE_TO_SELF, PIVOT);
		rotate.setDuration(DURATION);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setInterpolator(new LinearInterpolator());
		mImageAlbum.startAnimation(rotate);
	}

	private void setListener() {
		mButtonShuffle.setOnClickListener(this);
		mButtonLoop.setOnClickListener(this);
		mButtonPrevious.setOnClickListener(this);
		mButtonNext.setOnClickListener(this);
		mButtonChangeState.setOnClickListener(this);
		mButtonBack.setOnClickListener(this);
		mButtonFavorite.setOnClickListener(this);
		mSeekBar.setOnSeekBarChangeListener(this);
		mButtonDownload.setOnClickListener(this);
	}

	private void updateUI() {
		if (mPresenter.isFavoriteTrack(mService.getTracks().get(mService.getTrack())))
			mButtonFavorite.setImageResource(R.drawable.ic_favorite);
		else mButtonFavorite.setImageResource(R.drawable.ic_favorite_none);
		int index = mService.getTrack();
		mTrack = mService.getTracks().get(index);
		int duration = mService.getDuration();
		mTextSong.setText(mTrack.getTitle());
		mTextSinger.setText(mTrack.getArtist());
		mSeekBar.setMax(mService.getDuration());
		mSeekBar.setProgress(mService.getCurrrentPosition());
		mTextDuration.setText(convertMilisecondToFormatTime(duration));
		initLoopImage();
		initShuffleImage();
		updatePlayImage(mService.isPlaying());
		setImageAlbum(mTrack.getArtworkUrl());
		setImageAnimation();
		mHandler.sendEmptyMessage(UPDATE_SEEKBAR);
	}

	private void updatePlayImage(boolean isPlaying) {
		if (isPlaying) {
			mButtonChangeState.setImageResource(R.drawable.ic_pause);
			return;
		}
		mButtonChangeState.setImageResource(R.drawable.ic_play);
	}

	private void pause() {
		mButtonChangeState.setImageResource(R.drawable.ic_play);
		mService.pauseTrack();
	}

	private void play() {
		mButtonChangeState.setImageResource(R.drawable.ic_pause);
		int mediaStatus = mService.getMediaPlayerManager().getState();
		if (mediaStatus == PlayMusic.StatusPlayerType.STOPPED) {
			mService.requestPrepareAsync();
			return;
		}
		mService.startTrack();
	}

	private void changeLoopType() {
		int looptype = mService.getMediaPlayerManager().getLoopType();
		switch (looptype) {
			case MediaPlayerSetting.LoopType.NONE:
				mService.getMediaPlayerManager().setLoopType(MediaPlayerSetting.LoopType.ONE);
				mService.loop(true);
				break;
			case MediaPlayerSetting.LoopType.ONE:
				mService.getMediaPlayerManager().setLoopType(MediaPlayerSetting.LoopType.ALL);
				mService.loop(false);
				break;
			case MediaPlayerSetting.LoopType.ALL:
				mService.getMediaPlayerManager().setLoopType(MediaPlayerSetting.LoopType.NONE);
				mService.loop(false);
				break;
			default:
				break;
		}
		initLoopImage();
	}

	private void initLoopImage() {
		int looptype = mService.getMediaPlayerManager().getLoopType();
		switch (looptype) {
			case MediaPlayerSetting.LoopType.NONE:
				mButtonLoop.setImageResource(R.drawable.ic_loop_none);
				break;
			case MediaPlayerSetting.LoopType.ONE:
				mButtonLoop.setImageResource(R.drawable.ic_loop_one);
				break;
			case MediaPlayerSetting.LoopType.ALL:
				mButtonLoop.setImageResource(R.drawable.ic_loop_all);
				break;
			default:
				break;
		}
	}

	private void updateSeekBar() {
		int currentPosition = mService.getCurrrentPosition();
		mSeekBar.setProgress(currentPosition);
		mTextCurrentPosition.setText(convertMilisecondToFormatTime(currentPosition));
		mHandler.sendEmptyMessageDelayed(UPDATE_SEEKBAR, MESSAGE_UPDATE_DELAY);
	}

	private void changeShuffleType() {
		int shuffleType = mService.getMediaPlayerManager().getShuffleType();
		switch (shuffleType) {
			case MediaPlayerSetting.ShuffleType.OFF:
				mService.getMediaPlayerManager().setShuffleType(MediaPlayerSetting.ShuffleType.ON);
				break;
			case MediaPlayerSetting.ShuffleType.ON:
				mService.getMediaPlayerManager().setShuffleType(MediaPlayerSetting.ShuffleType.OFF);
				break;
			default:
				break;
		}
		initShuffleImage();
	}

	private void initShuffleImage() {
		int shuffleType = mService.getMediaPlayerManager().getShuffleType();
		switch (shuffleType) {
			case MediaPlayerSetting.ShuffleType.OFF:
				mButtonShuffle.setImageResource(R.drawable.ic_shuffle_off);
				break;
			case MediaPlayerSetting.ShuffleType.ON:
				mButtonShuffle.setImageResource(R.drawable.ic_shuffle_on);
				break;
			default:
				break;
		}
	}

	private void beginDownload() {
		Intent intent = DownloadService.getDownloadIntent(getContext(), mTrack);
		getActivity().startService(intent);
	}

	private String convertMilisecondToFormatTime(long msec) {
		return String.format(TIME_FORMAT,
			TimeUnit.MILLISECONDS.toMinutes(msec) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
			TimeUnit.MILLISECONDS.toSeconds(msec) -
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec)));
	}

	private void addToFavorites() {
		mTrack = mService.getTracks().get(mService.getTrack());
		if (mPresenter.isFavoriteTrack(mTrack)) {
			mButtonFavorite.setImageResource(R.drawable.ic_favorite_none);
			mPresenter.deleteFavoriteTrack(mTrack);
			return;
		}
		mButtonFavorite.setImageResource(R.drawable.ic_favorite);
		mPresenter.addFavoriteTracks(mTrack);
	}

	private void initPresenter() {
		TrackRepository repository = TrackRepository
			.getInstance(TracksLocalDataSource.getInstance(getActivity()),
				TracksRemoteDataSource.getInstance(getActivity()));
		mPresenter = new PlayPresenter(repository, this);
	}
}
