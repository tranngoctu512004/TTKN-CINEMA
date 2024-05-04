package com.example.ttkncinema.activity.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.Movie;
import com.squareup.picasso.Picasso;

public class Information_Activity extends AppCompatActivity {
    TextView tvtenphimìnomation, tvnoidunginfomation, tvkiemduyetinfnomation, tvkhoichieuinfomation,
            tvdaodieninfomation, tvthoiluonginfomation;
    ImageView imageinfomation;
    Button btndatve;
    Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        imageinfomation = findViewById(R.id.imageinfomation);
        tvtenphimìnomation = findViewById(R.id.tvtenphimìnomation);
        tvnoidunginfomation = findViewById(R.id.tvnoidunginfomation);
        tvkiemduyetinfnomation = findViewById(R.id.tvkiemduyetinfnomation);
        tvkhoichieuinfomation = findViewById(R.id.tvkhoichieuinfomation);
        tvdaodieninfomation = findViewById(R.id.tvdaodieninfomation);
        tvthoiluonginfomation = findViewById(R.id.tvthoiluonginfomation);
        btndatve = findViewById(R.id.btndatve);
        Toolbar toolbar = findViewById(R.id.toolbarinfomation);
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
                String image = bundle.getString("movieImage");
                String title = bundle.getString("movieTitle");
                String content = bundle.getString("movieContent");
                String censorship= bundle.getString("movieCensorship");
                String date1 = bundle.getString("movieDate1");
                String director = bundle.getString("movieDirector");
                String time = bundle.getString("movieTime");
                String date1_time1 = bundle.getString("movieDate1_time1");
                String date1_time2 = bundle.getString("movieDate1_time2");
                String date2 = bundle.getString("movieDate2");
                String date3 = bundle.getString("movieDate3");
                String date4 = bundle.getString("movieDate4");
                String date5 = bundle.getString("movieDate5");
                String date2_time1 = bundle.getString("movieDate2_time1");
                String date3_time1 = bundle.getString("movieDate3_time1");
                String date4_time1 = bundle.getString("movieDate4_time1");
                String date5_time1 = bundle.getString("movieDate5_time1");
                String id = bundle.getString("id");
                String room1 = bundle.getString("room1");

                movie =new Movie(image, title, time, content, censorship, director,date1,date1_time1,
                        date1_time2,date2,date2_time1,date3,date3_time1,date4,date4_time1,date5,date5_time1,id, room1);
                setImage(image);
                tvtenphimìnomation.setText(title);
                tvnoidunginfomation.setText(content);
                tvkiemduyetinfnomation.setText(censorship);
                tvkhoichieuinfomation.setText(date1);
                tvdaodieninfomation.setText(director);
                tvthoiluonginfomation.setText(time);
            }
        }
        btndatve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movie != null) {
                    Intent intent = new Intent(Information_Activity.this, DateActivity.class);
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
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
    }

    void setImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(imageinfomation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
