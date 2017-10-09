package com.conceptappsworld.newsaholics;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private static final int DEFAUL_MAX_RESULT = 10;
    private static final int LOADER_ID = 1;
    private LoaderManager loaderManager;
    private ArrayList<News> alNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "TEST onCreate");

        findViewByIds();
        rvNewsSetup();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = getSupportLoaderManager();

        connectionDetector = new ConnectionDetector(MainActivity.this);

        if (!connectionDetector.isConnectingToInternet()) {
            //No Internet
            loadingIndicator.setVisibility(View.GONE);
            tvEmpty.setText(getResources().getString(R.string.no_internet));
            return;
        } else {
            loadingIndicator.setVisibility(View.GONE);
            initLoader();
//            dummyData();

//            newsAdapter.notifyDataSetChanged();
        }
    }

    private void initLoader() {
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    private void rvNewsSetup() {
        Log.i(LOG_TAG, "TEST lvBookSetup");

        alNews = new ArrayList<News>();

        // Create a new {@link ArrayAdapter} of book
        newsAdapter = new NewsAdapter(this, alNews);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        rvNews.setAdapter(newsAdapter);
    }

    private void findViewByIds() {
        rvNews = (RecyclerView) findViewById(R.id.rv_news);
        tvEmpty = (TextView) findViewById(R.id.empty_view);
        tvSearchResult = (TextView) findViewById(R.id.tv_search_result);
        loadingIndicator = (View) findViewById(R.id.loading_indicator);

//        tvSearchResult.setText("Hi");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNews.setLayoutManager(linearLayoutManager);
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST onCreateLoader");

        Uri baseUri = Uri.parse(ConstantUtil.URL_ENDPOINT);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("api-key", ConstantUtil.API_KEY);

        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        Log.i(LOG_TAG, "TEST onLoadFinished");

        loadingIndicator.setVisibility(View.GONE);
        rvNews.setVisibility(View.VISIBLE);
        if(alNews != null){
            alNews.clear();
        }

        if (data != null && !data.isEmpty()) {
            if(data.size() > 0){
                alNews.addAll(data);
                newsAdapter.notifyDataSetChanged();
            } else {
                tvEmpty.setText(getResources().getString(R.string.no_books));
            }

        }else {
            Log.i(LOG_TAG, "TEST data empty");
            tvEmpty.setText(getResources().getString(R.string.no_books));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "TEST onLoaderReset");
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

    private void dummyData(){
        News news1 = new News("hi", "2017-05-03", "hello");
        alNews.add(news1);

        News news2 = new News("hi2", "2017-05-03", "hello2");
        alNews.add(news2);

        News news3 = new News("hi3", "2017-05-03", "hello3");
        alNews.add(news3);

    }
}
