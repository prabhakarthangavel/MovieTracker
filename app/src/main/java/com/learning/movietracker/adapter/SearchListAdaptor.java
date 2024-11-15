package com.learning.movietracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.learning.movietracker.R;
import com.learning.movietracker.databinding.SearchResultFragmentBinding;
import com.learning.movietracker.model.searchmovies.MovieResults;

import java.util.List;

public class SearchListAdaptor extends BaseAdapter {

    private Context context;
    private List<MovieResults> movieResultsList;

    public SearchListAdaptor(Context context, List<MovieResults> movieResultsList) {
        this.context = context;
        this.movieResultsList = movieResultsList;
    }

    @Override
    public int getCount() {
        return movieResultsList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieResultsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return movieResultsList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultFragmentBinding searchResultBinding;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            searchResultBinding = DataBindingUtil.inflate(inflater, R.layout.search_result_fragment, parent, false);
            convertView = searchResultBinding.getRoot();
            convertView.setTag(searchResultBinding);
        } else {
            searchResultBinding = (SearchResultFragmentBinding) convertView.getTag();
        }

        MovieResults movieResult = movieResultsList.get(position);
        searchResultBinding.setMovie(movieResult);
        searchResultBinding.executePendingBindings();
        return convertView;
    }
}
