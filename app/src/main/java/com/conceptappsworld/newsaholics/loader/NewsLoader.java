package com.conceptappsworld.newsaholics.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.conceptappsworld.newsaholics.QueryUtils;
import com.conceptappsworld.newsaholics.model.News;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<News> newses = QueryUtils.fetchNewsData(mUrl);
        return newses;
    }
}
