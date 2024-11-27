package com.learning.movietracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.movietracker.adapter.WatchlistAdapter;
import com.learning.movietracker.databinding.WatchlistsBinding;
import com.learning.movietracker.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class Watchlists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchlists);

        WatchlistsBinding watchlistsBinding = DataBindingUtil.setContentView(this, R.layout.watchlists);
        displayWatchlistItems(watchlistsBinding.watchlistItems);

        watchlistsBinding.mainActivityNavMenu.bottomNavigationMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("home")) {
                    Intent i = new Intent(Watchlists.this, MainActivity.class);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
    }

    void displayWatchlistItems(RecyclerView watchlistRecycler) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("tbl_watchlist");
        List<Movie> movies = new ArrayList<>();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    movies.add(snapshot1.getValue(Movie.class));
                }
                WatchlistAdapter adapter = new WatchlistAdapter(getApplicationContext(), movies);
                watchlistRecycler.setItemAnimator(new DefaultItemAnimator());
                watchlistRecycler.setAdapter(adapter);
                watchlistRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
