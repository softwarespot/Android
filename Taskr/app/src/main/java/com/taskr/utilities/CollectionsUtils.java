package com.taskr.utilities;

import com.taskr.models.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by SoftwareSpot on 20/02/2015.
 * Idea: http://www.codejava.net/java-core/collections/sorting-list-collections-examples
 */
public class CollectionsUtils {
    private static final int EQUAL = 0;
    private static final int GREATER_THAN = 1;
    private static final int LESS_THAN = -1;

    public static void sortTasksByDateDesc(ArrayList<Task> tasks) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                switch (lhs.compareTo(rhs)) {
                    case GREATER_THAN:
                        return LESS_THAN;
                    case LESS_THAN:
                        return GREATER_THAN;
                    default:
                        return EQUAL;
                }
            }
        });
    }

    public static void sortTasksByNameAsc(List<Task> tasks) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }

    public static void sortTasksByNameDesc(List<Task> tasks) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                switch (lhs.getName().compareTo(rhs.getName())) {
                    case GREATER_THAN:
                        return LESS_THAN;
                    case LESS_THAN:
                        return GREATER_THAN;
                    default:
                        return EQUAL;
                }
            }
        });
    }
}
