package com.example.ttkncinema.activity.fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.adapter.LichPhimAdapter;
import com.example.ttkncinema.activity.adapter.MovieAdapter;
import com.example.ttkncinema.activity.model.Movie;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LichPhim_Fragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private List<Movie> listCinemaData;
    private MovieAdapter movieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_phim_, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutlichphim);
        viewPager = view.findViewById(R.id.viewPagerlichphim);
        listCinemaData = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("cinema").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCinemaData.clear();
                for (DataSnapshot cinemaSnapshot : dataSnapshot.getChildren()) {
                    Movie movie = convertHashMapToMovie((Map<String, Object>) cinemaSnapshot.getValue());
                    if (movie != null) {
                        listCinemaData.add(movie);
                    }
                }
                LichPhimAdapter lichPhimAdapter = new LichPhimAdapter(getActivity(), listCinemaData);
                viewPager.setAdapter(lichPhimAdapter);

                // Sử dụng TabLayoutMediator mới để hiển thị tất cả các ngày
                new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if (listCinemaData.size() > 0) {
                            // Không cần lấy movie đầu tiên vì ta đã sử dụng adapter mới
                            // Lấy danh sách ngày duy nhất từ adapter
                            List<String> distinctDates = lichPhimAdapter.getDistinctDates();

                            if (position < distinctDates.size()) {
                                // Nếu vị trí position hợp lệ trong danh sách distinctDates, hiển thị ngày tương ứng
                                tab.setText(distinctDates.get(position));
                            }
                        } else {
                            Log.d("LichPhim_Fragment", "listCinemaData is empty");
                        }
                    }
                }).attach();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    private Movie convertHashMapToMovie(Map<String, Object> map) {
        String image = getStringFromObject(map.get("Image"));
        String title = getStringFromObject(map.get("Title"));
        String time = getStringFromObject(map.get("Time"));
        String content = getStringFromObject(map.get("Content"));
        String censorship = getStringFromObject(map.get("Censorship"));
        String director = getStringFromObject(map.get("Director"));
        String date1 = getStringFromObject(map.get("date1"));
        String date1_time1 = getStringFromObject(map.get("date1_time1"));
        String date1_time2 = getStringFromObject(map.get("date1_time2"));
        String date2 = getStringFromObject(map.get("date2"));
        String date2_time1 = getStringFromObject(map.get("date2_time1"));
        String date3 = getStringFromObject(map.get("date3"));
        String date3_time1 = getStringFromObject(map.get("date3_time1"));
        String date4 = getStringFromObject(map.get("date4"));
        String date4_time1 = getStringFromObject(map.get("date4_time1"));
        String date5 = getStringFromObject(map.get("date5"));
        String date5_time1 = getStringFromObject(map.get("date5_time1"));
        String id = getStringFromObject(map.get("ID"));
        String room1=getStringFromObject(map.get("room1"));
        Movie movie = new Movie(image, title,time, content,censorship,director,date1,date1_time1,date1_time2,date2,date2_time1,date3,date3_time1,date4,date4_time1,date5,date5_time1,id,room1);
        if (image != null && title != null && time != null && content != null && censorship != null && director != null && date1 != null
                && date1_time1 != null && date1_time2 != null && date2 != null && date3 != null && date4 != null && date5 != null
                && date2_time1 != null && date3_time1 != null && date4_time1 != null && date5_time1 != null) {
            return new Movie(image, title, time, content, censorship, director, date1, date1_time1,
                    date1_time2, date2, date2_time1, date3, date3_time1, date4, date4_time1, date5, date5_time1,id,room1);
        } else {
            return null;
        }

    }
    private String getStringFromObject(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Long) {
            return String.valueOf(value);
        } else {
            return null;
        }
    }



}

