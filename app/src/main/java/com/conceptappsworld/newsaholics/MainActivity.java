package com.conceptappsworld.newsaholics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.conceptappsworld.newsaholics.adapter.NewsAdapter;
import com.conceptappsworld.newsaholics.loader.NewsLoader;
import com.conceptappsworld.newsaholics.model.News;
import com.conceptappsworld.newsaholics.util.ConnectionDetector;
import com.conceptappsworld.newsaholics.util.ConstantUtil;
import com.conceptappsworld.newsaholics.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView rvNews;
    private View loadingIndicator;
    private TextView tvEmpty, tvSearchResult;

    NewsAdapter newsAdapter;
    private ConnectionDetector connectionDetector;
    private static final int LOADER_ID = 1;
    private LoaderManager loaderManager;
    private ArrayList<News> alNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIds();
        rvNewsSetup();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = getSupportLoaderManager();

        connectionDetector = new ConnectionDetector(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!connectionDetector.isConnectingToInternet()) {
            //No Internet
            loadingIndicator.setVisibility(View.GONE);
            tvSearchResult.setVisibility(View.GONE);
            rvNews.setVisibility(View.GONE);
            tvEmpty.setText(getResources().getString(R.string.no_internet));
            return;
        } else {
            loadingIndicator.setVisibility(View.VISIBLE);
            tvSearchResult.setVisibility(View.VISIBLE);
            rvNews.setVisibility(View.VISIBLE);
            initLoader();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initLoader() {
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    private void rvNewsSetup() {
        alNews = new ArrayList<News>();

        // Create a new {@link ArrayAdapter} of News
        newsAdapter = new NewsAdapter(alNews, MainActivity.this);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        rvNews.setAdapter(newsAdapter);
    }

    private void findViewByIds() {
        rvNews = (RecyclerView) findViewById(R.id.rv_news);
        tvEmpty = (TextView) findViewById(R.id.empty_view);
        tvSearchResult = (TextView) findViewById(R.id.tv_search_result);
        loadingIndicator = (View) findViewById(R.id.loading_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNews.setLayoutManager(linearLayoutManager);
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String keyword = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default));

        tvSearchResult.setText(getResources().getString(R.string.search_result) + " " +
                keyword);

        Uri baseUri = Uri.parse(ConstantUtil.URL_ENDPOINT);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", keyword);
        uriBuilder.appendQueryParameter("api-key", ConstantUtil.API_KEY);

        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        if (connectionDetector.isConnectingToInternet()) {
            loadingIndicator.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);
            if (alNews != null) {
                alNews.clear();
            }

            if (data != null && !data.isEmpty()) {
                if (data.size() > 0) {
                    alNews.addAll(data);
                    newsAdapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setText(getResources().getString(R.string.no_news));
                }

            } else {
                tvEmpty.setText(getResources().getString(R.string.no_news));
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String keyword = sharedPrefs.getString(
                    getString(R.string.settings_topic_key),
                    getString(R.string.settings_topic_default));

            tvSearchResult.setText(getResources().getString(R.string.search_result) + " " +
                    keyword);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
