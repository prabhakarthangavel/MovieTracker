package com.learning.movietracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.learning.movietracker.adapter.SearchListAdaptor;
import com.learning.movietracker.adapter.WatchlistAdapter;
import com.learning.movietracker.databinding.AddReviewBinding;
import com.learning.movietracker.model.Movie;
import com.learning.movietracker.model.addreview.AddReview;
import com.learning.movietracker.model.common.Toolbar;
import com.learning.movietracker.model.searchmovies.MovieResults;
import com.learning.movietracker.model.searchmovies.SearchMovieResult;
import com.learning.movietracker.utl.CustomLinearLayout;
import com.learning.movietracker.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddReviewActivity extends AppCompatActivity {

    private AddReviewBinding addReviewBinding;
    private MainActivityViewModel viewModel;
    private List<MovieResults> movieResultsList;
    private final AddReview userReview = new AddReview();
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addReviewBinding = DataBindingUtil.setContentView(this, R.layout.add_review);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        addReviewBinding.setViewModel(viewModel);
        addReviewBinding.setLifecycleOwner(this);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isFromWatchlist", false)) {
            MovieResults movieResult = new MovieResults();
            movieResult.setId(intent.getIntExtra("movieId", 0));
            movieResult.setTitle(intent.getStringExtra("movieTitle"));
            movieResult.setPosterPath(intent.getStringExtra("moviePosterPath"));
            movieResult.setReleaseDate(intent.getStringExtra("movieReleaseDate"));
            movieResult.setOverview(intent.getStringExtra("movieOverview"));
            showAddReviewPage(movieResult);
        } else {
            Toolbar toolbar = new Toolbar(getString(R.string.addReview), false, View.GONE, 0);
            addReviewBinding.setToolbar(toolbar);

            initialDisplay();


            addReviewBinding.searchbox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (charSequence.length() > 0) {
                        Drawable rightDrwable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icons8_clear_20);
                        addReviewBinding.searchbox.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrwable, null);
                        viewModel.searchBarKeyTyped(charSequence.toString());
                        getSearchedMoviesResult();
                    } else {
                        Drawable leftDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icons8_search_20);
                        addReviewBinding.searchbox.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
                        initialDisplay();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            addReviewBinding.searchbox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        Drawable rightDrwable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icons8_clear_20);
                        if (motionEvent.getRawX() >= (addReviewBinding.searchbox.getRight() - rightDrwable.getBounds().width() - addReviewBinding.searchbox.getCompoundPaddingRight())) {
                            addReviewBinding.searchbox.getText().clear();
                            return true;
                        }
                    }
                    return false;
                }
            });

            addReviewBinding.searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MovieResults movieResult = movieResultsList.get(position);
                    showAddReviewPage(movieResult);
                }
            });
        }
        addReviewBinding.headerSection.toolbarBacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addReviewBinding.firstTimeWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addReviewBinding.firstTimeWatchText.getText().toString().equals(getString(R.string.firstTimeWatched))) {
                    addReviewBinding.eyeImage.setImageResource(R.drawable.icons8_eye_100_watched);
                    addReviewBinding.firstTimeWatchText.setText(R.string.seenBefore);
                } else {
                    addReviewBinding.eyeImage.setImageResource(R.drawable.icons8_eye_100);
                    addReviewBinding.firstTimeWatchText.setText(R.string.firstTimeWatched);
                }
            }
        });

        addReviewBinding.likeMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addReviewBinding.movieLikeText.getText().toString().equals(getString(R.string.like))) {
                    addReviewBinding.movieLiked.setImageResource(R.drawable.icons8_heart_100_filled);
                    addReviewBinding.movieLikeText.setText(R.string.liked);
                } else {
                    addReviewBinding.movieLiked.setImageResource(R.drawable.icons8_heart_100);
                    addReviewBinding.movieLikeText.setText(R.string.like);
                }
            }
        });

        addReviewBinding.savePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference("tbl_saved_reviews");
                userReview.setReview(addReviewBinding.reviewEditText.getText().toString());
                userReview.setTags(addReviewBinding.reviewTags.getText().toString());
                userReview.setFirstTimeWatched(addReviewBinding.firstTimeWatchText.getText().equals(getString(R.string.firstTimeWatched)));
                userReview.setLiked(addReviewBinding.movieLikeText.getText().equals(getString(R.string.liked)));
                userReview.setStars(CustomLinearLayout.starsCount);
                userReview.setDate(addReviewBinding.selectDate.getText().toString());
                final String[] key = new String[1];

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            AddReview addReview = snapshot1.getValue(AddReview.class);
                            if (addReview != null && Objects.equals(addReview.getId(), userReview.getId())) {
                                key[0] = snapshot1.getKey();
                                break;
                            }
                        }

                        if (key[0] != null) {
                            dbRef.child(key[0]).setValue(userReview);
                        } else {
                            dbRef.push().setValue(userReview);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        addReviewBinding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePostFromSavedPost(userReview.getId());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference("tbl_posts");
                userReview.setReview(addReviewBinding.reviewEditText.getText().toString());
                userReview.setTags(addReviewBinding.reviewTags.getText().toString());
                userReview.setFirstTimeWatched(addReviewBinding.firstTimeWatchText.getText().equals(getString(R.string.firstTimeWatched)));
                userReview.setLiked(addReviewBinding.movieLikeText.getText().equals(getString(R.string.liked)));
                userReview.setStars(CustomLinearLayout.starsCount);
                userReview.setDate(addReviewBinding.selectDate.getText().toString());
                final String[] key = new String[1];

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            AddReview addReview = snapshot1.getValue(AddReview.class);
                            if (addReview != null && Objects.equals(addReview.getId(), userReview.getId())) {
                                key[0] = snapshot1.getKey();
                                break;
                            }
                        }

                        if (key[0] != null) {
                            //updatong the existing post
                            dbRef.child(key[0]).setValue(userReview);
                        } else {
                            // creating new post
                            dbRef.push().setValue(userReview);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });
    }

    private void showAddReviewPage(MovieResults movieResult) {
        Toolbar toolbar = new Toolbar(getString(R.string.iWatched), false, View.GONE, 0);
        addReviewBinding.setToolbar(toolbar);
        switchSearchAndReview();
        setValuesToReviewSection(movieResult);
        updateValuesForViews(movieResult.getId());
    }

    private void updateValuesForViews(final Integer movieId) {
        final AddReview[] addReview = {new AddReview()};
        addReview[0].setDate(getDateString());
        viewModel.setaddReview(addReview[0]);
        String[] tables = {"tbl_saved_reviews", "tbl_posts"};
        for (String tableName : tables) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference(tableName);
            final String[] key = new String[1];

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        addReview[0] = snapshot1.getValue(AddReview.class);
                        if (addReview[0] != null && Objects.equals(addReview[0].getId(), movieId)) {
                            key[0] = snapshot1.getKey();
                            viewModel.setaddReview(addReview[0]);
                            if (tableName.equalsIgnoreCase("tbl_posts")) {
                                addReviewBinding.savePostBtn.setVisibility(View.GONE);
                                addReviewBinding.postBtn.setVisibility(View.GONE);
                                addReviewBinding.setToolbar(new Toolbar(getString(R.string.youAdded), false, View.VISIBLE, R.drawable.baseline_delete_24_white));
                            }
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void removePostFromSavedPost(int id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("tbl_saved_reviews");
        final String[] key = new String[1];

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    AddReview addReview = snapshot1.getValue(AddReview.class);
                    if (addReview != null && Objects.equals(addReview.getId(), id)) {
                        key[0] = snapshot1.getKey();
                        dbRef.child(key[0]).removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setValuesToReviewSection(MovieResults movieResult) {
        SpannableString spannableString = new SpannableString(movieResult.getTitle() + " " + movieResult.getReleaseDate().substring(0, 4));
        int spanLength = spannableString.toString().length();
        spannableString.setSpan(new RelativeSizeSpan(0.6f), spanLength - 4, spanLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        addReviewBinding.movieName.setText(spannableString);
        userReview.setTitle(movieResult.getTitle());
        userReview.setOverview(movieResult.getOverview());
        userReview.setId(movieResult.getId());

        String image = "https://image.tmdb.org/t/p/w500/" + movieResult.getPosterPath();
        Glide.with(this).load(image).into(addReviewBinding.movieImage);
        userReview.setPosterPath(movieResult.getPosterPath());

        addReviewBinding.selectDate.setText(getDateString());

        addReviewBinding.selectDate.setOnClickListener(v -> showDatePickerDialog(addReviewBinding.selectDate, months));

        addReviewBinding.headerSection.toolbarRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference("tbl_posts");
                Query query = dbRef.orderByChild("id").equalTo(movieResult.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Review deleted successfully!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public String getDateString() {
        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.DAY_OF_MONTH) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        return date;
    }

    private void showDatePickerDialog(TextView dateTextView, String[] months) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            dateTextView.setText(selectedDay + " " + months[selectedMonth] + " " + selectedYear);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void getSearchedMoviesResult() {
        viewModel.getSearchedMovies().observe(this, new Observer<SearchMovieResult>() {
            @Override
            public void onChanged(SearchMovieResult searchMovieResult) {
                if (searchMovieResult.getResults() != null && !searchMovieResult.getResults().isEmpty()) {
                    ListView searchlistView = addReviewBinding.searchResultList;
                    movieResultsList = searchMovieResult.getResults();
                    SearchListAdaptor searchListAdaptor = new SearchListAdaptor(getApplicationContext(), movieResultsList);
                    searchlistView.setAdapter(searchListAdaptor);
                    addReviewBinding.searchResultArea.setVisibility(View.VISIBLE);
                } else {
                    //display no result message
                }
            }
        });
    }

    private void switchSearchAndReview() {
        addReviewBinding.movieSearchSpace.setVisibility(View.GONE);
        addReviewBinding.movieReviewSpace.setVisibility(View.VISIBLE);
        addReviewBinding.buttonsSections.setVisibility(View.VISIBLE);
    }

    private void initialDisplay() {
        addReviewBinding.searchResultArea.setVisibility(View.GONE);
        addReviewBinding.movieReviewSpace.setVisibility(View.GONE);
        addReviewBinding.buttonsSections.setVisibility(View.GONE);
    }

//    public void loadSearchFragment() {
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.searchFragmentSpace, new SearchResultFragment());
//        fragmentTransaction.commit();
//    }
}
