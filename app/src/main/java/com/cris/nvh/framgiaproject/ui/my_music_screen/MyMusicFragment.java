package com.cris.nvh.framgiaproject.ui.my_music_screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cris.nvh.framgiaproject.R;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class MyMusicFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_music, container, false);
	}
}
