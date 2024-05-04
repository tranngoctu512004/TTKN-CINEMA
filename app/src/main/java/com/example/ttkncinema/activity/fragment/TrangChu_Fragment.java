package com.example.ttkncinema.activity.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.DateActivity;
import com.example.ttkncinema.activity.activity.Information_Activity;
import com.example.ttkncinema.activity.activity.Login_Activity;
import com.example.ttkncinema.activity.adapter.SliderAdapter;
import com.example.ttkncinema.activity.adapter.Upcomingadapter;
import com.example.ttkncinema.activity.model.Movie;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrangChu_Fragment extends Fragment {
    ViewPager2 viewPager2;
    List<Movie> listMovie;
    List<Movie> listUpcoming;
    Timer timer;
    int currentPage = 0;
    TextView tenphim, ngayphim, thoigian;
    SliderAdapter adapter;
    Button datvetrangchu;
    Movie movie;
    Upcomingadapter upcomingadapter;
    RecyclerView recyclerViewpsc;
    public TrangChu_Fragment() {
    }

    public static TrangChu_Fragment newInstance(String param1, String param2) {
        TrangChu_Fragment fragment = new TrangChu_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        viewPager2 = view.findViewById(R.id.viewpager2);
        recyclerViewpsc = view.findViewById(R.id.recyclerview_pxh);
        listMovie = new ArrayList<>();
        listUpcoming = new ArrayList<>();
        recyclerViewpsc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SliderAdapter(listMovie, getActivity());
        viewPager2.setAdapter(adapter);

        // Liên kết các thành phần giao diện
        tenphim = view.findViewById(R.id.tenphim);
        ngayphim = view.findViewById(R.id.tvngaychieu);
        thoigian = view.findViewById(R.id.tvthoigian);
        datvetrangchu=view.findViewById(R.id.btndatvetrangchu);
        // Lấy dữ liệu từ Firebase Realtime Database với key "slides"
        datvetrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently selected movie from the ViewPager
                int currentSlidePosition = viewPager2.getCurrentItem();
                Movie selectedMovie = listMovie.get(currentSlidePosition);

                // Create a bundle with movie data
                Bundle movieBundle = createMovieBundle(selectedMovie);

                // Create an intent to start the DateActivity
                Intent dateIntent = new Intent(getActivity(), DateActivity.class);

                // Put the movie bundle into the intent
                dateIntent.putExtras(movieBundle);

                // Start the DateActivity
                startActivity(dateIntent);
            }
        });
        FirebaseDatabase.getInstance().getReference("cinema").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot slideSnapshot : snapshot.getChildren()) {
                    Object slideData = slideSnapshot.getValue();
                    if (slideData instanceof Map) {
                        // Convert the HashMap to a Movie object or handle it accordingly
                        Movie movie = convertHashMapToMovie((Map<String, Object>) slideData);
                        listMovie.add(movie);
                    }
                }

                // Cập nhật dữ liệu trong Adapter và bắt đầu chạy auto-slider
                adapter.notifyDataSetChanged();
                startAutoSlider();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
        // Đăng ký một sự kiện để cập nhật tiêu đề và ngày phát hành khi thay đổi trang trong ViewPager2
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Đảm bảo rằng vị trí nằm trong giới hạn của dữ liệu
                if (position >= 0 && position < listMovie.size()) {
                    Movie movie = listMovie.get(position);
                    if (movie != null) {
                        updateTitleAndDate(movie.getTitle(), movie.getDate1(), movie.getTime());
                    }
                }
            }
        });

        // Lấy dữ liệu từ Firebase Realtime Database cho phim sắp chiếu
        FirebaseDatabase.getInstance().getReference("cinema").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUpcoming.clear();
                for (DataSnapshot slideSnapshot : dataSnapshot.getChildren()) {
                    Movie movie = convertHashMapToMovie((Map<String, Object>) slideSnapshot.getValue());
                    listUpcoming.add(movie);
                }
                // Dừng hiệu ứng shimmer và hiển thị RecyclerView
                ShimmerFrameLayout shimmerLayout = view.findViewById(R.id.shimmer_layout_pop);
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);

                List<Integer> originalList = new ArrayList<>();
                for (int i = 1; i <= listMovie.size(); i++) {
                    originalList.add(i);
                }
                List<Integer> shuffledList = shuffleList(originalList);
                Collections.shuffle(listMovie);
                // Tạo danh sách ngẫu nhiên dựa trên shuffledList
                List<Movie> randomMovieList = new ArrayList<>();
                for (Integer index : shuffledList) {
                    randomMovieList.add(listMovie.get(index - 1));
                }
                upcomingadapter = new Upcomingadapter(listUpcoming, new Upcomingadapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        if (isUserLoggedIn()) {
                            Intent intent = new Intent(getActivity(), Information_Activity.class);
                            Bundle bundle = createMovieBundle(movie);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
//                            startActivity(new Intent(getActivity(), Login_Activity.class));
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Đăng nhập");
                            builder.setMessage("Bạn cần đăng nhập để thực hiện thao tác này.");
                            builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Chuyển đến màn hình đăng nhập
                                    Intent loginIntent = new Intent(getActivity(), Login_Activity.class);
                                    // You may consider using startActivityForResult and checking the result
                                    startActivity(loginIntent);
                                }
                            });
                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                });

                recyclerViewpsc.setAdapter(upcomingadapter);
                recyclerViewpsc.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("getdata", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isremember", false);
    }


    // Phương thức để cập nhật tiêu đề và ngày phát hành
    public void updateTitleAndDate(String title, String date, String time) {
        tenphim.setText(title);
        ngayphim.setText(date);
        thoigian.setText(time);
    }

    // Phương thức để bắt đầu auto-slider
    private void startAutoSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == listMovie.size()) {
                            currentPage = 0;
                        }
                        viewPager2.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, 2000, 4000);
    }

    private List<Integer> shuffleList(List<Integer> originalList) {
        List<Integer> shuffledList = new ArrayList<>(originalList);
        Collections.shuffle(shuffledList);
        return shuffledList;
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
        String date4= getStringFromObject(map.get("date4"));
        String date4_time1 = getStringFromObject(map.get("date4_time1"));
        String date5 = getStringFromObject(map.get("date5"));
        String date5_time1 = getStringFromObject(map.get("date5_time1"));
        String id = getStringFromObject(map.get("ID"));
        String room1=getStringFromObject(map.get("room1"));
//        movie = new Movie(image, title,time, content,censorship,director,date1,date1_time1,date1_time2,date2,date2_time1,date3,date3_time1,date4,date4_time1,date5,date5_time1);
        if (image != null && title != null && time != null && content != null && censorship != null && director != null && date1 !=null
                && date1_time1!=null && date1_time2!=null && date2!=null && date3!=null && date4!=null && date5!=null
                && date2_time1!=null && date3_time1!=null && date4_time1!=null && date5_time1!=null  && id!=null && room1!=null) {
            return new Movie(image, title, time, content, censorship, director,date1,date1_time1,
                    date1_time2,date2,date2_time1,date3,date3_time1,date4,date4_time1,date5,date5_time1,id,room1);
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
    private Bundle createMovieBundle(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putString("movieImage", movie.getImage());
        bundle.putString("movieTitle", movie.getTitle());
        bundle.putString("movieTime", movie.getTime());
        bundle.putString("movieContent", movie.getContent());
        bundle.putString("movieCensorship", movie.getCensorship());
        bundle.putString("movieDirector", movie.getDirector());
        bundle.putString("movieDate1", movie.getDate1());
        bundle.putString("movieDate1_time1", movie.getDate1_time1());
        bundle.putString("movieDate1_time2", movie.getDate1_time2());
        bundle.putString("movieDate2", movie.getDate2());
        bundle.putString("movieDate2_time1", movie.getDate2_time1());
        bundle.putString("movieDate3", movie.getDate3());
        bundle.putString("movieDate3_time1", movie.getDate3_time1());
        bundle.putString("movieDate4", movie.getDate4());
        bundle.putString("movieDate4_time1", movie.getDate4_time1());
        bundle.putString("movieDate5", movie.getDate5());
        bundle.putString("movieDate5_time1", movie.getDate5_time1());
        bundle.putString("id", movie.getId());
        bundle.putString("room1", movie.getRoom1());
        return bundle;
    }

}

