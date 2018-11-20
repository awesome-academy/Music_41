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
		public static final String ALL_MUSIC = "genre=soundcloud:genres:all-music";
		public static final String ALL_AUDIO = "genre=soundcloud:genres:all-audio";
		public static final String ALTERNATIVE = "genre=soundcloud:genres:salternativerock";
		public static final String AMBIENT = "genre=soundcloud:genres:ambient";
		public static final String CLASSICAL = "genre=soundcloud:genres:classical";
		public static final String COUNTRY = "genre=soundcloud:genres:country";
	}
}
