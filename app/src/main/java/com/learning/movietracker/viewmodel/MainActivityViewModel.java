package com.learning.movietracker.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.learning.movietracker.model.Movie;
import com.learning.movietracker.model.addreview.AddReview;
import com.learning.movietracker.model.moviedetails.CastAndCrew;
import com.learning.movietracker.model.moviedetails.MovieDetails;
import com.learning.movietracker.model.searchmovies.SearchMovieResult;
import com.learning.movietracker.repository.MovieDBRepo;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    // ViewModel: suitable for non-Android-specific logic
    // AndroidViewModel: used when viewModel class needs to
    //                   access Android-specific components
    public ObservableField<AddReview> $addReview = new ObservableField<>();
    private MovieDBRepo movieDBRepo;
    private String searchedKeyword;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.movieDBRepo = new MovieDBRepo(application);
    }

    //Live Data
    public LiveData<List<Movie>> getAllMovies(String movieCategory) {
        return movieDBRepo.getMutableLiveData(movieCategory);
    }

    public MutableLiveData<MovieDetails> getMovieDetail(String movieId) { return movieDBRepo.getMovieDetails(movieId);}

    public MutableLiveData<CastAndCrew> getCastAndCrew(String movId) { return movieDBRepo.getMovieCastndCrew(movId);}

    public void searchBarKeyTyped(String searchText) {
        searchedKeyword = searchText;
    }

    public MutableLiveData<SearchMovieResult> getSearchedMovies() {
        return movieDBRepo.getSearchedMovies(searchedKeyword);
    }

    public ObservableField<AddReview> getaddReview() {
        return $addReview;
    }

    public void setaddReview(AddReview addReview) {
        $addReview.set(addReview);
    }
}
