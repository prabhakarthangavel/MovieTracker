package com.learning.movietracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.movietracker.adapter.MyCustomAdapter;
import com.learning.movietracker.adapter.SearchListAdaptor;
import com.learning.movietracker.databinding.ActivityMainBinding;
import com.learning.movietracker.databinding.HomeFragmentBinding;
import com.learning.movietracker.model.Movie;
import com.learning.movietracker.model.searchmovies.MovieResults;
import com.learning.movietracker.model.searchmovies.SearchMovieResult;
import com.learning.movietracker.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private MainActivityViewModel viewModel;
    private HomeFragmentBinding binding;
    private List<MovieResults> movieResultsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstaceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView popularMoviesTitle = binding.poupularMoviesTitle;
        popularMoviesTitle.setText(R.string.popularMovies);
        TextView nowShowingTitle =  binding.nowShowingMoviesTitle;
        nowShowingTitle.setText(R.string.nowShowing);
        TextView topRated = binding.topRatedMoviesTitle;
        topRated.setText(R.string.topRated);
        TextView upcoing =  binding.upcomingRatedMoviesTitle;
        upcoing.setText(R.string.upcoming);

        String[] moviesTypes = {"now_playing", "popular", "top_rated", "upcoming"};
        for (String moviesType : moviesTypes) {
            switch (moviesType) {
                case "now_playing":
                    getMoviesByType(moviesType, binding.nowShowingrecyclerview, true);
                    break;
                case "popular":
                    getMoviesByType(moviesType, binding.recyclerview, false);
                    break;
                case "top_rated":
                    getMoviesByType(moviesType, binding.topRatedRecyclerview, false);
                    break;
                case "upcoming":
                    getMoviesByType(moviesType, binding.upcomingMoviesRecyclerview, true);
                    break;
            }
        }

        binding.searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    Drawable rightDrwable = ContextCompat.getDrawable(getContext(), R.drawable.icons8_clear_20);
                    binding.searchbox.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrwable, null);
                    viewModel.searchBarKeyTyped(charSequence.toString());
                    getSearchedMoviesResult(binding);
                } else {
                    Drawable leftDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icons8_search_20);
                    binding.searchbox.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
                    showMainContent(true, binding);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.searchbox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Drawable rightDrwable = ContextCompat.getDrawable(getContext(), R.drawable.icons8_clear_20);
                    if (motionEvent.getRawX() >= (binding.searchbox.getRight() - rightDrwable.getBounds().width() - binding.searchbox.getCompoundPaddingRight())) {
                        binding.searchbox.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        binding.searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MovieResults movieResult = movieResultsList.get(position);
                Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                movieDetailsIntent.putExtra("movieId", movieResult.getId().toString());
                startActivity(movieDetailsIntent);
            }
        });
    }


    private void getMoviesByType(String moviesType, RecyclerView recyclerView, boolean doLocallySort) {
        viewModel.getAllMovies(moviesType).observe(getActivity(), new Observer<List<Movie>>() {
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
                    } else {
                        movies.addAll(moviesFromLiveData);
                        displayMoviesInRecyclerView(movies, recyclerView);
                    }
                }
            }
        });
    }

    private void displayMoviesInRecyclerView(ArrayList<Movie> moviesList, RecyclerView recyclerView) {
        MyCustomAdapter adapter = new MyCustomAdapter(getContext(), moviesList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void getSearchedMoviesResult(HomeFragmentBinding binding) {
        viewModel.getSearchedMovies().observe(this, new Observer<SearchMovieResult>() {
            @Override
            public void onChanged(SearchMovieResult searchMovieResult) {
                Log.i("**searchResult**", String.valueOf(searchMovieResult.getResults().size()));
                if (searchMovieResult.getResults() != null && !searchMovieResult.getResults().isEmpty()) {
                    ListView searchlistView = binding.searchResultList;
                    movieResultsList = searchMovieResult.getResults();
                    SearchListAdaptor searchListAdaptor = new SearchListAdaptor(getContext(), movieResultsList);
                    searchlistView.setAdapter(searchListAdaptor);
                    loadSearchFragment(binding);
                } else {
                    //display no result message
                }
            }
        });
    }

    public void loadSearchFragment(HomeFragmentBinding binding) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.searchFragmentSpace, new SearchResultFragment());
        fragmentTransaction.commit();
        showMainContent(false, binding);
    }

    private void showMainContent(boolean showMainContent, HomeFragmentBinding binding) {
        ScrollView scrollViewContent = binding.scrollViewContent;
        scrollViewContent.setVisibility(showMainContent ? ScrollView.VISIBLE : ScrollView.GONE);
        View listFragment = binding.searchFragmentSpace;
        listFragment.setVisibility(showMainContent ? View.GONE : View.VISIBLE);
    }
}
