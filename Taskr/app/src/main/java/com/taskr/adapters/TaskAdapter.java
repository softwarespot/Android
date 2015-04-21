package com.taskr.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.taskr.R;
import com.taskr.models.Setting;
import com.taskr.models.Task;
import com.taskr.utilities.ObjectUtils;
import com.taskr.utilities.StringUtils;

import java.util.ArrayList;

// Idea: http://theopentutorials.com/examples/android/listview/android-contextual-action-bar-for-listview-item-deletion-using-actionbarsherlock/ & http://software-workshop.eu/content/swiping-listview-elements

/**
 * Created by SoftwareSpot on 24/02/2015.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    private final Context mContext;
    private SparseBooleanArray mSelectedItems;
    private Setting mSettings;
    private ArrayList<Task> mTasks;

    public TaskAdapter(Context context, ArrayList<Task> tasks, Setting settings) {
        super(context, R.layout.single_row, R.id.titleListItem, tasks);
        mContext = context;
        mSelectedItems = new SparseBooleanArray();
        mSettings = settings;
        mTasks = tasks;
    }

    @Override
    public void add(Task object) {
        if (ObjectUtils.isNull(object)) {
            return;
        }
        if (ObjectUtils.isNull(mTasks)) {
            mTasks = new ArrayList<>(); // Create an new array list if null
        }
        if (mTasks.add(object)) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(Task object) {
        if (ObjectUtils.isNull(object) || ObjectUtils.isNull(mTasks)) {
            return;
        }
        if (mTasks.remove(object)) {
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (ObjectUtils.isNull(row)) { // Optimised by 175%
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row, parent, false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        try {
            Task task = mTasks.get(position);
            String firstLetter = StringUtils.getFirstLetter(task.getName());
            viewHolder.getImage().setImageDrawable(
                    TextDrawable.builder()
                    .buildRect(firstLetter, task.getColor())
            );
            viewHolder.getTitle().setText(task.getName());
            viewHolder.getDescription().setText(StringUtils.compact(task.getDescription(), mSettings.getDescriptionLength()));

            if (!mSettings.isImageDisplay()) {
                viewHolder.getImage().setLeft(viewHolder.getImage().getWidth() * -1);
            }
            // viewHolder.getImage().setVisibility(mSettings.isImageDisplay() ? View.VISIBLE : View.INVISIBLE);

            viewHolder.getDescription().setVisibility(mSettings.isDescriptionDisplay() ? View.VISIBLE : View.INVISIBLE);

            viewHolder.getTitle().setAllCaps(true);
            if (mSelectedItems.get(position)) {
                row.setBackgroundColor(Color.LTGRAY);
            } else {
                row.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return row;
    }

    public void destroySelection() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedItems() {
        return mSelectedItems;
    }

    public int getSelectedItemsCount() {
        return mSelectedItems.size();
    }

    public ArrayList<Task> getTasks() {
        return mTasks;
    }

    public void setSelectedItem(int position, boolean value) {
        if (value) {
            mSelectedItems.put(position, value);
        } else {
            mSelectedItems.delete(position);
        }
        notifyDataSetChanged();
    }

    public void toggleSelectedItem(int position) {
        setSelectedItem(position, !mSelectedItems.get(position));
    }

    public void update(Task task) {
        if (ObjectUtils.isNull(task) || ObjectUtils.isNull(mTasks)) {
            return;
        }
        int index = mTasks.indexOf(task);
        if (index != -1) {
            mTasks.set(index, task);
            notifyDataSetChanged();
        }
    }

    public void update(Setting settings) {
        mSettings = settings;
        notifyDataSetChanged();
    }

    private static class ViewHolder { // Internal class for holding the list item views
        private final TextView mDescription;
        private final ImageView mImage;
        private final TextView mTitle;

        ViewHolder(View view) {
            mTitle = (TextView) view.findViewById(R.id.titleListItem);
            mDescription = (TextView) view.findViewById(R.id.descriptionListItem);
            mImage = (ImageView) view.findViewById(R.id.imageListItem);
        }

        public TextView getDescription() {
            return mDescription;
        }

        public ImageView getImage() {
            return mImage;
        }

        public TextView getTitle() {
            return mTitle;
        }
    }
}