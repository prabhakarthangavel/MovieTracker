package com.learning.movietracker.serviceAPI;

import com.learning.movietracker.model.Result;
import com.learning.movietracker.model.moviedetails.CastAndCrew;
import com.learning.movietracker.model.moviedetails.MovieDetails;
import com.learning.movietracker.model.searchmovies.SearchMovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    // The service interface defines the structure
    // and behavior of the API requests.
    // Acts as a bridge between your app and the API

    // Call<ResponseType>: Represents a network request
    // and its response. 'ResponseType' should be replaced
    // with the actual data model class that represents
    // the expected response from the API.

    @GET("movie/{movieCategory}")
    Call<Result> getPopularMovies(@Path("movieCategory") String movieCategory, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<CastAndCrew> getMovieCastAndCrew(@Path("movie_id") String movidId, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<SearchMovieResult> getSearchedMoviesResults(@Query("query") String searchedString, @Query("api_key") String apiKey);
}
