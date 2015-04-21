package com.taskr.models;

import com.taskr.enums.SortCode;
import com.taskr.utilities.ObjectUtils;
import com.taskr.utilities.StringUtils;

import java.io.Serializable;

/**
 * Created by SoftwareSpot on 24/02/2015.
 */
public class Setting implements Serializable {
    private boolean deleteConfirm;
    private boolean descriptionDisplay;
    private int descriptionLength;
    private boolean imageDisplay;
    private boolean randomColor;
    private SortCode sort;

    public Setting() {
        sort = SortCode.NAME_ASC;
        descriptionDisplay = true;
        setDescriptionLength(0); // Set to default length
        deleteConfirm = true;
        randomColor = true;
        imageDisplay = true;
    }

    public Setting(SortCode sort, boolean descriptionDisplay, int descriptionLength, boolean deleteConfirm, boolean randomColor, boolean imageDisplay) {
        this.sort = sort;
        this.descriptionDisplay = descriptionDisplay;
        setDescriptionLength(descriptionLength);
        this.deleteConfirm = deleteConfirm;
        this.randomColor = randomColor;
        this.imageDisplay = imageDisplay;
    }

    public static Setting create(SortCode sort, boolean descriptionDisplay, int descriptionLength, boolean deleteConfirm, boolean randomColor, boolean imageDisplay) {
        return new Setting(sort, descriptionDisplay, descriptionLength, deleteConfirm, randomColor, imageDisplay);
    }

    @Override
    public boolean equals(Object o) {
        if (ObjectUtils.isNull(o)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Setting)) {
            return false;
        }
        Setting setting = (Setting) o;
        return hashCode() == setting.hashCode() || sort == setting.sort;
    }

    @Override
    public int hashCode() {
        return sort.hashCode();
    }

    public int getDescriptionLength() {
        return descriptionLength;
    }

    public void setDescriptionLength(int descriptionLength) {
        this.descriptionLength = (descriptionLength >= 5 && descriptionLength <= 50) ? descriptionLength : StringUtils.COMPACT_LENGTH;
    }

    public SortCode getSort() {
        return sort;
    }

    public void setSort(SortCode sort) {
        this.sort = sort;
    }

    public SortCode getSortCode() {
        return sort;
    }

    public void setSortCode(SortCode sort) {
        this.sort = sort;
    }

    public boolean isDeleteConfirm() {
        return deleteConfirm;
    }

    public void setDeleteConfirm(boolean deleteConfirm) {
        this.deleteConfirm = deleteConfirm;
    }

    public boolean isDescriptionDisplay() {
        return descriptionDisplay;
    }

    public void setDescriptionDisplay(boolean descriptionDisplay) {
        this.descriptionDisplay = descriptionDisplay;
    }

    public boolean isEquals(Object o) {
        if (ObjectUtils.isNull(o)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Setting)) {
            return false;
        }
        Setting setting = (Setting) o;
        return hashCode() == setting.hashCode() || sort == setting.sort;
    }

    public boolean isImageDisplay() {
        return imageDisplay;
    }

    public void setImageDisplay(boolean imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    public boolean isRandomColor() {
        return randomColor;
    }

    public void setRandomColor(boolean randomColor) {
        this.randomColor = randomColor;
    }
}
