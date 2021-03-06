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
	private String mArtistImage;
	private String mStreamUrl;
	private boolean mIsOffline;

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
		mDownloadable = in.readByte() != 0;
		mDownloadUrl = in.readString();
		mDuration = in.readInt();
		mId = in.readInt();
		mPermalinkUrl = in.readString();
		mTitle = in.readString();
		mArtist = in.readString();
		mArtistImage = in.readString();
		mStreamUrl = in.readString();
		mIsOffline = in.readByte() != 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mArtworkUrl);
		dest.writeByte((byte) (mDownloadable ? 1 : 0));
		dest.writeString(mDownloadUrl);
		dest.writeInt(mDuration);
		dest.writeInt(mId);
		dest.writeString(mPermalinkUrl);
		dest.writeString(mTitle);
		dest.writeString(mArtist);
		dest.writeString(mArtistImage);
		dest.writeString(mStreamUrl);
		dest.writeByte((byte) (mIsOffline ? 1 : 0));
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

	public String getArtistImage() {
		return mArtistImage;
	}

	public void setArtistImage(String artistImage) {
		mArtistImage = artistImage;
	}

	public boolean isOffline() {
		return mIsOffline;
	}

	public void setOffline(boolean offline) {
		mIsOffline = offline;
	}

	public String getStreamUrl() {
		return mStreamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		mStreamUrl = streamUrl;
	}
}
