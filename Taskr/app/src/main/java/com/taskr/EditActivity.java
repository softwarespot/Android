package com.taskr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taskr.enums.ActivityCode;
import com.taskr.models.Task;
import com.taskr.utilities.StringUtils;
import com.taskr.utilities.ToastUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class EditActivity extends ActionBarActivity {
    private int mRequestCode;
    private Task mTask;
    private TextView mTaskDate;
    private EditText mTaskDescription;
    private EditText mTaskName;

    /**
     * Called when a button is selected
     *
     * @param view
     */
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.saveButton) {
            String name = mTaskName.getText().toString();
            if (StringUtils.isNullOrEmpty(name)) {
                ToastUtils.toastError(this, getString(R.string.edit_title_empty));
            } else {
                boolean isEqual = true;
                String description = mTaskDescription.getText().toString();
                if (mRequestCode == ActivityCode.EDIT_ACTIVITY_ADD) {
                    isEqual = false; // Set to false as it's a new addition and therefore won't be equal
                    mTask = Task.create(name, description);
                } else if (mRequestCode == ActivityCode.EDIT_ACTIVITY_EDIT) {
                    isEqual = name.equals(mTask.getName()) && description.equals(mTask.getDescription());
                    if (!isEqual) { // Clearly there was an update as either the name or description are not equal
                        mTask.setDescription(description);
                        mTask.setName(name);
                    }
                }

                // Create a new intent to pass to the MainActivity
                Intent intent = new Intent();
                // Put the task object reference as extra
                intent.putExtra(ActivityCode.INTENT_OBJECT, mTask);
                // Set the result and pass the intent
                setResult(isEqual ? RESULT_CANCELED : RESULT_OK, intent);
                // On close finish
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        mTask = (Task) intent.getSerializableExtra(ActivityCode.INTENT_OBJECT); // Get the task object reference

        mTaskName = (EditText) findViewById(R.id.titleText);
        mTaskDescription = (EditText) findViewById(R.id.descriptionText);
        mTaskDate = (TextView) findViewById(R.id.dateText);

        mRequestCode = intent.getIntExtra(ActivityCode.INTENT_REQUEST_CODE, ActivityCode.NONE);
        if (mRequestCode == ActivityCode.EDIT_ACTIVITY_ADD || mRequestCode == ActivityCode.NONE) {
            setTitle(getString(R.string.edit_adding_task));
        } else if (mRequestCode == ActivityCode.EDIT_ACTIVITY_EDIT) {
            String name = mTask.getName();
            mTaskName.setText(name);
            mTaskDescription.setText(mTask.getDescription());
            mTaskDate.setText(StringUtils.dateToString(mTask.getAdded()));

            setTitle(String.format(getString(R.string.edit_editing_task), name));
        }
    }

    @Override
    protected void onDestroy() {
        Crouton.clearCroutonsForActivity(this); // Clear any croutons displayed within the activity
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
