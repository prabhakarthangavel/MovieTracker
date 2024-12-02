package com.learning.movietracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.movietracker.R;
import com.learning.movietracker.adaterInerface.WatchlistListener;
import com.learning.movietracker.databinding.WatchlistItemBinding;
import com.learning.movietracker.model.Movie;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistHolder> {
    private List<Movie> movieList;
    private WatchlistListener listener;

    public WatchlistAdapter(List<Movie> movies, WatchlistListener listener) {
        this.movieList = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WatchlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WatchlistItemBinding watchlistItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.watchlist_item, parent, false);
        return new WatchlistHolder(watchlistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapter.WatchlistHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.watchlistItemBinding.setMovie(movie);
        holder.hideMovieRating(movie);
        holder.bindWatchlistDeleteEvent(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class WatchlistHolder extends RecyclerView.ViewHolder {
        private WatchlistItemBinding watchlistItemBinding;

        public WatchlistHolder(WatchlistItemBinding watchlistItemBinding) {
            super(watchlistItemBinding.getRoot());
            this.watchlistItemBinding = watchlistItemBinding;
        }

        private void hideMovieRating(Movie movie) {
            if (movie.getVoteAverage() <= 0) {
                View ratingView = watchlistItemBinding.movieRating.getRoot();
                ratingView.setVisibility(View.GONE);
            }
        }

        private void bindWatchlistDeleteEvent(Movie movie, WatchlistListener listener) {
            watchlistItemBinding.setMovie(movie);
            watchlistItemBinding.setClickListener(listener);
            watchlistItemBinding.executePendingBindings();
        }
    }
}
