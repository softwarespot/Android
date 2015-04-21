package com.taskr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.taskr.enums.DatabaseCode;
import com.taskr.enums.SortCode;
import com.taskr.models.Setting;
import com.taskr.utilities.BooleanUtils;
import com.taskr.utilities.IntegerUtils;
import com.taskr.utilities.ObjectUtils;
import com.taskr.utilities.SerializationUtils;

import java.io.Closeable;

/**
 * Created by SoftwareSpot on 16/02/2015.
 */
public class SettingDAO implements Closeable {
    private static final String[] sAllColumns = {
            DatabaseHelper.COLUMNNAME_SETTING_ID,
            DatabaseHelper.COLUMNNAME_SETTING_SORTCODE,
            DatabaseHelper.COLUMNNAME_SETTING_DESCRIPTIONDISPLAY,
            DatabaseHelper.COLUMNNAME_SETTING_DESCRIPTIONLENGTH,
            DatabaseHelper.COLUMNNAME_SETTING_DELETECONFIRM,
            DatabaseHelper.COLUMNNAME_SETTING_RANDOMCOLOR,
            DatabaseHelper.COLUMNNAME_SETTING_IMAGEDISPLAY
    };
    private static final String sId = "1";
    private final DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public SettingDAO(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        open();
    }

    /**
     * Create a new Setting object based on the current cursor
     *
     * @param cursor SQLite database cursor
     * @return Setting object reference
     */
    private static Setting cursorToSetting(Cursor cursor) {
        String sortCode = cursor.getString(DatabaseHelper.COLUMNINDEX_SETTING_SORTCODE);
        return new Setting(
                SerializationUtils.getSerializable(SortCode.class, sortCode),
                BooleanUtils.intToBoolean(cursor.getInt(DatabaseHelper.COLUMNINDEX_SETTING_DESCRIPTIONDISPLAY)),
                cursor.getInt(DatabaseHelper.COLUMNINDEX_SETTING_DESCRIPTIONLENGTH),
                BooleanUtils.intToBoolean(cursor.getInt(DatabaseHelper.COLUMNINDEX_SETTING_DELETECONFIRM)),
                BooleanUtils.intToBoolean(cursor.getInt(DatabaseHelper.COLUMNINDEX_SETTING_RANDOMCOLOR)),
                BooleanUtils.intToBoolean(cursor.getInt(DatabaseHelper.COLUMNINDEX_SETTING_IMAGEDISPLAY))
        );
    }

    public static void deleteAll(Context context) {
        SettingDAO settingDAO = new SettingDAO(context);
        settingDAO.open();
        settingDAO.deleteAll();
        settingDAO.close();
    }

    public static Setting getSetting(Context context) {
        SettingDAO settingDAO = new SettingDAO(context);
        settingDAO.open();
        Setting setting = settingDAO.getSetting();
        settingDAO.close();
        return setting;
    }

    private static boolean isSetting(Setting setting) {
        return !ObjectUtils.isNull(setting);
    }

    private static ContentValues settingToContentValues(Setting setting) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMNNAME_SETTING_ID, sId);
        values.put(DatabaseHelper.COLUMNNAME_SETTING_SORTCODE, SerializationUtils.putSerializable(setting.getSortCode()));
        values.put(DatabaseHelper.COLUMNNAME_SETTING_DESCRIPTIONDISPLAY, IntegerUtils.booleanToInt(setting.isDescriptionDisplay()));
        values.put(DatabaseHelper.COLUMNNAME_SETTING_DESCRIPTIONLENGTH, setting.getDescriptionLength());
        values.put(DatabaseHelper.COLUMNNAME_SETTING_DELETECONFIRM, IntegerUtils.booleanToInt(setting.isDeleteConfirm()));
        values.put(DatabaseHelper.COLUMNNAME_SETTING_RANDOMCOLOR, IntegerUtils.booleanToInt(setting.isRandomColor()));
        values.put(DatabaseHelper.COLUMNNAME_SETTING_IMAGEDISPLAY, IntegerUtils.booleanToInt(setting.isImageDisplay()));
        return values;
    }

    public static DatabaseCode writeSetting(Context context, Setting setting) {
        SettingDAO settingDAO = new SettingDAO(context);
        settingDAO.open();
        DatabaseCode databaseCode = settingDAO.update(setting);
        settingDAO.close();
        return databaseCode;
    }

    @Override
    public void close() {
        if (!ObjectUtils.isNull(mDatabase)) {
            mDatabase.close(); // Unclear if this is required at all
            mDatabase = null;
        }
        mDatabaseHelper.close();
    }

    public void deleteAll() {
        mDatabase.execSQL(DatabaseHelper.DROP_SETTING);
        mDatabase.execSQL(DatabaseHelper.CREATE_SETTING);
        // return mDatabase.delete(DatabaseHelper.TABLE_SETTING, null, null) != DatabaseHelper.ERROR ? DatabaseCode.OK : DatabaseCode.ERROR;
    }

    public Setting getSetting() {
        Setting setting;

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_SETTING, sAllColumns, DatabaseHelper.COLUMNNAME_SETTING_ID + " = ?", new String[]{ sId }, null, null, null);
        if (cursor.moveToFirst() && cursor.getColumnCount() == sAllColumns.length) {
            setting = cursorToSetting(cursor);
        } else {
            setting = new Setting();
            insert(setting);
        }
        cursor.close(); // Close the cursor
        return setting;
    }

    public DatabaseCode update(Setting setting) {
        if (!isSetting(setting)) {
            return DatabaseCode.INVALID_OBJECT;
        }
        if (!isSettingExists()) {
            return DatabaseCode.NOT_FOUND;
        }
        if (mDatabase.update(DatabaseHelper.TABLE_SETTING, settingToContentValues(setting), DatabaseHelper.COLUMNNAME_SETTING_ID + " = ?", new String[]{ sId }) != DatabaseHelper.ERROR) {
            return DatabaseCode.OK;
        }
        return DatabaseCode.ERROR;
    }

    private long getCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, DatabaseHelper.TABLE_SETTING);
    }

    private DatabaseCode insert(Setting setting) {
        if (!isSetting(setting)) {
            return DatabaseCode.INVALID_OBJECT;
        }
        if (mDatabase.insert(DatabaseHelper.TABLE_SETTING, null, settingToContentValues(setting)) != DatabaseHelper.INSERT_ERROR) {
            return DatabaseCode.OK;
        }
        return DatabaseCode.ERROR;
    }

    private boolean isSettingExists() {
        return isSetting(getSetting());
    }

    private void open() {
        if (ObjectUtils.isNull(mDatabase)) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    @Override
    protected void finalize() {
        close();
        try {
            super.finalize();
        } catch (Throwable ignored) {
        }
    }
}