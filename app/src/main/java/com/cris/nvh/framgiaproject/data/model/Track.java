package com.cris.nvh.framgiaproject.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class Track implements Parcelable {

	private String mArtworkUrl;
	private boolean mDownloadable;
	private String mDownloadUrl;
	private int mDuration;
	private int mId;
	private String mPermalinkUrl;
	private String mTitle;
	private String mArtist;

	public final static Parcelable.Creator<Track> CREATOR = new Creator<Track>() {
		@SuppressWarnings({
				"unchecked"
		})
		public Track createFromParcel(Parcel in) {
			return new Track(in);
		}

		public Track[] newArray(int size) {
			return (new Track[size]);
		}
	};

	public Track() {
	}

	public Track(int id) {
		mId = id;
	}

	protected Track(Parcel in) {
		mArtworkUrl = in.readString();
		mDownloadable = ((boolean) in.readValue((boolean.class.getClassLoader())));
		mDownloadUrl = in.readString();
		mDuration = in.readInt();
		mId = in.readInt();
		mPermalinkUrl = in.readString();
		mTitle = in.readString();
		mArtist = in.readString();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mArtworkUrl);
		dest.writeValue(mDownloadable);
		dest.writeString(mDownloadUrl);
		dest.writeInt(mDuration);
		dest.writeInt(mId);
		dest.writeString(mPermalinkUrl);
		dest.writeString(mTitle);
		dest.writeString(mArtist);
	}

	public int describeContents() {
		return 0;
	}

	public String getArtworkUrl() {
		return mArtworkUrl;
	}

	public void setArtworkUrl(String artworkUrl) {
		mArtworkUrl = artworkUrl;
	}

	public boolean isDownloadable() {
		return mDownloadable;
	}

	public void setDownloadable(boolean downloadable) {
		mDownloadable = downloadable;
	}

	public String getDownloadUrl() {
		return mDownloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		mDownloadUrl = downloadUrl;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int duration) {
		mDuration = duration;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getPermalinkUrl() {
		return mPermalinkUrl;
	}

	public void setPermalinkUrl(String permalinkUrl) {
		mPermalinkUrl = permalinkUrl;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String artist) {
		mArtist = artist;
	}
}
