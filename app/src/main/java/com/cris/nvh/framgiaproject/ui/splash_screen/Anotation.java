package com.cris.nvh.framgiaproject.ui.splash_screen;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

public class Anotation {
	@Retention(RetentionPolicy.SOURCE)
	@StringDef({
			ListGenres.ALL_MUSIC,
			ListGenres.ALL_AUDIO,
			ListGenres.ALTERNATIVE,
			ListGenres.AMBIENT,
			ListGenres.CLASSICAL,
			ListGenres.COUNTRY
	})
	@interface ListGenres {
		String ALL_MUSIC = "genre=soundcloud:genres:all-music";
		String ALL_AUDIO = "genre=soundcloud:genres:all-audio";
		String ALTERNATIVE = "genre=soundcloud:genres:alternativerock";
		String AMBIENT = "genre=soundcloud:genres:ambient";
		String CLASSICAL = "genre=soundcloud:genres:classical";
		String COUNTRY = "genre=soundcloud:genres:country";
	}
}
