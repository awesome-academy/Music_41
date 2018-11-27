package com.cris.nvh.framgiaproject.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class Genre implements Parcelable {
	private List<Track> mTracks;

	public Genre() {
	}

	protected Genre(Parcel in) {
		mTracks = in.readArrayList(Track.class.getClassLoader());
	}

	public static final Creator<Genre> CREATOR = new Creator<Genre>() {
		@Override
		public Genre createFromParcel(Parcel in) {
			return new Genre(in);
		}

		@Override
		public Genre[] newArray(int size) {
			return new Genre[size];
		}
	};

	public List<Track> getTracks() {
		return mTracks;
	}

	public void setTracks(List<Track> tracks) {
		mTracks = tracks;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeList(mTracks);
	}
}
