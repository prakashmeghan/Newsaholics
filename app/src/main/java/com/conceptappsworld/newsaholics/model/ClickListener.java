package com.conceptappsworld.newsaholics.model;

import android.view.View;

/**
 * Created by Sprim on 15-08-2016.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
