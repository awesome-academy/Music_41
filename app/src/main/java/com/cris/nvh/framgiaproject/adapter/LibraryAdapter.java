package com.cris.nvh.framgiaproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cris.nvh.framgiaproject.R;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {
	private static int[] sImageResIds = {R.drawable.list_tracks,
			R.drawable.download, R.drawable.like};
	private static int[] sTitles = {R.string.text_song,
			R.string.donwload, R.string.favorite};
	private static int[] sTotalTracks;
	private OnClickItemListener mListener;

	public LibraryAdapter(int[] totalTracks, OnClickItemListener listener) {
		mListener = listener;
		sTotalTracks = totalTracks;
	}

	@NonNull
	@Override
	public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater
				.from(viewGroup.getContext())
				.inflate(R.layout.layout_library, viewGroup, false);
		return new LibraryViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull LibraryViewHolder libraryViewHolder, int i) {
		libraryViewHolder.bindData(i, mListener);
	}

	@Override
	public int getItemCount() {
		return sImageResIds == null ? 0 : sImageResIds.length;
	}

	public static class LibraryViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		private ImageView mImageView;
		private TextView mTittle;
		private TextView mTextTotalSongs;
		private OnClickItemListener mListener;

		public LibraryViewHolder(@NonNull View itemView) {
			super(itemView);
			mImageView = itemView.findViewById(R.id.image_item);
			mTittle = itemView.findViewById(R.id.text_title);
			mTextTotalSongs = itemView.findViewById(R.id.text_total_songs);
			itemView.setOnClickListener(this);
		}

		public void bindData(int i, OnClickItemListener listener) {
			mImageView.setImageResource(sImageResIds[i]);
			mTittle.setText(sTitles[i]);
			mTextTotalSongs.setText(String.valueOf(sTotalTracks[i]));
			mListener = listener;
		}

		@Override
		public void onClick(View view) {
			mListener.clickItem(getAdapterPosition());
		}
	}

	public interface OnClickItemListener {
		void clickItem(int position);
	}
}
