package com.learning.movietracker.serviceAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
            // Create a logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            // Create an OkHttpClient with the interceptor
            OkHttpClient client = new OkHttpClient.Builder() .addInterceptor(loggingInterceptor).build();

            movieRetro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return movieRetro.create(MovieApiService.class);
    }

}
