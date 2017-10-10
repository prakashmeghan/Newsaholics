package com.conceptappsworld.newsaholics.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conceptappsworld.newsaholics.R;
import com.conceptappsworld.newsaholics.model.News;
import com.conceptappsworld.newsaholics.util.CommonUtil;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<News> listData;
    private Context context;

    public NewsAdapter(ArrayList<News> alNews, Context _context) {
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

        String dateStr = news.getPublicationDate();
        holder.tvPublicationDate.setText(CommonUtil.separateDate(dateStr));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvSection;
        TextView tvPublicationDate;

        public NewsViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSection = (TextView) itemView.findViewById(R.id.tv_section);
            tvPublicationDate = (TextView) itemView.findViewById(R.id.tv_publication);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String webUrl = "";
            if (listData != null && listData.get(getAdapterPosition()).getWebUrl() != null)
                webUrl = listData.get(getAdapterPosition()).getWebUrl();

            if (webUrl != "") {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                context.startActivity(browserIntent);
            }
        }
    }
}
