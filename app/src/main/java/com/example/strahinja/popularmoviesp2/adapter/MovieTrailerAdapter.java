package com.example.strahinja.popularmoviesp2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.strahinja.popularmoviesp2.R;
import com.example.strahinja.popularmoviesp2.model.Trailer.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder>{


    List<Trailer> mList;
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.trailer_image);
        }
    }

    public MovieTrailerAdapter(List<Trailer> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.ViewHolder holder, int position) {
        final String YOUTUBE_BASE_URL = "http://img.youtube.com/vi/";

        Trailer trailer= mList.get(position);

        Picasso.with(mContext).load(YOUTUBE_BASE_URL + trailer.getKey() + "/0.jpg").error(R.drawable.noimagefound).into(holder.imageView);

        holder.imageView.setOnClickListener((v)->{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
            if(intent.resolveActivity(mContext.getPackageManager())!= null) mContext.startActivity(intent);
            else Toast.makeText(mContext, "No suitable app installed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
