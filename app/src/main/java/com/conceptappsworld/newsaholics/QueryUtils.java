package com.conceptappsworld.newsaholics;

import android.util.Log;

import com.conceptappsworld.newsaholics.model.News;
import com.conceptappsworld.newsaholics.util.CommonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = "QueryUtils";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    /**
     * Query the Google News API and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = CommonUtil.createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = CommonUtil.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> newses = CommonUtil.extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}s
        return newses;
    }
}
