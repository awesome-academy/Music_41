package com.cris.nvh.framgiaproject.screen.mymusic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.adapter.LibraryAdapter;
import com.cris.nvh.framgiaproject.adapter.TracksAdapter;
import com.cris.nvh.framgiaproject.data.model.Track;
import com.cris.nvh.framgiaproject.screen.home.HomeFragment;
import com.cris.nvh.framgiaproject.screen.listtracks.ListTracksActivity;
import com.cris.nvh.framgiaproject.screen.playing.PlayActivity;

import java.util.ArrayList;

import static com.cris.nvh.framgiaproject.screen.splash.SplashActivity.EXTRA_TRACKS;

public class MyMusicFragment extends Fragment implements View.OnClickListener,
		LibraryAdapter.OnClickItemListener, TracksAdapter.OnClickItemTrackListener {
	public static final String EXTRA_TRACK =
			"com.cris.nvh.framgiaproject.screen.mymusic.EXTRA_TRACK";
	private static final int DEFAULT_TOTAL_SONGS = 0;
	private static final String TAG = "DIALOG";
	private static String[] sOptions = {"Delete", "Add to favorite"};
	private RecyclerView mRecyclerViewLibrary;
	private RecyclerView mRecyclerViewRecent;
	private EditText mEditSearch;
	private ImageView mButtonSearch;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater
				.from(container.getContext())
				.inflate(R.layout.fragment_my_music, container, false);
		initView(view);
		return view;
	}

	@Override
	public void clickItemTrackListener(int position) {
		startActivity(getPlayActivityIntent(getActivity(), getLocalTracks().get(position)));
	}

	@Override
	public void showDialogFeatureTrack(int position) {
		OptionDialog optionDialog = new OptionDialog();
		optionDialog.show(getFragmentManager(), TAG);
	}

	@Override
	public void clickItem(int position) {
		startActivity(getListTracksActivityIntent(getActivity(), getLocalTracks()));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_search:
				startActivity(HomeFragment
						.getSearchActivityIntent(getActivity(), mEditSearch.getText().toString()));
				break;
			default:
				break;
		}
	}

	public static Intent getPlayActivityIntent(Context context, Track track) {
		Intent intent = new Intent(context, PlayActivity.class);
		return intent.putExtra(EXTRA_TRACK, track);
	}

	public static Intent getListTracksActivityIntent(Context context, ArrayList<Track> tracks) {
		Intent intent = new Intent(context, ListTracksActivity.class);
		return intent.putParcelableArrayListExtra(EXTRA_TRACKS, tracks);
	}

	public static MyMusicFragment newInstance() {
		MyMusicFragment myMusicFragment = new MyMusicFragment();
		return myMusicFragment;
	}

	private void initView(View view) {
		ArrayList<Track> tracks = getLocalTracks();
		mRecyclerViewLibrary = view.findViewById(R.id.recycler_library);
		mRecyclerViewRecent = view.findViewById(R.id.recycler_recent);
		mButtonSearch = view.findViewById(R.id.image_search);
		mEditSearch = view.findViewById(R.id.edit_search);
		mRecyclerViewLibrary.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerViewRecent.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerViewLibrary.setAdapter(new LibraryAdapter(new int[]{tracks.size(),
				DEFAULT_TOTAL_SONGS, DEFAULT_TOTAL_SONGS}, this));
		mRecyclerViewRecent.setAdapter(new TracksAdapter(tracks, this));
		mButtonSearch.setOnClickListener(this);
	}

	private ArrayList<Track> getLocalTracks() {
		if (getArguments() != null) {
			ArrayList<Track> tracks = getArguments().getParcelableArrayList(EXTRA_TRACKS);
			return tracks;
		}
		return null;
	}

	public static class OptionDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setItems(sOptions, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
						}
					})
					.create();
		}
	}
}
