package com.learning.movietracker.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.learning.movietracker.R;
import com.learning.movietracker.model.Movie;
import com.learning.movietracker.model.Result;
import com.learning.movietracker.model.moviedetails.CastAndCrew;
import com.learning.movietracker.model.moviedetails.MovieDetails;
import com.learning.movietracker.model.searchmovies.SearchMovieResult;
import com.learning.movietracker.serviceAPI.MovieApiService;
import com.learning.movietracker.serviceAPI.MovieDBInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDBRepo {
    // used to abstract the data source details and
    // provides a clean API for the ViewModel to
    // fetch and manage data
    private Application application;

    public MovieDBRepo(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Movie>> getMutableLiveData(String moviesCategory) {

        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<List<Movie>>();
        MovieApiService movieService = MovieDBInstance.getService();

        Call<Result> resultCall = movieService.getPopularMovies(moviesCategory, application.getApplicationContext().getString(R.string.api_key));
        Log.i("**moviesCateogory", moviesCategory);
        // perform network request in the background thread and
        // handle the response on the main (UI) thread
        resultCall.enqueue(new Callback<Result>() {
            ArrayList<Movie> movies = new ArrayList<Movie>();
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.i("**response", response.body().toString());
                Result result = response.body();

                if (result != null && result.getResults() != null){
                    movies = (ArrayList<Movie>) result.getResults();
                    mutableLiveData.setValue(movies);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {}
        });

        return mutableLiveData;
    }


    public MutableLiveData<MovieDetails> getMovieDetails(String movieId) {
        MutableLiveData<MovieDetails> mutableLiveData = new MutableLiveData<MovieDetails>();
        MovieApiService movieService = MovieDBInstance.getService();

        Call<MovieDetails> movieDetailsCall = movieService.getMovieDetails(movieId, application.getApplicationContext().getString(R.string.api_key));
        movieDetailsCall.enqueue(new Callback<MovieDetails>() {

            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails movieDetails = response.body();
                if (movieDetails != null){
                    mutableLiveData.setValue(movieDetails);
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {}
        });

        return mutableLiveData;
    }

    public MutableLiveData<CastAndCrew> getMovieCastndCrew(String movieId) {
        MutableLiveData<CastAndCrew> mutableLiveData = new MutableLiveData<CastAndCrew>();
        MovieApiService movieService = MovieDBInstance.getService();

        Call<CastAndCrew> castAndCrewCall = movieService.getMovieCastAndCrew(movieId, application.getApplicationContext().getString(R.string.api_key));
        castAndCrewCall.enqueue(new Callback<CastAndCrew>() {

            @Override
            public void onResponse(Call<CastAndCrew> call, Response<CastAndCrew> response) {
                CastAndCrew castAndCrew = response.body();
                if (castAndCrew != null){
                    mutableLiveData.setValue(castAndCrew);
                }
            }

            @Override
            public void onFailure(Call<CastAndCrew> call, Throwable t) {}
        });

        return mutableLiveData;
    }

    public MutableLiveData<SearchMovieResult> getSearchedMovies(String searachedString) {
        MutableLiveData<SearchMovieResult> resultMutableLiveData = new MutableLiveData<>();
        MovieApiService movieApiService = MovieDBInstance.getService();

        Call<SearchMovieResult> moviesResultCall = movieApiService.getSearchedMoviesResults(searachedString, application.getApplicationContext().getString(R.string.api_key));
        moviesResultCall.enqueue(new Callback<SearchMovieResult>() {
            @Override
            public void onResponse(Call<SearchMovieResult> call, Response<SearchMovieResult> response) {
                SearchMovieResult apiResult = response.body();
                if (apiResult != null) {
                    resultMutableLiveData.setValue(apiResult);
                }
            }

            @Override
            public void onFailure(Call<SearchMovieResult> call, Throwable t) {}
        });
        return resultMutableLiveData;
    }
}
