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

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SongsSlideAdapter extends PagerAdapter {
	private int[] mImageResources = {R.drawable.image_slide1,
			R.drawable.slide2, R.drawable.slide3};

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View imageLayout;
		imageLayout = LayoutInflater.from(container.getContext())
				.inflate(R.layout.layout_slide_images, container, false);
		ImageView imageView = imageLayout.findViewById(R.id.image_slide);
		Glide.with(imageLayout)
				.load(mImageResources[position])
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
		return mImageResources == null ? 0 : mImageResources.length;
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}
}
