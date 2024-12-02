package com.learning.movietracker.adaterInerface;

import com.learning.movietracker.model.Movie;

public interface WatchlistListener {
    void onWatchlistDeleteClick(Movie movie);

    void addMoviewReviewClick(Movie movie);
}
