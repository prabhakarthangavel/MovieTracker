package com.learning.movietracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.movietracker.R;
import com.learning.movietracker.databinding.WatchlistItemBinding;
import com.learning.movietracker.model.Movie;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistHolder> {
    private Context context;
    private List<Movie> movieList;

    public WatchlistAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    @NonNull
    @Override
    public WatchlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WatchlistItemBinding watchlistItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.watchlist_item, parent, false);
        return new WatchlistHolder(watchlistItemBinding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapter.WatchlistHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.watchlistItemBinding.setMovie(movie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public int getItemViewType(int position) { return position;}

    public static class WatchlistHolder extends RecyclerView.ViewHolder {
        private WatchlistItemBinding watchlistItemBinding;

        public WatchlistHolder(WatchlistItemBinding watchlistItemBinding, Context context) {
            super(watchlistItemBinding.getRoot());
            this.watchlistItemBinding = watchlistItemBinding;
        }
    }
}
