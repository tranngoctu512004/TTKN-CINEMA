package com.example.ttkncinema.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.adapter.MovieAdapter;
import com.example.ttkncinema.activity.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class LichPhimDate_Fragment extends Fragment {

    private static final String ARG_MOVIES = "arg_movies";

    public static LichPhimDate_Fragment newInstance(List<Movie> movies) {
        LichPhimDate_Fragment fragment = new LichPhimDate_Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIES, new ArrayList<>(movies));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_phim_date, container, false);

        if (getArguments() != null) {
            List<Movie> movies = (List<Movie>) getArguments().getSerializable(ARG_MOVIES);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMovies);
            MovieAdapter movieAdapter = new MovieAdapter(movies);
            recyclerView.setAdapter(movieAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return view;


    }
}
