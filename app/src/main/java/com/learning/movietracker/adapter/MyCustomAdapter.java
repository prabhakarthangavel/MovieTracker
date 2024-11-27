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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.movietracker.MainActivity;
import com.learning.movietracker.MovieDetailsActivity;
import com.learning.movietracker.R;
import com.learning.movietracker.databinding.MovieTileBinding;
import com.learning.movietracker.model.Movie;

import java.util.ArrayList;
import java.util.Objects;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MovieViewHolder> {

    private final Context context;
    private ArrayList<Movie> movieArrayList;

    public MyCustomAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieTileBinding movieTileBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_tile, parent, false);
        return new MovieViewHolder(movieTileBinding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        holder.movieTileBinding.setMovie(movie);
        holder.fetchWatchlist(movie);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

        public MovieViewHolder(MovieTileBinding movieTileBinding, Context context) {
            super(movieTileBinding.getRoot());
            this.movieTileBinding = movieTileBinding;

            movieTileBinding.addWatchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("tbl_watchlist");
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isValueFound = false;
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Movie watchlistMovie = snapshot1.getValue(Movie.class);
                                if (watchlistMovie != null && Objects.equals(watchlistMovie.getId(), movieTileBinding.getMovie().getId())) {
                                    isValueFound = true;
                                    snapshot1.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                movieTileBinding.addWatchlist.setImageResource(R.drawable.baseline_bookmark_24_green);
                                                movieTileBinding.addWatchlistIcon.setImageResource(R.drawable.baseline_add_24);
                                            }
                                        }
                                    });
                                }
                            }
                            if (!isValueFound) {
                                String id = dbRef.push().getKey(); //generates new id for new entry
                                Movie movie = movieTileBinding.getMovie();
                                dbRef.child(id).setValue(movie);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });

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

            movieTileBinding.addWatchlist.setImageResource(R.drawable.baseline_bookmark_24_green);
            movieTileBinding.addWatchlistIcon.setImageResource(R.drawable.baseline_add_24);
        }

        private void fetchWatchlist(Movie movie) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference("tbl_watchlist");
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Movie watchlistMovie = snapshot1.getValue(Movie.class);
                        if (watchlistMovie != null && Objects.equals(movie.getId(), watchlistMovie.getId())) {
                            movieTileBinding.addWatchlist.setImageResource(R.drawable.baseline_bookmark_24_red);
                            movieTileBinding.addWatchlistIcon.setImageResource(R.drawable.baseline_close_24);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
