package com.cris.nvh.framgiaproject.screen.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cris.nvh.framgiaproject.R;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
	private ImageView mButtonSearch;
	private EditText mEditSearch;
	private ImageView mButtonBack;
	private RecyclerView mRecyclerTracks;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_view:
				super.onBackPressed();
				break;
			case R.id.image_search:
				searchTracks();
				break;
			default:
				break;
		}
	}

	private void searchTracks() {
	}

	private void initView() {
		mButtonSearch = findViewById(R.id.image_search);
		mEditSearch = findViewById(R.id.edit_search);
		mButtonBack = findViewById(R.id.image_view);
		mRecyclerTracks = findViewById(R.id.recycler_tracks);
		mButtonBack.setImageResource(R.drawable.ic_back);
		mButtonSearch.setOnClickListener(this);
		mButtonBack.setOnClickListener(this);
	}
}
