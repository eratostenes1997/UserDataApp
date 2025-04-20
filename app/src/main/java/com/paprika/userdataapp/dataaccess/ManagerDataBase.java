package com.paprika.userdataapp.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ManagerDataBase extends SQLiteOpenHelper {

    private static final String DATA_BASE = "db_users";
    private static final int VERSION = 1;
    private static final String TABLE_USERS = "users";

    public ManagerDataBase(@Nullable Context context) {
        super(context, DATA_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE = "CREATE TABLE " + TABLE_USERS + " (" +
                "use_document INTEGER PRIMARY KEY NOT NULL, " +
                "use_name TEXT NOT NULL, " +
                "use_lastname TEXT NOT NULL, " +
                "use_user TEXT NOT NULL, " +
                "use_pass TEXT NOT NULL, " +
                "use_status TEXT);";

        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}

