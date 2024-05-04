package com.example.ttkncinema.activity.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ttkncinema.R;

import com.example.ttkncinema.activity.adapter.ViewPagerAdapter;
import com.example.ttkncinema.activity.model.Movie;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;

public class DateActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    Toolbar toolbar;
    String date1;
    String date2;
    String date3;
    String date4;
    String date5;
    String date1_time1;
    String date2_time1;
    String date3_time1;
    String date4_time1;
    String date5_time1;
    String date1_time2;
    String date2_time2;
    String date3_time2;
    String date4_time2;
    String date5_time2;
    String image;
    String title;
    String content;
    String censorship;
    String director;
    String time;
    String id;
    String room1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbardate);
        setSupportActionBar(toolbar);
        // Hiển thị nút back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                image = bundle.getString("movieImage");
                title = bundle.getString("movieTitle");
                content = bundle.getString("movieContent");
                censorship= bundle.getString("movieCensorship");
                date1 = bundle.getString("movieDate1");
                director = bundle.getString("movieDirector");
                time = bundle.getString("movieTime");
                date1_time1 = bundle.getString("movieDate1_time1");
                date1_time2 = bundle.getString("movieDate1_time2");
                date2 = bundle.getString("movieDate2");
                date3 = bundle.getString("movieDate3");
                date4 = bundle.getString("movieDate4");
                date5 = bundle.getString("movieDate5");
                date2_time1 = bundle.getString("movieDate2_time1");
                date3_time1 = bundle.getString("movieDate3_time1");
                date4_time1 = bundle.getString("movieDate4_time1");
                date5_time1 = bundle.getString("movieDate5_time1");
                id=bundle.getString("id");
                room1=bundle.getString("room1");
            }
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, Arrays.asList(date1, date2, date3, date4, date5), Arrays.asList(date1_time1, date2_time1, date3_time1, date4_time1, date5_time1),
                Arrays.asList(date1_time2, date2_time2, date3_time2, date4_time2, date5_time2), title, image,content,censorship,director, time,id,room1);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(date1);
                        break;
                    case 1:
                        tab.setText(date2);
                        break;
                    case 2:
                        tab.setText(date3);
                        break;
                    case 3:
                        tab.setText(date4);
                        break;
                    case 4:
                        tab.setText(date5);
                        break;
                }
            }
        }).attach();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}