package com.taskr.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SoftwareSpot on 16/02/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Error codes
     */
    public static final byte INSERT_ERROR = -1;
    public static final byte ERROR = 0;

    /**
     * Default attributes
     */
    public static final byte DEFAULT_ID = -1;

    /**
     * Database name and Version
     */
    private static final String DATABASE_NAME = "taskr.db";
    private static final byte DATABASE_VERSION = 1;

    /**
     * Setting names
     */
    public static final String TABLE_SETTING = "setting";

    /**
     * Setting column names
     */
    public static final String COLUMNNAME_SETTING_ID = "setting_id";
    public static final String COLUMNNAME_SETTING_SORTCODE = "setting_sortcode";
    public static final String COLUMNNAME_SETTING_DESCRIPTIONDISPLAY = "setting_descriptiondisplay";
    public static final String COLUMNNAME_SETTING_DESCRIPTIONLENGTH = "setting_descriptionlength";
    public static final String COLUMNNAME_SETTING_DELETECONFIRM = "setting_deleteconfirm";
    public static final String COLUMNNAME_SETTING_RANDOMCOLOR = "setting_randomcolor";
    public static final String COLUMNNAME_SETTING_IMAGEDISPLAY = "setting_imagedisplay";

    /**
     * Setting column index
     */
    public static final byte COLUMNINDEX_SETTING_ID = 0;
    public static final byte COLUMNINDEX_SETTING_SORTCODE = 1;
    public static final byte COLUMNINDEX_SETTING_DESCRIPTIONDISPLAY = 2;
    public static final byte COLUMNINDEX_SETTING_DESCRIPTIONLENGTH = 3;
    public static final byte COLUMNINDEX_SETTING_DELETECONFIRM = 4;
    public static final byte COLUMNINDEX_SETTING_RANDOMCOLOR = 5;
    public static final byte COLUMNINDEX_SETTING_IMAGEDISPLAY = 6;

    /**
     * Table names
     */
    public static final String TABLE_TASK = "task";

    /**
     * Task column names
     */
    public static final String COLUMNNAME_TASK_ID = "task_id";
    public static final String COLUMNNAME_TASK_NAME = "task_name";
    public static final String COLUMNNAME_TASK_DESCRIPTION = "task_description";
    public static final String COLUMNNAME_TASK_ADDED = "task_added";
    public static final String COLUMNNAME_TASK_COLOR = "task_color";

    /**
     * Task column index
     */
    public static final byte COLUMNINDEX_TASK_ID = 0;
    public static final byte COLUMNINDEX_TASK_NAME = 1;
    public static final byte COLUMNINDEX_TASK_DESCRIPTION = 2;
    public static final byte COLUMNINDEX_TASK_ADDED = 3;
    public static final byte COLUMNINDEX_TASK_COLOR = 4;

    /**
     * Create tables
     */
    public static final String CREATE_SETTING = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTING + '(' +
            COLUMNNAME_SETTING_ID + " INTEGER PRIMARY KEY, " +
            COLUMNNAME_SETTING_SORTCODE + " TEXT NOT NULL, " +
            COLUMNNAME_SETTING_DESCRIPTIONDISPLAY + " INTEGER NOT NULL, " +
            COLUMNNAME_SETTING_DESCRIPTIONLENGTH + " INTEGER NOT NULL, " +
            COLUMNNAME_SETTING_DELETECONFIRM + " INTEGER NOT NULL, " +
            COLUMNNAME_SETTING_RANDOMCOLOR + " INTEGER NOT NULL, " +
            COLUMNNAME_SETTING_IMAGEDISPLAY + " INTEGER NOT NULL " +
            ')';
    public static final String CREATE_TASK = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + '(' +
            COLUMNNAME_TASK_ID + " INTEGER PRIMARY KEY, " +
            COLUMNNAME_TASK_NAME + " TEXT NOT NULL, " +
            COLUMNNAME_TASK_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMNNAME_TASK_ADDED + " TEXT NOT NULL, " +
            COLUMNNAME_TASK_COLOR + " INTEGER NOT NULL " +
            ')';

    // Drop tables
    public static final String DROP_SETTING = "DROP TABLE IF EXISTS " + TABLE_SETTING;
    public static final String DROP_TASK = "DROP TABLE IF EXISTS " + TABLE_TASK;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SETTING);
        db.execSQL(CREATE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version "
                        + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.delete(TABLE_SETTING, null, null);
        db.delete(TABLE_TASK, null, null);

        onCreate(db);
    }
}
