package com.cris.nvh.framgiaproject.screen.listtracks;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nvh
 * Contact: toiyeuthethao1997@gmail.com
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	TrackTypes.LOCAL,
	TrackTypes.ALL_MUSIC,
	TrackTypes.ALL_AUDIO,
	TrackTypes.ALTERNATIVE,
	TrackTypes.AMBIENT,
	TrackTypes.CLASSICAL,
	TrackTypes.COUNTRY,
	TrackTypes.DOWNLOAD,
	TrackTypes.FAVORITES
})
public @interface TrackTypes {
	int ALL_MUSIC = 0;
	int ALL_AUDIO = 1;
	int ALTERNATIVE = 2;
	int AMBIENT = 3;
	int CLASSICAL = 4;
	int COUNTRY = 5;
	int LOCAL = 6;
	int DOWNLOAD = 7;
	int FAVORITES = 8;
}
