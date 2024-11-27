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

    private List<MovieResults> movieResultsList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        TextView popularMoviesTitle = findViewById(R.id.poupularMoviesTitle);
        popularMoviesTitle.setText(R.string.popularMovies);
        TextView nowShowingTitle = findViewById(R.id.nowShowingMoviesTitle);
        nowShowingTitle.setText(R.string.nowShowing);
        TextView topRated = findViewById(R.id.topRatedMoviesTitle);
        topRated.setText(R.string.topRated);
        TextView upcoing = findViewById(R.id.upcomingRatedMoviesTitle);
        upcoing.setText(R.string.upcoming);

        String[] moviesTypes = {"now_playing", "popular", "top_rated", "upcoming"};
        for (String moviesType : moviesTypes) {
            switch (moviesType) {
                case "now_playing":
                    getMoviesByType(moviesType, mainBinding.nowShowingrecyclerview, true);
                    break;
                case "popular":
                    getMoviesByType(moviesType, mainBinding.recyclerview, false);
                    break;
                case "top_rated":
                    getMoviesByType(moviesType, mainBinding.topRatedRecyclerview, false);
                    break;
                case "upcoming":
                    getMoviesByType(moviesType, mainBinding.upcomingMoviesRecyclerview, true);
                    break;
            }
        }

        mainBinding.searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    Drawable rightDrwable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icons8_clear_20);
                    mainBinding.searchbox.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrwable, null);
                    viewModel.searchBarKeyTyped(charSequence.toString());
                    getSearchedMoviesResult();
                } else {
                    Drawable leftDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icons8_search_20);
                    mainBinding.searchbox.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
                    showMainContent(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mainBinding.searchbox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Drawable rightDrwable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icons8_clear_20);
                    if (motionEvent.getRawX() >= (mainBinding.searchbox.getRight() - rightDrwable.getBounds().width() - mainBinding.searchbox.getCompoundPaddingRight())) {
                        mainBinding.searchbox.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        mainBinding.searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MovieResults movieResult = movieResultsList.get(position);
                Intent movieDetailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                movieDetailsIntent.putExtra("movieId", movieResult.getId().toString());
                startActivity(movieDetailsIntent);
            }
        });

        // handle bottom navigation
        mainBinding.mainActivityNavMenu.bottomNavigationMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("Watchlists")) {
                    Intent i = new Intent(MainActivity.this, Watchlists.class);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
    }

    private void getMoviesByType(String moviesType, RecyclerView recyclerView, boolean doLocallySort) {
        viewModel.getAllMovies(moviesType).observe(this, new Observer<List<Movie>>() {
            String[] regions = {"ta", "hi", "te", "ml", "kn"};

            @Override
            public void onChanged(List<Movie> moviesFromLiveData) {
                ArrayList<Movie> movies = new ArrayList<>();
                if (!moviesFromLiveData.isEmpty()) {
                    if (doLocallySort) {
                        for (int i = 0; i < regions.length; i++) {
                            Movie movieToRemove = null;
                            for (Movie movie : moviesFromLiveData) {
                                if (movie.getOriginalLanguage().equalsIgnoreCase(regions[i])) {
                                    movieToRemove = movie;
                                }
                            }
                            if (movieToRemove != null) {
                                moviesFromLiveData.remove(movieToRemove);
                                movies.add(movieToRemove);
                                i--;
                            }
                        }
                        movies.addAll(moviesFromLiveData);
                        displayMoviesInRecyclerView(movies, recyclerView);
                    }else {
                        movies.addAll(moviesFromLiveData);
                        displayMoviesInRecyclerView(movies, recyclerView);
                    }
                }
            }
        });
    }

    private void displayMoviesInRecyclerView(ArrayList<Movie> moviesList, RecyclerView recyclerView) {
        MyCustomAdapter adapter = new MyCustomAdapter(this, moviesList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getSearchedMoviesResult() {
        viewModel.getSearchedMovies().observe(this, new Observer<SearchMovieResult>() {
            @Override
            public void onChanged(SearchMovieResult searchMovieResult) {
                Log.i("**searchResult**", String.valueOf(searchMovieResult.getResults().size()));
                if (searchMovieResult.getResults() != null && !searchMovieResult.getResults().isEmpty()) {
                    ListView searchlistView = findViewById(R.id.searchResultList);
                    movieResultsList = searchMovieResult.getResults();
                    SearchListAdaptor searchListAdaptor = new SearchListAdaptor(getApplicationContext(), movieResultsList);
                    searchlistView.setAdapter(searchListAdaptor);
                    loadSearchFragment();
                } else {
                    //display no result message
                }
            }
        });
    }

    public void loadSearchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.searchFragmentSpace, new SearchResultFragment());
        fragmentTransaction.commit();
        showMainContent(false);
    }

    private void showMainContent(boolean showMainContent) {
        ScrollView scrollViewContent = findViewById(R.id.scrollViewContent);
        scrollViewContent.setVisibility(showMainContent ? ScrollView.VISIBLE : ScrollView.GONE);
        View listFragment = findViewById(R.id.searchFragmentSpace);
        listFragment.setVisibility(showMainContent ? View.GONE : View.VISIBLE);
    }
}