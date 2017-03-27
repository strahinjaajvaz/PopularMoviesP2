package com.example.strahinja.popularmoviesp2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strahinja.popularmoviesp2.R;
import com.example.strahinja.popularmoviesp2.model.Review.Review;


import java.util.List;


public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder>{


    List<Review> mList;
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;

        public ViewHolder(View v) {
            super(v);
             author = (TextView) v.findViewById(R.id.review_author);
             content = (TextView) v.findViewById(R.id.review_content);
        }
    }

    public MovieReviewAdapter(List<Review> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.ViewHolder holder, int position) {
        Review review= mList.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());

        holder.content.setOnClickListener((v)->{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(review.getUrl()));
            if(intent.resolveActivity(mContext.getPackageManager())!= null) mContext.startActivity(intent);
            else Toast.makeText(mContext, "No suitable app installed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
