package com.example.ttkncinema.activity.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ttkncinema.activity.fragment.MovieFragment;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<String> dates;
    private List<String> times1;
    private List<String> times2;
    private   String title;
    private   String image;
    private String content;
    private String censorship;
    private String director;
    private  String time;
    private String id;
    private String room1;
    public ViewPagerAdapter(FragmentActivity fragmentActivity, List<String> dates, List<String> times1, List<String> times2,
                            String title, String image, String content,String censorship, String director, String time, String id, String room1) {
        super(fragmentActivity);
        this.dates = dates;
        this.times1 = times1;
        this.times2 = times2;
        this.title=title;
        this.image=image;
        this.content=content;
        this.censorship=censorship;
        this.director=director;
        this.time=time;
        this.id = id;
        this.room1=room1;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a new instance of MovieFragment with data for each tab
        return MovieFragment.newInstance(dates.get(position), times1.get(position), times2.get(position),title, image,content,censorship,director,time,id,room1);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}