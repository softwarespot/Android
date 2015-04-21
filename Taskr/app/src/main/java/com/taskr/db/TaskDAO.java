package com.taskr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.taskr.enums.DatabaseCode;
import com.taskr.models.Task;
import com.taskr.utilities.DateUtils;
import com.taskr.utilities.ObjectUtils;
import com.taskr.utilities.StringUtils;

import java.io.Closeable;
import java.util.ArrayList;

/**
 * Created by SoftwareSpot on 16/02/2015.
 */
public class TaskDAO implements Closeable {
    private static final String[] sAllColumns = {
            DatabaseHelper.COLUMNNAME_TASK_ID,
            DatabaseHelper.COLUMNNAME_TASK_NAME,
            DatabaseHelper.COLUMNNAME_TASK_DESCRIPTION,
            DatabaseHelper.COLUMNNAME_TASK_ADDED,
            DatabaseHelper.COLUMNNAME_TASK_COLOR
    };
    private final DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private long mLastInsertId;

    public TaskDAO(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mLastInsertId = DatabaseHelper.DEFAULT_ID;
        open();
    }

    /**
     * Create a new Task object based on the current cursor
     *
     * @param cursor SQLite database cursor
     * @return Task object reference
     */
    private static Task cursorToTask(Cursor cursor) {
        return new Task(
                cursor.getInt(DatabaseHelper.COLUMNINDEX_TASK_ID),
                cursor.getString(DatabaseHelper.COLUMNINDEX_TASK_NAME),
                cursor.getString(DatabaseHelper.COLUMNINDEX_TASK_DESCRIPTION),
                DateUtils.stringToDate(cursor.getString(DatabaseHelper.COLUMNINDEX_TASK_ADDED)),
                cursor.getInt(DatabaseHelper.COLUMNINDEX_TASK_COLOR)
        );
    }

    private static boolean isTask(Task task) {
        return !ObjectUtils.isNull(task) && !StringUtils.isNullOrEmpty(task.getName());
    }

    private static ContentValues taskToContentValues(Task task) {
        ContentValues values = new ContentValues();
        // values.put(DatabaseHelper.COLUMNNAME_TASK_ID, task.getId());
        values.put(DatabaseHelper.COLUMNNAME_TASK_NAME, task.getName());
        values.put(DatabaseHelper.COLUMNNAME_TASK_DESCRIPTION, StringUtils.isNullOrEmpty(task.getDescription()) ? StringUtils.EMPTY : task.getDescription());
        values.put(DatabaseHelper.COLUMNNAME_TASK_ADDED, StringUtils.dateToString(task.getAdded()));
        values.put(DatabaseHelper.COLUMNNAME_TASK_COLOR, task.getColor());
        return values;
    }

    @Override
    public void close() {
        if (!ObjectUtils.isNull(mDatabase)) {
            mDatabase.close(); // Unclear if this is required at all
            mDatabase = null;
        }
        mDatabaseHelper.close();
    }

    public Task create(String name, String description) {
        DatabaseCode databaseCode = insert(name, description);
        return databaseCode == DatabaseCode.OK || databaseCode == DatabaseCode.FOUND || databaseCode == DatabaseCode.ERROR ? getLastInsert() : null;
    }

    public DatabaseCode delete(Task task) {
        if (!isTask(task)) {
            return DatabaseCode.INVALID_OBJECT;
        }
        if (!isTaskExistsById(task)) {
            return DatabaseCode.NOT_FOUND;
        }
        if (mDatabase.delete(DatabaseHelper.TABLE_TASK, DatabaseHelper.COLUMNNAME_TASK_ID + " = ?", new String[]{ String.valueOf(task.getId()) }) != DatabaseHelper.ERROR) {
            return DatabaseCode.OK;
        }
        return DatabaseCode.ERROR;
    }

    public void deleteAll() {
        mDatabase.execSQL(DatabaseHelper.DROP_TASK);
        mDatabase.execSQL(DatabaseHelper.CREATE_TASK);
        // return mDatabase.delete(DatabaseHelper.TABLE_TASK, null, null) != DatabaseHelper.ERROR ? DatabaseCode.OK : DatabaseCode.ERROR;
    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, DatabaseHelper.TABLE_TASK);
    }

    public Task getLastInsert() {
        return mLastInsertId != DatabaseHelper.DEFAULT_ID ? getTaskById(mLastInsertId) : null;
    }

    public long getLastInsertId() {
        return mLastInsertId;
    }

    public Task getTaskById(long id) {
        Task task = null;

        if (id > DatabaseHelper.DEFAULT_ID) {
            Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TASK, sAllColumns, DatabaseHelper.COLUMNNAME_TASK_ID + " = ?", new String[]{ String.valueOf(id) }, null, null, null);
            if (cursor.moveToFirst() && cursor.getColumnCount() == sAllColumns.length) {
                task = cursorToTask(cursor);
            }
            cursor.close(); // Close the cursor
        }
        return task;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = null;

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TASK,
                sAllColumns, null, null, null, null, null);
        if (cursor.moveToFirst()) { // Move to the first cursor
            tasks = new ArrayList<>(cursor.getCount()); // Create an ArrayList<Task>
            while (!cursor.isAfterLast()) {
                Task task = cursorToTask(cursor);
                tasks.add(task);
                cursor.moveToNext();
            }
        }
        cursor.close(); // Close the cursor

        return tasks;
    }

    public DatabaseCode insert(String name, String description) {
        Task task = Task.create(name, description);
        if (!isTask(task)) {
            return DatabaseCode.INVALID_OBJECT;
        }
        mLastInsertId = mDatabase.insert(DatabaseHelper.TABLE_TASK, null, taskToContentValues(task));
        if (mLastInsertId != DatabaseHelper.INSERT_ERROR) {
            return DatabaseCode.OK;
        }
        return DatabaseCode.ERROR;
    }

    public void open() {
        if (ObjectUtils.isNull(mDatabase)) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    public DatabaseCode update(Task task) {
        if (!isTask(task)) {
            return DatabaseCode.INVALID_OBJECT;
        }
        if (!isTaskExistsById(task)) {
            return DatabaseCode.NOT_FOUND;
        }
        if (mDatabase.update(DatabaseHelper.TABLE_TASK, taskToContentValues(task), DatabaseHelper.COLUMNNAME_TASK_ID + " = ?", new String[]{ String.valueOf(task.getId()) }) != DatabaseHelper.ERROR) {
            return DatabaseCode.OK;
        }
        return DatabaseCode.ERROR;
    }

    private boolean isTaskExistsById(Task task) {
        return !ObjectUtils.isNull(task) && task.getId() != DatabaseHelper.DEFAULT_ID && isTask(getTaskById(task.getId()));
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