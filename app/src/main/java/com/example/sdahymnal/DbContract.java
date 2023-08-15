package com.example.sdahymnal;

import android.provider.BaseColumns;

public final class DbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbContract() {}


    /* Inner class that defines the table contents */
    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String HYMN_NUMBER = "number";
        public static final String HYMN_TEXT = "lyrics";
    }

    public static class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favoriteEntry";
        public static final String FAVORITE_NUMBER = "favoriteNumber";

    }
}
