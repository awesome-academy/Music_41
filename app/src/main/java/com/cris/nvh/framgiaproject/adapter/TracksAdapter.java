package com.cris.nvh.framgiaproject.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;
import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.MyViewHolder> {
	private int mCurrentTrack;
	private List<Track> mTracks;
	private OnClickItemTrackListener mListener;
	private boolean mIsNowPlaying;
	private boolean mIsRecentTracks;
	private boolean mIsFavorite;

	public TracksAdapter(OnClickItemTrackListener listener) {
		mTracks = new ArrayList<>();
		mListener = listener;
	}

	public TracksAdapter(List<Track> tracks, OnClickItemTrackListener listener) {
		mTracks = tracks;
		mListener = listener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext())
			.inflate(R.layout.item_track, viewGroup, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
		myViewHolder.bindData(mTracks.get(i), mListener);
		if (i == mCurrentTrack) {
			myViewHolder.setNowPlayingBackground();
			return;
		}
		myViewHolder.setDefaultTrackBackground();
	}

	@Override
	public int getItemCount() {
		return mTracks != null ? mTracks.size() : 0;
	}

	public TracksAdapter setNowPlaying(boolean nowPlaying) {
		mIsNowPlaying = nowPlaying;
		return this;
	}

	public TracksAdapter setRecentTracks(boolean recentTracks) {
		mIsRecentTracks = recentTracks;
		return this;
	}

	public TracksAdapter setFavorite(boolean favarite) {
		mIsFavorite = favarite;
		return this;
	}

	public void updateTracks(List<Track> tracks) {
		if (tracks != null) {
			mTracks.clear();
			mTracks.addAll(tracks);
			notifyDataSetChanged();
		}
	}

	public void addTracks(List<Track> tracks) {
		if (tracks != null) {
			mTracks.addAll(tracks);
			notifyDataSetChanged();
		}
	}

	public void setCurrentTrack(int index) {
		mCurrentTrack = index;
		notifyDataSetChanged();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView mTrackImage;
		private TextView mTrackName;
		private TextView mSingerName;
		private ImageView mFeature;
		private OnClickItemTrackListener mListener;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			mTrackImage = itemView.findViewById(R.id.image_track);
			mTrackName = itemView.findViewById(R.id.text_song_name);
			mSingerName = itemView.findViewById(R.id.text_singer_name);
			mFeature = itemView.findViewById(R.id.image_feature);
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.image_feature:
					if (mIsFavorite) mListener.deleteFromFavorite(getAdapterPosition());
					else mListener.showDialogFeatureTrack(getAdapterPosition());
					break;
				default:
					mListener.clickItemTrackListener(getAdapterPosition());
					break;
			}
		}

		public void setNowPlayingBackground() {
			itemView.setBackgroundResource(R.color.color_gray);
		}

		public void setDefaultTrackBackground() {
			itemView.setBackgroundColor(Color.TRANSPARENT);
		}

		public void bindData(Track track,
		                     final OnClickItemTrackListener listener) {
			mTrackName.setText(track.getTitle());
			mSingerName.setText(track.getArtist());
			setImage(mTrackImage, track.getArtworkUrl());
			if (mIsFavorite) mFeature.setImageResource(R.drawable.like);
			mListener = listener;
			itemView.setOnClickListener(this);
			mFeature.setOnClickListener(this);
		}

		private void setImage(ImageView image, String source) {
			RequestOptions requestOptions = new RequestOptions();
			requestOptions.error(R.drawable.default_album);
			Glide.with(image.getContext())
				.load(source)
				.apply(requestOptions)
				.into(image);
		}
	}

	public interface OnClickItemTrackListener {
		void clickItemTrackListener(int position);

		void showDialogFeatureTrack(int position);

		void deleteFromFavorite(int position);
	}
}
