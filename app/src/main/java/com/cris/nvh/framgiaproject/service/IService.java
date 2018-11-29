package com.cris.nvh.framgiaproject.service;

public interface IService {
	void create(int index);

	void prepare();

	void start();

	void pause();

	void stop();

	int getDuration();

	int getCurrrentPosition();

	boolean isPlaying();

	void seek(int possition);

	void loop(boolean isLoop);

	int getTrack();

	void changeTrack();
}
