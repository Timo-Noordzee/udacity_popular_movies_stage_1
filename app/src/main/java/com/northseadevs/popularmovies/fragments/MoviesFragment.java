package com.northseadevs.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.northseadevs.popularmovies.DetailsActivity;
import com.northseadevs.popularmovies.MainActivity;
import com.northseadevs.popularmovies.R;
import com.northseadevs.popularmovies.movie.Movie;
import com.northseadevs.popularmovies.networking.FetchMoviesTask;
import com.northseadevs.popularmovies.utils.MovieAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment implements FetchMoviesTask.FetchMoviesCallback, MovieAdapter.MovieClickHandler {

    public static final String SORT_TYPE_ARGS_KEY = "sort_type";

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar_movies)
    ProgressBar mProgressBar;

    private String mSortBy = FetchMoviesTask.MOST_POPULAR;
    private MovieAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null)
            mSortBy = args.getString(SORT_TYPE_ARGS_KEY, FetchMoviesTask.MOST_POPULAR);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setup the RecyclerView and Adapter
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_column_count)));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMovies(mSortBy);
    }

    //Fetch movies using the TheMovieDatabase API
    private void fetchMovies(String sortBy) {
        mProgressBar.setVisibility(View.VISIBLE);
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(sortBy, this);
        fetchMoviesTask.execute();
    }

    @Override
    public void onMoviesFetched(List<Movie> movies) {
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setMovieData(movies);
        Log.d(getClass().getSimpleName(), mSortBy + " Movie count: " + movies.size());
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(MainActivity.INTENT_MOVIE_KEY, movie);
        startActivity(intent);
    }

    public static MoviesFragment newInstance(String sortBy){
        MoviesFragment moviesFragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(SORT_TYPE_ARGS_KEY, sortBy);
        moviesFragment.setArguments(args);
        return moviesFragment;
    }
}