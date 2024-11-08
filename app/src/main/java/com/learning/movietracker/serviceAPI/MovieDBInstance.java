package com.learning.movietracker.serviceAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDBInstance {
    // Acts as a central configuration point for
    // defining how HTTP requests and responses
    // should be handled.

    private static Retrofit movieRetro = null;
    private static String baseURL = "https://api.themoviedb.org/3/";

    public static MovieApiService getService() {
        if (movieRetro == null) {
            movieRetro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return movieRetro.create(MovieApiService.class);
    }

}
