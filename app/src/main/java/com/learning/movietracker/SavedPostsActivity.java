package com.learning.movietracker;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.transition.Visibility;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.movietracker.adapter.SearchListAdaptor;
import com.learning.movietracker.databinding.SavedPostsBinding;
import com.learning.movietracker.model.addreview.AddReview;
import com.learning.movietracker.model.common.Toolbar;
import com.learning.movietracker.model.searchmovies.MovieResults;
import com.learning.movietracker.utl.CustomLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedPostsActivity extends AppCompatActivity {

    private SavedPostsBinding savedPostsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_posts);

        savedPostsBinding = DataBindingUtil.setContentView(this, R.layout.saved_posts);
        savedPostsBinding.setLifecycleOwner(this);

        Toolbar toolbar = new Toolbar("Saved Posts", true, View.GONE);
        savedPostsBinding.setToolbar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("tbl_saved_reviews");
        List<MovieResults> movieResultsList = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    AddReview addReview = snapshot1.getValue(AddReview.class);
                    MovieResults movieResults = new MovieResults();
                    movieResults.setId(addReview.getId());
                    movieResults.setTitle(addReview.getTitle());
                    movieResults.setPosterPath(addReview.getPosterPath());
                    movieResults.setReleaseDate(addReview.getReleaseDate());
                    movieResultsList.add(movieResults);
                }
                ListView savedPostList = savedPostsBinding.savedPostList;
                SearchListAdaptor searchListAdaptor = new SearchListAdaptor(getApplicationContext(), movieResultsList);
                savedPostList.setAdapter(searchListAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
