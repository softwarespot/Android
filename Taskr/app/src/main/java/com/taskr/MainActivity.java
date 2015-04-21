package com.taskr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.taskr.adapters.TaskAdapter;
import com.taskr.db.SettingDAO;
import com.taskr.db.TaskDAO;
import com.taskr.enums.ActivityCode;
import com.taskr.enums.DatabaseCode;
import com.taskr.enums.SortCode;
import com.taskr.models.Setting;
import com.taskr.models.Task;
import com.taskr.utilities.CollectionsUtils;
import com.taskr.utilities.DbTestingUtils;
import com.taskr.utilities.ObjectUtils;
import com.taskr.utilities.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;

import de.keyboardsurfer.android.widget.crouton.Crouton;

// Idea: https://github.com/codepath/android_guides/wiki/Must-Have-Libraries
// Idea: https://www.youtube.com/watch?v=hB3AqKy8QME
// Idea: http://www.stevenmarkford.com/passing-objects-between-android-activities/

public class MainActivity extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    private TaskAdapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Setting mSettings;
    private TaskDAO mTaskDAO;

    /**
     * Sort an array list of tasks
     *
     * @param tasks
     * @param sortCode
     */
    private static void sortTasks(ArrayList<Task> tasks, SortCode sortCode) {
        if (sortCode == SortCode.DATE_ASC || sortCode == SortCode.DEFAULT) {
            Collections.sort(tasks); // Sort the tasks by date added
        } else if (sortCode == SortCode.DATE_DESC) {
            CollectionsUtils.sortTasksByDateDesc(tasks);
        } else if (sortCode == SortCode.NAME_ASC) {
            CollectionsUtils.sortTasksByNameAsc(tasks);
        } else {
            CollectionsUtils.sortTasksByNameDesc(tasks);
        }
    }

    /**
     * Create a task
     *
     * @param view
     */
    public void onAdd(View view) {
        // Create a new intent to pass to the EditActivity
        Intent intent = new Intent(this, EditActivity.class);
        // Put the task object reference as extra
        // intent.putExtra(ActivityCode.INTENT_OBJECT, null);
        // Specify the request code which will be read when adding
        intent.putExtra(ActivityCode.INTENT_REQUEST_CODE, ActivityCode.EDIT_ACTIVITY_ADD);
        // Start the activity with the purpose to capture the result
        startActivityForResult(intent, ActivityCode.EDIT_ACTIVITY_ADD);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        /*if (mLastLocation != null) {
            ToastUtils.toastOK(this, String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
        } else {
            ToastUtils.toastError(this, "An error occurred getting the location");
        }*/
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.w("Google API", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it's present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_aboutButton:
                ToastUtils.toastOK(this, getString(R.string.about));
                break;

            case R.id.action_settingsButton:
                // Create a new intent to pass to the SettingsActivity
                Intent intent = new Intent(this, SettingsActivity.class);
                // Put the settings object reference as extra
                intent.putExtra(ActivityCode.INTENT_OBJECT, mSettings);
                // Start the activity with the purpose to capture the result
                startActivityForResult(intent, ActivityCode.SETTINGS_ACTIVITY);
                break;

            case R.id.action_locationButton:
                if (!ObjectUtils.isNull(mLastLocation)) {
                    String.format(getString(R.string.location_success), mLastLocation.getLatitude(), mLastLocation.getLongitude());
                } else {
                    ToastUtils.toastError(this, getString(R.string.location_error));
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onResume(); // This is called after onActivityResult

        super.onActivityResult(requestCode, resultCode, data);
        if (ObjectUtils.isNull(data)) {
            return;
        }

        if (resultCode == RESULT_CANCELED ||
                requestCode == ActivityCode.EDIT_ACTIVITY_VIEW ||
                requestCode == ActivityCode.NONE) {
            return;
        }

        if (requestCode == ActivityCode.EDIT_ACTIVITY_ADD) {
            Task task = (Task) data.getSerializableExtra(ActivityCode.INTENT_OBJECT);
            if (mTaskDAO.insert(task.getName(), task.getDescription()) == DatabaseCode.OK) {
                mAdapter.add(task);
                ToastUtils.toastOK(this, getString(R.string.edit_saved));
            } else {
                ToastUtils.toastError(this, getString(R.string.edit_error_saving));
            }
        } else if (requestCode == ActivityCode.EDIT_ACTIVITY_EDIT) {
            Task task = (Task) data.getSerializableExtra(ActivityCode.INTENT_OBJECT);
            if (mTaskDAO.update(task) == DatabaseCode.OK) {
                mAdapter.update(task);
                ToastUtils.toastOK(this, getString(R.string.edit_updated));
            } else {
                ToastUtils.toastError(this, getString(R.string.edit_error_updating));
            }
        } else if (requestCode == ActivityCode.SETTINGS_ACTIVITY) {
            mSettings = (Setting) data.getSerializableExtra(ActivityCode.INTENT_OBJECT);
            if (!ObjectUtils.isNull(mSettings) && SettingDAO.writeSetting(this, mSettings) == DatabaseCode.OK) {
                ToastUtils.toastOK(this, getString(R.string.settings_updated));
                sortTasks(mAdapter.getTasks(), mSettings.getSortCode());
                mAdapter.update(mSettings);
            } else {
                ToastUtils.toastError(this, getString(R.string.settings_error_updating));
            }
        }
    }

    @Override
    protected void onPause() {
        mTaskDAO.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mTaskDAO.open();
        super.onResume();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate random data
        DbTestingUtils.generateData(this);

        mTaskDAO = new TaskDAO(this);
        mTaskDAO.open();

        mSettings = SettingDAO.getSetting(this);
        ArrayList<Task> tasks = mTaskDAO.getTasks();
        if (ObjectUtils.isNull(tasks)) { // If null then create an empty list
            tasks = new ArrayList<>();
        }

        // Sort the tasks using the sortcode
        sortTasks(tasks, mSettings.getSortCode());

        mAdapter = new TaskAdapter(this, tasks, mSettings);
        ListView listView = (ListView) findViewById(R.id.taskListView);

        listView.setAdapter(mAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        ListViewMultiChoiceModeListener listener = new ListViewMultiChoiceModeListener();

        // Set the adapter, settings and task data access object
        listener.setAdapter(mAdapter);
        listener.setSettings(mSettings);
        listener.setTaskDAO(mTaskDAO);
        listView.setMultiChoiceModeListener(listener);
        listView.setOnItemClickListener(new ListViewOnItemClickListener());

        buildGoogleApiClient();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Crouton.clearCroutonsForActivity(this); // Clear any croutons displayed within the activity
        super.onDestroy();
    }

    // Idea: http://www.survivingwithandroid.com/2013/04/android-listview-context-menu.html
    // Idea: http://stackoverflow.com/questions/12939627/passing-id-of-listview-item-to-actionmode-callback-object
    private interface ListViewMultiChoiceModeListenerInterface extends AbsListView.MultiChoiceModeListener {

        void delete(int position);

        TaskAdapter getAdapter();

        void setAdapter(TaskAdapter view);

        Setting getSettings();

        void setSettings(Setting settings);

        TaskDAO getTaskDAO();

        void setTaskDAO(TaskDAO taskDAO);
    }

    // Idea: http://www.androidbegin.com/tutorial/android-delete-multiple-selected-items-listview-tutorial/

    /**
     * Called when a listview item is selected on long click
     */
    private static class ListViewMultiChoiceModeListener implements ListViewMultiChoiceModeListenerInterface {
        private TaskAdapter mAdapter;
        private Setting mSettings;
        private TaskDAO mTaskDAO;

        @Override
        public void delete(int position) {
            Task task = mAdapter.getItem(position);
            // Remove the task
            if (!ObjectUtils.isNull(task) && mTaskDAO.delete(task) == DatabaseCode.OK) {
                mAdapter.remove(task);
            }
        }

        @Override
        public TaskAdapter getAdapter() {
            return mAdapter;
        }

        @Override
        public void setAdapter(TaskAdapter view) {
            mAdapter = view;
        }

        @Override
        public Setting getSettings() {
            return mSettings;
        }

        @Override
        public void setSettings(Setting settings) {
            mSettings = settings;
        }

        @Override
        public TaskDAO getTaskDAO() {
            return mTaskDAO;
        }

        @Override
        public void setTaskDAO(TaskDAO taskDAO) {
            mTaskDAO = taskDAO;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Only display the context action bar (CAB) if confirmation is true
            if (mSettings.isDeleteConfirm()) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Get the id and decide whether to remove the items
            int id = item.getItemId();
            if (id == R.id.deleteButton && mSettings.isDeleteConfirm()) {
                // Calls getSelectedItems method from TaskAdapter class
                SparseBooleanArray selectedItems = mAdapter.getSelectedItems();
                // Captures all selectedItems items with a loop
                for (int i = selectedItems.size() - 1; i >= 0; i--) {
                    // If the item is selected
                    if (selectedItems.valueAt(i)) {
                        // The retrieve the position and delete
                        delete(selectedItems.keyAt(i));
                    }
                }
                // Destroy the selections
                mAdapter.destroySelection();

                // Close the context action bar (CAB)
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.destroySelection();
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if (checked && !mSettings.isDeleteConfirm()) {
                delete(position);

                // Close the context action bar (CAB)
                mode.finish();
            } else {
                // Calls toggleSelectedItem method from TaskAdapter class
                mAdapter.toggleSelectedItem(position);
                // Set the CAB title according to total checked items
                mode.setTitle(mAdapter.getSelectedItemsCount() + " selected");
            }
        }
    }

    /**
     * Called when a listview item is selected on short click
     */
    private class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get the task at the clicked position. This will be serialized so it can be passed to the EditActivity
            Task task = (Task) parent.getItemAtPosition(position);
            // Create a new intent to pass to the EditActivity
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            // Put the task object reference as extra
            intent.putExtra(ActivityCode.INTENT_OBJECT, task);
            // Specify the request code which will be read when editing
            intent.putExtra(ActivityCode.INTENT_REQUEST_CODE, ActivityCode.EDIT_ACTIVITY_EDIT);
            // Start the activity with the purpose to capture the result
            startActivityForResult(intent, ActivityCode.EDIT_ACTIVITY_EDIT);
        }
    }
}
