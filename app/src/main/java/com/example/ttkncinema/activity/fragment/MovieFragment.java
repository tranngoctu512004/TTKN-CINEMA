package com.example.ttkncinema.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.Seat_Activity;

// MovieFragment.java
public class MovieFragment extends Fragment {
    private String date;
    private String time1;
    private String time2;
    private String image;
    private String title;
    private String content;
    private String censorship;
    private String director;
    private String id;
    private  String time;
    private String room1;
    public MovieFragment() {
    }

    public static MovieFragment newInstance(String date, String time1, String time2, String title, String image,String content,String censorship, String director, String time, String id, String room1) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putString("time1", time1);
        args.putString("time2", time2);
        args.putString("title", title);
        args.putString("image", image);
        args.putString("content", content);
        args.putString("censorship", censorship);
        args.putString("director", director);
        args.putString("time", time);
        args.putString("id", id);
        args.putString("room1",room1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        if (getArguments() != null) {
            date = getArguments().getString("date");
            time1 = getArguments().getString("time1");
            time2 = getArguments().getString("time2");
            title = getArguments().getString("title");
            image = getArguments().getString("image");
            content = getArguments().getString("content");
            censorship = getArguments().getString("censorship");
            director = getArguments().getString("director");
            time= getArguments().getString("time");
            room1=getArguments().getString("room1");
            id=getArguments().getString("id");

        }

        // Initialize UI components
        Button time1Button = view.findViewById(R.id.time1Button);
        Button time2Button = view.findViewById(R.id.time2Button);

        // Set text for time1Button
        time1Button.setText(time1);
        time1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Seat_Activity.class);
                intent.putExtra("date", date);
                intent.putExtra("time1", time1);// đây là giờ khách chọn
                intent.putExtra("image", image);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("censorship", censorship);
                intent.putExtra("director", director);
                intent.putExtra("time", time);// đây là thời lượng nha
                intent.putExtra("id", id);
                intent.putExtra("room1", room1);
                startActivity(intent);
            }
        });

        // Set text for time2Button if available, otherwise hide it
        if (time2 != null && !time2.isEmpty()) {
            time2Button.setText(time2);
            time2Button.setVisibility(View.VISIBLE);

            time2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            time2Button.setVisibility(View.GONE);
            time2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Seat_Activity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("time1", time2);// đây là giờ khách chọn
                    intent.putExtra("image", image);
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("censorship", censorship);
                    intent.putExtra("director", director);
                    intent.putExtra("time", time);// đây là thời lượng nha
                    intent.putExtra("id", id);
                    intent.putExtra("room1", room1);
                    startActivity(intent);
                }
            });
        }

        return view;
    }
}

