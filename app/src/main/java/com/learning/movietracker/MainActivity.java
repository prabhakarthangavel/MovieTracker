package com.learning.movietracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.learning.movietracker.adapter.MyCustomAdapter;
import com.learning.movietracker.adapter.SearchListAdaptor;
import com.learning.movietracker.databinding.ActivityMainBinding;
import com.learning.movietracker.model.Movie;
import com.learning.movietracker.model.searchmovies.MovieResults;
import com.learning.movietracker.model.searchmovies.SearchMovieResult;
import com.learning.movietracker.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mainBinding.mainActivityNavMenu.bottomNavigationMenu.setSelectedItemId(R.id.mainActivityNavMenu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, new HomeFragment());
        fragmentTransaction.commit();

        // handle bottom navigation
        mainBinding.mainActivityNavMenu.bottomNavigationMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("watchlists")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new WatchlistFragment()).commit();
                    return true;
                }else if (item.getTitle().toString().equalsIgnoreCase("home")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new HomeFragment()).commit();
                    return true;
                }else if (item.getTitle().toString().equalsIgnoreCase("add post")) {
                    Intent i = new Intent(getApplicationContext(), AddReviewActivity.class);
                    startActivity(i);
                }
                return false;
            }
        });
    }

    private void showOnlyBottomNavBar(boolean isShowBottomNavBar) {
        View navMenu = findViewById(R.id.mainActivityNavMenu);
        navMenu.setVisibility(View.VISIBLE);
    }
}