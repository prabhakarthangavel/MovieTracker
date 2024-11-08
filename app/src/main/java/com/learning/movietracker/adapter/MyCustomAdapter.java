package com.learning.movietracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.movietracker.MainActivity;
import com.learning.movietracker.MovieDetailsActivity;
import com.learning.movietracker.R;
import com.learning.movietracker.databinding.MovieTileBinding;
import com.learning.movietracker.model.Movie;

import java.util.ArrayList;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MovieViewHolder> {

    private static Context context;
    private ArrayList<Movie> movieArrayList;

    public MyCustomAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieTileBinding movieTileBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_tile, parent, false);
        return new MovieViewHolder(movieTileBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        holder.movieTileBinding.setMovie(movie);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }


//    public View getView(int position, View convertView, ViewGroup parent) {
//        Movie movie = getItem(position);
//
//        ViewHolder holder = null;
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false);
//            holder = new ViewHolder();
//            holder.movieRating = convertView.findViewById(R.id.tvRating);
//            holder.movieTitle = convertView.findViewById(R.id.tvTitle);
//
//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.movieTitle.setText(movie.getTitle());
//        holder.movieRating.setText(movie.getVoteAverage().toString());
//
//        return convertView;
//    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private MovieTileBinding movieTileBinding;

        public MovieViewHolder(MovieTileBinding movieTileBinding) {
            super(movieTileBinding.getRoot());
            this.movieTileBinding = movieTileBinding;

            movieTileBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.i("**position**", movieTileBinding.getMovie().getId().toString());
                    Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                    movieDetailsIntent.putExtra("movieId", movieTileBinding.getMovie().getId().toString());
                    context.startActivity(movieDetailsIntent);
                }
            });
        }
    }
}
