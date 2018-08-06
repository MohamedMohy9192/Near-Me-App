package com.androideradev.www.nearme.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PlaceContract {

    public static final String AUTHORITY = "com.androideradev.www.nearme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_PLACES = "favorite_places";

    public static final class PlaceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_PLACES).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_FAVORITE_PLACES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_FAVORITE_PLACES;

        public static final String TABLE_NAME = "favorite_places";
        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_PLACE_PHONE = "place_phone";
        public static final String COLUMN_PLACE_ADDRESS = "place_address";
        public static final String COLUMN_PLACE_LAT = "place_lat";
        public static final String COLUMN_PLACE_LNG = "place_lng";
        public static final String COLUMN_PLACE_TYPE = "place_type";
        public static final String COLUMN_PLACE_IS_FAVORITE = "place_is_favorite";

        public static final int PLACE_IS_FAVORITE = 1;
        public static final int PLACE_NOT_FAVORITE = 0;


    }
}
