package com.cris.nvh.framgiaproject.screen.playing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cris.nvh.framgiaproject.R;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class PlayFragment extends Fragment {
	private static final int START_ANGLE = 0;
	private static final int END_ANGLE = 360;
	private static final int DURATION = 10000;
	private static final float PIVOT = 0.5f;
	private ImageView mImageAlbum;
	private ImageView mImageBackButton;

	public static PlayFragment newInstance() {
		return new PlayFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(container.getContext())
				.inflate(R.layout.fragment_play, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mImageAlbum = view.findViewById(R.id.image_album);
		Glide.with(view)
				.load(R.drawable.default_album)
				.apply(RequestOptions.circleCropTransform())
				.into(mImageAlbum);
		RotateAnimation rotate = new RotateAnimation(START_ANGLE, END_ANGLE,
				Animation.RELATIVE_TO_SELF, PIVOT, Animation.RELATIVE_TO_SELF, PIVOT);
		rotate.setDuration(DURATION);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setInterpolator(new LinearInterpolator());
		mImageAlbum.startAnimation(rotate);
	}
}
