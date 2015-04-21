package com.taskr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.taskr.enums.ActivityCode;
import com.taskr.enums.SortCode;
import com.taskr.models.Setting;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class SettingsActivity extends ActionBarActivity {
    private static final byte sSortIndex_DateAsc = 0;
    private static final byte sSortIndex_DateDesc = 1;
    private static final byte sSortIndex_NameAsc = 2;
    private static final byte sSortIndex_NameDesc = 3;
    private Setting mSettings;
    private Spinner mSortCode;

    /**
     * Convert an enumeration value to an integer index value
     *
     * @param sortCode value
     * @return 0th-based integer index value
     */
    private static byte sortCodeToSortIndex(SortCode sortCode) {
        if (sortCode == SortCode.DATE_DESC) {
            return sSortIndex_DateDesc;
        } else if (sortCode == SortCode.NAME_ASC) {
            return sSortIndex_NameAsc;
        } else if (sortCode == SortCode.NAME_DESC) {
            return sSortIndex_NameDesc;
        } else {
            return sSortIndex_DateAsc;
        }
    }

    /**
     * Convert an integer index value to sortcode enumeration value
     *
     * @param index 0th-based integer index value
     * @return Sortcode enumeration value
     */
    private static SortCode sortIndexToSortCode(byte index) {
        if (index == sSortIndex_DateDesc) {
            return SortCode.DATE_DESC;
        } else if (index == sSortIndex_NameAsc) {
            return SortCode.NAME_ASC;
        } else if (index == sSortIndex_NameDesc) {
            return SortCode.NAME_DESC;
        } else {
            return SortCode.DATE_ASC;
        }
    }

    /**
     * Called when any button in the activity is selected
     *
     * @param view
     */
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.saveButton) {
            // Set the settings sortcode
            mSettings.setSortCode(sortIndexToSortCode((byte) mSortCode.getSelectedItemPosition()));

            // Create a new intent to pass to the MainActivity
            Intent intent = new Intent();
            // Put the task object reference as extra
            intent.putExtra(ActivityCode.INTENT_OBJECT, mSettings);
            // Set the result and pass the intent
            setResult(RESULT_OK, intent);
            // On close finish
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettings = (Setting) getIntent().getSerializableExtra(ActivityCode.INTENT_OBJECT);

        // Find the controls on the activity
        mSortCode = (Spinner) findViewById(R.id.sortcodeSpinner);
        Switch descriptionDisplay = (Switch) findViewById(R.id.descriptionDisplayToggle);
        Switch deleteConfirm = (Switch) findViewById(R.id.deleteConfirmToggle);
        Switch imageDisplay = (Switch) findViewById(R.id.imageDisplayToggle);

        imageDisplay.setEnabled(false); // Disabled

        mSortCode.setSelection(sortCodeToSortIndex(mSettings.getSortCode())); // Set the selection by translating the sortcode enumeration to an index value
        mSortCode.setOnItemSelectedListener(new SpinnerOnItemSelectedListener()); // Set the onselected listener

        CompoundButton.OnCheckedChangeListener onChecked = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.descriptionDisplayToggle:
                        mSettings.setDescriptionDisplay(buttonView.isChecked());
                        break;

                    case R.id.deleteConfirmToggle:
                        mSettings.setDeleteConfirm(buttonView.isChecked());
                        break;

                    case R.id.imageDisplayToggle:
                        mSettings.setImageDisplay(buttonView.isChecked());
                        break;
                }
            }
        };
        descriptionDisplay.setOnCheckedChangeListener(onChecked);
        descriptionDisplay.setChecked(mSettings.isDescriptionDisplay());

        deleteConfirm.setOnCheckedChangeListener(onChecked);
        deleteConfirm.setChecked(mSettings.isDeleteConfirm());

        imageDisplay.setOnCheckedChangeListener(onChecked);
        imageDisplay.setChecked(mSettings.isImageDisplay());
    }

    @Override
    protected void onDestroy() {
        Crouton.clearCroutonsForActivity(this); // Clear any croutons displayed within the activity
        super.onDestroy();
    }

    /**
     * Called when the spinner view is selected
     */
    private static class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
