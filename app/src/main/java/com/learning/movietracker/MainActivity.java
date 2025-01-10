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
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationBarView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.learning.movietracker.databinding.ActivityMainBinding;
import com.learning.movietracker.viewmodel.MainActivityViewModel;

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
                    i.putExtra("isFromWatchlist", false);
                    startActivity(i);
                }
                return false;
            }
        });



//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open,
//                R.string.nav_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id, new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.savedPosts);
//        }

//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.titleTooleBar);
        // Get the fragment reference
//        if (fragment instanceof HomeFragment) {
//            HomeFragment myFrag = new HomeFragment();
//            ConstraintLayout constraintLayout = myFrag.getView().findViewById(R.id.titleTooleBar);

            // Use the textView reference
//        }
    }

    private void showOnlyBottomNavBar(boolean isShowBottomNavBar) {
        View navMenu = findViewById(R.id.mainActivityNavMenu);
        navMenu.setVisibility(View.VISIBLE);
    }
}