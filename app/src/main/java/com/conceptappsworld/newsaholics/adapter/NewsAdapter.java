package com.conceptappsworld.newsaholics.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conceptappsworld.newsaholics.R;
import com.conceptappsworld.newsaholics.model.News;

import java.util.ArrayList;

/**
 * Created by Sprim on 09-10-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private ArrayList<News> listData;
    private Context context;


    public NewsAdapter(Context _context, ArrayList<News> alNews){
        this.listData = alNews;
        this.context = _context;
    }

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        NewsViewHolder mViewHolder = new NewsViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        News news = listData.get(position);

        holder.tvTitle.setText(news.getTitle());
        holder.tvSection.setText(news.getSectionName());
        holder.tvPublicationDate.setText(news.getPublicationDate());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvSection;
        TextView tvPublicationDate;

        public NewsViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSection = (TextView) itemView.findViewById(R.id.tv_section);
            tvPublicationDate = (TextView) itemView.findViewById(R.id.tv_publication);
        }
    }
}
