package com.cris.nvh.framgiaproject.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Genre;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
	public static final String ARTWORK_DEFAULT_SIZE = "large";
	public static final String ARTWORK_MAX_SIZE = "t500x500";
	private static final int ALL_MUSIC_INDEX = 0;
	private static final int ALL_AUDIO_INDEX = 1;
	private static final int ALL_ALTERNATIVE_INDEX = 2;
	private static final int ALL_AMBIENT_INDEX = 3;
	private static final int ALL_CLASSICAL_INDEX = 4;
	private static final int ALL_COUNTRY_INDEX = 5;
	private static final String NULL = "null";
	private GenreClickListener mListener;
	private List<Genre> mGenres;

	public GenreAdapter(List<Genre> genres, GenreClickListener listener) {
		mListener = listener;
		mGenres = genres;
	}

	@Override
	public GenreViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater
				.from(viewGroup.getContext())
				.inflate(R.layout.layout_genre, viewGroup, false);
		return new GenreViewHolder(view);
	}

	@Override
	public void onBindViewHolder(GenreViewHolder genreViewHolder, int i) {
		genreViewHolder.bindData(mGenres.get(i), mListener);
	}

	@Override
	public int getItemCount() {
		return mGenres == null ? 0 : mGenres.size();
	}

	public static class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView mTextView;
		private RecyclerView mRecyclerView;
		private GenreClickListener mListener;

		@Override
		public void onClick(View view) {
			mListener.onItemClick(getAdapterPosition());
		}

		public GenreViewHolder(View itemView) {
			super(itemView);
			mTextView = itemView.findViewById(R.id.text_genre);
			mRecyclerView = itemView.findViewById(R.id.recycler_tracks);
		}

		public void bindData(Genre genre, GenreClickListener listener) {
			setGenreText();
			mRecyclerView.setAdapter(new ListTracksAdapter(genre.getTracks(),
					listener, getAdapterPosition()));
			mRecyclerView.setHasFixedSize(true);
			mRecyclerView.setLayoutManager(new LinearLayoutManager(
					itemView.getContext(), HORIZONTAL, false));
			mListener = listener;
			itemView.setOnClickListener(this);
		}

		public void setGenreText() {
			switch (getAdapterPosition()) {
				case ALL_MUSIC_INDEX:
					mTextView.setText(R.string.all_music);
					break;
				case ALL_AUDIO_INDEX:
					mTextView.setText(R.string.all_audio);
					break;
				case ALL_ALTERNATIVE_INDEX:
					mTextView.setText(R.string.alternate);
					break;
				case ALL_AMBIENT_INDEX:
					mTextView.setText(R.string.ambient);
					break;
				case ALL_CLASSICAL_INDEX:
					mTextView.setText(R.string.classical);
					break;
				case ALL_COUNTRY_INDEX:
					mTextView.setText(R.string.country);
					break;
			}
		}

		public static class ListTracksAdapter extends RecyclerView.Adapter<ListTracksAdapter.TrackViewHolder> {
			private List<Track> mTracks;
			private GenreClickListener mListener;
			private int mGenreIndex;

			public ListTracksAdapter(List<Track> tracks, GenreClickListener listener, int index) {
				mListener = listener;
				mTracks = tracks;
				mGenreIndex = index;
			}

			@Override
			public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
				View view = LayoutInflater
						.from(viewGroup.getContext())
						.inflate(R.layout.layout_tracks, viewGroup, false);
				return new TrackViewHolder(view);
			}

			@Override
			public void onBindViewHolder(TrackViewHolder trackViewHolder, int i) {
				trackViewHolder.bindData(mTracks.get(i), mListener, mGenreIndex);
			}

			@Override
			public int getItemCount() {
				return mTracks == null ? 0 : mTracks.size();
			}

			public static class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
				private ImageView mImageView;
				private GenreClickListener mListener;
				private int mGenreIndex;

				@Override
				public void onClick(View view) {
					mListener.onTrackClick(mGenreIndex, getAdapterPosition());
				}

				public TrackViewHolder(View itemView) {
					super(itemView);
					mImageView = itemView.findViewById(R.id.image_track);
				}

				public void bindData(Track track, GenreClickListener listener, int genreIndex) {
					mListener = listener;
					mGenreIndex = genreIndex;
					itemView.setOnClickListener(this);
					track.setArtworkUrl(track.getArtworkUrl()
							.replace(ARTWORK_DEFAULT_SIZE, ARTWORK_MAX_SIZE));
					String imageUrl = track.getArtworkUrl();
					if (!imageUrl.equals(NULL)) {
						Glide.with(itemView)
								.load(imageUrl)
								.into(mImageView);
						return;
					}
					Glide.with(itemView)
							.load(R.drawable.default_artwork)
							.apply(new RequestOptions().centerCrop())
							.into(mImageView);
				}
			}
		}
	}

	public interface GenreClickListener {
		void onItemClick(int position);

		void onTrackClick(int genreIndex, int trackIndex);
	}
}
