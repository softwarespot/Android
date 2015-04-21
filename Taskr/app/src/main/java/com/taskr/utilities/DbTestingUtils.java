package com.taskr.utilities;

import android.content.Context;

import com.taskr.db.TaskDAO;

import java.util.Random;

/**
 * Created by SoftwareSpot on 06/04/2015.
 */
public class DbTestingUtils {
    /**
     * Generate random data inside the table
     *
     * @param context Current activity context
     */
    public static void generateData(Context context) {
        // Open the task data access object
        TaskDAO taskDAO = new TaskDAO(context);
        taskDAO.open();

        // Only create new entries if the database is empty
        if (taskDAO.getCount() == 0) {
            // Delete the task table
            taskDAO.deleteAll();

            // Random titles
            String[] titles = { "Buy milk",
                    "Visit Julia",
                    "Wash car",
                    "Complete Mobile assignment",
                    "Apply for certification",
                    "Contact Dad & Mum" };

            // Random descriptions
            String[] descriptions = { "Some random description", "This description has no meaning!", "What does this mean?" };

            // Generate random tasks
            Random random = new Random();
            int length = descriptions.length - 1;
            for (int i = 0; i < titles.length; i++) {
                taskDAO.insert(titles[i], random.nextBoolean() ? descriptions[random.nextInt(length)] : StringUtils.EMPTY);
            }
        }

        // Close the task data access object
        taskDAO.close();
    }
}
