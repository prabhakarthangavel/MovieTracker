package com.learning.movietracker.model.addreview;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddReview extends BaseObservable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("stars")
    @Expose
    private Integer stars;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("isFirstTimeWatched")
    @Expose
    private boolean isFirstTimeWatched;
    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isFirstTimeWatched() {
        return isFirstTimeWatched;
    }

    public void setFirstTimeWatched(boolean firstTimeWatched) {
        isFirstTimeWatched = firstTimeWatched;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
