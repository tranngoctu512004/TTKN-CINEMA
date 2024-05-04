package com.example.ttkncinema.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.ttkncinema.R;

public class Welcome_Activity extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Ánh xạ
        ImageView imageView = findViewById(R.id.image);

        // Tạo hiệu ứng thu phóng
        Animation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        scaleAnimation.setDuration(1000);// Thiết lập thời gian thực hiện là 1 giây

        // Kích hoạt hiệu ứng thu phóng trên ImageView
        imageView.startAnimation(scaleAnimation);

        // Tạo một Handler để chuyển hướng sau 2 giây
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Tạo Intent để chuyển từ màn hình chào đón đến màn hình chính
                Intent intent = new Intent(Welcome_Activity.this, MainActivity.class);
                startActivity(intent); // Thực hiện chuyển hướng
                finish(); // Đóng màn hình chào
            }
        }, 2000); // Thực hiện sau 2 giây
    }
}