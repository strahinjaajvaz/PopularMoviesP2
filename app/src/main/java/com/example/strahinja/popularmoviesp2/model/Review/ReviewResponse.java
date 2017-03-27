package com.example.strahinja.popularmoviesp2.model.Review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReviewResponse {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("results")
    @Expose
    private List<Review> results = new ArrayList<Review>();

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public List<Review> getResults() {
        return results;
    }
}
