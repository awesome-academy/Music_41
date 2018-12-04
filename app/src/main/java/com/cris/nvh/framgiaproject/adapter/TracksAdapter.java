package com.cris.nvh.framgiaproject.adapter;

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
	private List<Track> mTracks;
	private OnClickItemTrackListener mListener;
	private boolean mIsNowPlaying;
	private boolean mIsRecentTracks;
	private boolean mIsFavorite;
	private OnClickDeleteListener mDeleteListener;

	public TracksAdapter(OnClickItemTrackListener listener) {
		mTracks = new ArrayList<>();
		mListener = listener;
	}

	public TracksAdapter(List<Track> tracks, OnClickItemTrackListener listener) {
		mTracks = tracks;
		mListener = listener;
	}

	public TracksAdapter(List<Track> tracks, OnClickItemTrackListener trackListener,
	                     OnClickDeleteListener deleteListener) {
		mTracks = tracks;
		mDeleteListener = deleteListener;
		mListener = trackListener;
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

	public TracksAdapter setFavarite(boolean favarite) {
		mIsFavorite = favarite;
		return this;
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

		private void updateItem() {
			itemView.setBackgroundColor(itemView.getResources()
				.getColor(R.color.color_black));
		}

		public void bindData(Track track,
		                     final OnClickItemTrackListener listener) {
			mTrackName.setText(track.getTitle());
			mSingerName.setText(track.getArtist());
			setImage(mTrackImage, track.getArtworkUrl());
			mListener = listener;
			itemView.setOnClickListener(this);
			mFeature.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.image_feature:
					mListener.showDialogFeatureTrack(getAdapterPosition());
					break;
				default:
					mListener.clickItemTrackListener(getAdapterPosition());
					break;
			}
		}

		public void setImage(ImageView image, String source) {
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
	}

	public interface OnClickDeleteListener {
		void deleteFromFavorite(int position);
	}
}
