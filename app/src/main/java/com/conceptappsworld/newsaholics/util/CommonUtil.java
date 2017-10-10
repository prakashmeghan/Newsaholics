package com.conceptappsworld.newsaholics.util;

import android.text.TextUtils;
import android.util.Log;

import com.conceptappsworld.newsaholics.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    private static final String LOG_TAG = "CommonUtil";

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<News> extractFeatureFromJson(String newsJson) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newses = new ArrayList<News>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJson);

            JSONObject responseObj = baseJsonResponse.getJSONObject(ConstantUtil.NODE_RESPONSE);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or news).
            JSONArray itemsArray = responseObj.getJSONArray(ConstantUtil.NODE_RESULTS);

            // For each item in the itemsArray, create an {@link News} object
            for (int i = 0; i < itemsArray.length(); i++) {
                // Get a single News at position i within the list of itemsArray
                JSONObject currentResult = itemsArray.getJSONObject(i);

                String title = "";
                // Extract the value for the key called "webTitle"
                if (currentResult.has(ConstantUtil.NODE_WEBTITLE)) {
                    title = currentResult.getString(ConstantUtil.NODE_WEBTITLE);
                }

                String publicationDate = "";
                // Extract the value for the key called "webPublicationDate"
                if (currentResult.has(ConstantUtil.NODE_WEBPUBLICATIONDATE)) {
                    publicationDate = currentResult.getString(ConstantUtil.NODE_WEBPUBLICATIONDATE);
                }

                String sectionName = "";
                // Extract the value for the key called "sectionName"
                if (currentResult.has(ConstantUtil.NODE_SECTIONNAME)) {
                    sectionName = currentResult.getString(ConstantUtil.NODE_SECTIONNAME);
                }

                String webUrl = "";
                // Extract the value for the key called "webUrl"
                if (currentResult.has(ConstantUtil.NODE_WEBURL)) {
                    webUrl = currentResult.getString(ConstantUtil.NODE_WEBURL);
                }

                News news = new News(title, publicationDate, sectionName, webUrl);

                newses.add(news);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of News
        return newses;
    }

    public static String separateDate(String dateStr) {
        String dateOnly = "";
        String[] splitedDate = dateStr.split("T");

        if (splitedDate.length > 0)
            dateOnly = splitedDate[0];

        return dateOnly;
    }
}
