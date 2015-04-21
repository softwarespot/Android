package com.taskr.models;

import com.taskr.db.DatabaseHelper;
import com.taskr.utilities.ColorUtils;
import com.taskr.utilities.DateUtils;
import com.taskr.utilities.ObjectUtils;
import com.taskr.utilities.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SoftwareSpot on 16/02/2015.
 */
public class Task implements Comparable<Task>, Serializable {
    private final long id;
    private Date added;
    private int color;
    private String description;
    private String name;

    public Task(long id, String name, String description, Date added, int color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.added = added;
        this.color = color;
    }

    public static Task create(String name, String description) {
        return new Task(DatabaseHelper.DEFAULT_ID, name, description, DateUtils.getCurrentDate(), ColorUtils.getRandomColor());
    }

    @Override
    public int compareTo(Task task) {
        return added.compareTo(task.added);
    }

    @Override
    public boolean equals(Object o) {
        if (ObjectUtils.isNull(o)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        int hash = (int) id; // Based on the fibonacci sequence
        hash += name.hashCode();
        hash = hash * 2 + name.hashCode();
        hash = hash * 3 + description.hashCode();
        hash = hash * 5 + added.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%d: %s, %s, %s", id, name, description, StringUtils.dateToString(added));
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEquals(Object o) {
        if (ObjectUtils.isNull(o)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return hashCode() == task.hashCode() || id == task.id && name.equals(task.name) && description.equals(task.description) && added.equals(task.added);
    }
}
