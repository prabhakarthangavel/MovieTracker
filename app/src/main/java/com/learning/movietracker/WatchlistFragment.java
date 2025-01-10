package com.learning.movietracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.movietracker.adapter.WatchlistAdapter;
import com.learning.movietracker.adaterInerface.WatchlistListener;
import com.learning.movietracker.databinding.WatchlistsBinding;
import com.learning.movietracker.model.Movie;
import com.learning.movietracker.model.addreview.AddReview;
import com.learning.movietracker.model.searchmovies.MovieResults;
import com.learning.movietracker.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WatchlistFragment extends Fragment implements WatchlistListener {

    private MainActivityViewModel viewModel;
    private WatchlistsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstaceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.watchlists, container, false);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        displayWatchlistItems(binding.watchlistItems, this);
    }

    void displayWatchlistItems(RecyclerView watchlistRecycler, WatchlistListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("tbl_watchlist");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Movie> movies = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    movies.add(snapshot1.getValue(Movie.class));
                }
                WatchlistAdapter adapter = new WatchlistAdapter(movies, listener);
                watchlistRecycler.setItemAnimator(new DefaultItemAnimator());
                watchlistRecycler.setAdapter(adapter);
                watchlistRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onWatchlistDeleteClick(Movie movie) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("tbl_watchlist");
        Query query = dbRef.orderByChild("id").equalTo(movie.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Removed from watchlist successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void addMoviewReviewClick(Movie movie) {
        Intent intent = new Intent(getContext(), AddReviewActivity.class);
        intent.putExtra("isFromWatchlist", true);
        intent.putExtra("movieId", movie.getId());
        intent.putExtra("movieTitle", movie.getTitle());
        intent.putExtra("moviePosterPath", movie.getPosterPath());
        intent.putExtra("movieReleaseDate", movie.getReleaseDate());
        intent.putExtra("movieOverview", movie.getOverview());
        startActivity(intent);
    }
}
