package com.example.ttkncinema.activity.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ttkncinema.activity.fragment.LichPhimDate_Fragment;
import com.example.ttkncinema.activity.model.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LichPhimAdapter extends FragmentStateAdapter {

    private List<Movie> listCinemaData;

    public LichPhimAdapter(FragmentActivity fragmentActivity, List<Movie> listCinemaData) {
        super(fragmentActivity);
        this.listCinemaData = listCinemaData;
    }

    @Override
    public int getItemCount() {
        // Số lượng fragment nên bằng số lượng ngày duy nhất trong danh sách của bạn
        return getDistinctDates().size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String date = getDistinctDates().get(position);
        List<Movie> filteredMovies = filterMoviesByDate(date);
        return LichPhimDate_Fragment.newInstance(filteredMovies);
    }

    public List<String> getDistinctDates() {
        List<String> distinctDates = new ArrayList<>();
        Set<String> uniqueDatesSet = new HashSet<>();
        for (Movie movie : listCinemaData) {
            // Lấy danh sách các ngày từ mỗi đối tượng Movie
            List<String> movieDates = movie.getDates();

            for (String date : movieDates) {
                if (!uniqueDatesSet.contains(date)) {
                    distinctDates.add(date);
                    uniqueDatesSet.add(date);
                }
            }
        }

        return distinctDates;
    }


    private List<Movie> filterMoviesByDate(String date) {
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : listCinemaData) {
            // Lấy danh sách các ngày từ mỗi đối tượng Movie
            List<String> movieDates = movie.getDates();

            // Kiểm tra xem ngày cần tìm có trong danh sách ngày của Movie hay không
            if (movieDates.contains(date)) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }
}
