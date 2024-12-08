package com.learning.movietracker;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.learning.movietracker.databinding.MoviesDetailsBinding;
import com.learning.movietracker.model.moviedetails.CastAndCrew;
import com.learning.movietracker.model.moviedetails.Crew;
import com.learning.movietracker.model.moviedetails.Genre;
import com.learning.movietracker.model.moviedetails.MovieDetails;
import com.learning.movietracker.viewmodel.MainActivityViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class MovieDetailsActivity extends AppCompatActivity {
    private MoviesDetailsBinding movieDetailsBinding;
    private MainActivityViewModel viewModel;
    private MovieDetails movieDetailsModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_details);

        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.movies_details);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra("movieId");

        fetchMovieDetails(movieId, this);
        fetchMovieCastDetails(movieId, this);
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    public void fetchMovieDetails(String moviId, Context context) {
        viewModel.getMovieDetail(moviId).observe(this, new Observer<com.learning.movietracker.model.moviedetails.MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                movieDetailsModel = movieDetails;
                movieDetailsBinding.setMovieDetailsModel(movieDetailsModel);

                //dominant color to change the back button color
                ImageView movieBanner = findViewById(R.id.movieBanner);
                String banerimageURL = "https://image.tmdb.org/t/p/w500/" + movieDetailsModel.getTagline();

                Picasso.get().load(banerimageURL).into(movieBanner, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) movieBanner.getDrawable()).getBitmap();

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                if (palette != null) {
                                    int dominantColor = palette.getDominantColor(0);
                                    boolean isDark = isColorDark(dominantColor);
                                    if (movieDetailsBinding.backButton != null) {
                                        movieDetailsBinding.backButton.setImageResource(isDark ? R.drawable.baseline_arrow_back_24_background : R.drawable.baseline_arrow_back_24_black);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                //page back button
                ImageView backButton = findViewById(R.id.backButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                //movie year and runtime
                String movieSubInfo = "";
                if (movieDetails.getReleaseDate() != null) {
                    String[] year = movieDetails.getReleaseDate().split("-");
                    movieSubInfo += year[0];
                }
                int runtime = movieDetails.getRuntime();
                if (runtime > 0) {
                    int hours = runtime / 60;
                    int remainingMinutes = runtime % 60;
                    movieSubInfo += !movieSubInfo.isEmpty() ? "  " : "";
                    movieSubInfo += hours + "h " + remainingMinutes + "m";
                }
                movieDetailsBinding.movieSubInfo.setText(movieSubInfo);

                //movie genre
                ChipGroup chipGroup = findViewById(R.id.chipGroup);
                List<Genre> genreList = movieDetails.getGenres();
                for (int i = 0; i < genreList.size(); i++) {
                    Chip chip = new Chip(context);
                    chip.setText(genreList.get(i).getName());
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background)));
                    chipGroup.addView(chip);
                }

                //movie votecount
                int voteCount = movieDetails.getVoteCount();
                String votesInThousands = voteCount != 0 ? String.valueOf(voteCount) : "";
                if (voteCount >= 10000) {
                    votesInThousands = votesInThousands.substring(0, 2) + "K";
                } else if (voteCount >= 1000) {
                    votesInThousands = votesInThousands.charAt(0) + "." + votesInThousands.charAt(1) + "K";
                }
                if (!votesInThousands.isEmpty())
                    movieDetailsBinding.voteCount.setText(votesInThousands);
            }
        });
    }


    private void fetchMovieCastDetails(String movieId, MovieDetailsActivity movieDetailsActivity) {
        viewModel.getCastAndCrew(movieId).observe(this, new Observer<CastAndCrew>() {
            @Override
            public void onChanged(CastAndCrew castAndCrew) {
                String[] viewList = {"director", "acting"};
                for (int j = 0; j < viewList.length; j++) {
                    int viewId = getResources().getIdentifier(viewList[j], "id", getPackageName());
                    TextView displayViewText = findViewById(viewId);
                    StringBuilder displayName = new StringBuilder(Character.toUpperCase(viewList[j].charAt(0)) + viewList[j].substring(1) + "  ");
                    List<Crew> crews = castAndCrew.getCrew();
                    for (int i = 0; i < crews.size(); i++) {
                        if (j == 0) {
                            if (crews.get(i).getJob().equalsIgnoreCase(viewList[j])) {
                                if (displayName.length() > viewList[j].length() + 1)
                                    displayName.append(" · ");
                                displayName.append(crews.get(i).getName());
                            }
                        }
                        if (j == 1) {
                            if (crews.get(i).getKnownForDepartment().equalsIgnoreCase(viewList[j])) {
                                if (displayName.length() > viewList[j].length() + 1)
                                    displayName.append(" · ");
                                displayName.append(crews.get(i).getName());
                            }
                        }
                    }
                    SpannableString spannableString = new SpannableString(displayName);
                    StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
                    spannableString.setSpan(boldspan, 0, viewList[j].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    displayViewText.setText(spannableString);
                }
            }
        });
    }
}
