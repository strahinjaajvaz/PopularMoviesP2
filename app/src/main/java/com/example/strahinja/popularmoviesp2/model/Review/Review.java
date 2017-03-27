package com.example.strahinja.popularmoviesp2.model.Review;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.strahinja.popularmoviesp2.model.Movie.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by strahinja on 29/10/2016.
 */

public class Review implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("url")
    @Expose
    private String url;

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Review(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.id = data[0];
        this.author = data[1];
        this.content = data[2];
        this.url = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,
                this.author,
                this.content,
                this.url});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
