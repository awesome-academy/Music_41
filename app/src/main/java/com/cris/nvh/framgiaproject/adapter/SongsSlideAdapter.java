package com.cris.nvh.framgiaproject.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.R;
import com.cris.nvh.framgiaproject.data.model.Track;

import java.util.ArrayList;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SongsSlideAdapter extends PagerAdapter {
	private ArrayList<Track> mTracks;

	public SongsSlideAdapter(ArrayList<Track> tracks) {
		mTracks = tracks;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View imageLayout;
		imageLayout = LayoutInflater.from(container.getContext())
				.inflate(R.layout.layout_slide_images, container, false);
		ImageView imageView = imageLayout.findViewById(R.id.image_slide);
		String imageUrl = mTracks.get(position).getArtworkUrl();
		Glide.with(imageLayout)
				.load(imageUrl)
				.apply(new RequestOptions()
						.error(R.color.color_white)
						.centerCrop())
				.into(imageView);
		container.addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		View view = (View) object;
		container.removeView(view);
	}

	@Override
	public int getCount() {
		return mTracks == null ? 0 : mTracks.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}
}
