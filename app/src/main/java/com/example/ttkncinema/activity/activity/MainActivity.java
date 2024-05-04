package com.example.ttkncinema.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ttkncinema.R;

import com.example.ttkncinema.activity.fragment.LichPhim_Fragment;
import com.example.ttkncinema.activity.fragment.TaiKhoan_Fragment;
import com.example.ttkncinema.activity.fragment.ThongBao_Fragment;
import com.example.ttkncinema.activity.fragment.TinTuc_Fragment;
import com.example.ttkncinema.activity.fragment.TrangChu_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationView = findViewById(R.id.bottomnavview);


        TrangChu_Fragment fragmentHome = new TrangChu_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,fragmentHome);
        fragmentTransaction.commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.home) {
                    fragment = new TrangChu_Fragment();
                } else if (item.getItemId() == R.id.lichphim) {
                    fragment = new LichPhim_Fragment();
                } else if (item.getItemId() == R.id.tintuc) {
                    fragment = new TinTuc_Fragment();
                } else if (item.getItemId() == R.id.thongbao) {
                    fragment = new ThongBao_Fragment();
                } else if (item.getItemId() == R.id.taikhoan) {
                    fragment= new TaiKhoan_Fragment();
                }
                if (fragment != null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }
}