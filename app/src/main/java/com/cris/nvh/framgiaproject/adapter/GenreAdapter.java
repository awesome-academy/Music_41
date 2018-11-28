package com.cris.nvh.framgiaproject.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
	private static final int ALL_MUSIC_INDEX = 0;
	private static final int ALL_AUDIO_INDEX = 1;
	private static final int ALL_ALTERNATIVE_INDEX = 2;
	private static final int ALL_AMBIENT_INDEX = 3;
	private static final int ALL_CLASSICAL_INDEX = 4;
	private static final int ALL_COUNTRY_INDEX = 5;
	private ArrayList<Genre> mGenres;

	public GenreAdapter(ArrayList<Genre> genres) {
		mGenres = genres;
	}

	@Override
	public GenreViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater
				.from(viewGroup.getContext())
				.inflate(R.layout.genre_viewholder, viewGroup, false);
		return new GenreViewHolder(view);
	}

	@Override
	public void onBindViewHolder(GenreViewHolder genreViewHolder, int i) {
		genreViewHolder.bindData(mGenres.get(i));
	}

	@Override
	public int getItemCount() {
		return mGenres == null ? 0 : mGenres.size();
	}

	public static class GenreViewHolder extends RecyclerView.ViewHolder {
		private TextView mTextView;
		private RecyclerView mRecyclerView;

		public GenreViewHolder(View itemView) {
			super(itemView);
			mTextView = itemView.findViewById(R.id.text_genre);
			mRecyclerView = itemView.findViewById(R.id.recycler_list_tracks);
		}

		public void bindData(Genre genre) {
			setGenreText();
			mRecyclerView.setAdapter(new ListTracksAdapter(genre.getTracks()));
			mRecyclerView.setHasFixedSize(true);
			mRecyclerView.setLayoutManager(new LinearLayoutManager(
					itemView.getContext(), HORIZONTAL, false));
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

		public class ListTracksAdapter extends RecyclerView.Adapter<ListTracksAdapter.TrackViewHolder> {
			private List<Track> mTracks;

			public ListTracksAdapter(List<Track> tracks) {
				mTracks = tracks;
			}

			@Override
			public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
				View view = LayoutInflater
						.from(viewGroup.getContext())
						.inflate(R.layout.tracks_viewholder, viewGroup, false);
				return new TrackViewHolder(view);
			}

			@Override
			public void onBindViewHolder(TrackViewHolder trackViewHolder, int i) {
				trackViewHolder.bindData(i);
			}

			@Override
			public int getItemCount() {
				return mTracks == null ? 0 : mTracks.size();
			}

			class TrackViewHolder extends RecyclerView.ViewHolder {
				private ImageView mImageView;

				public TrackViewHolder(View itemView) {
					super(itemView);
					mImageView = itemView.findViewById(R.id.image_track);
				}

				public void bindData(int i) {
					String imageUrl = mTracks.get(i).getArtworkUrl();
					if (!imageUrl.equals("null")) {
						Glide.with(itemView)
								.load(imageUrl)
								.into(mImageView);
						return;
					}
					Glide.with(itemView)
							.load(R.drawable.default_image)
							.into(mImageView);
				}
			}
		}
	}

	public interface GenreClickListener {
		void onItemClick(Genre genre);
	}
}
