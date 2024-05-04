package com.example.ttkncinema.activity.activity.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.ThongBao;
import com.squareup.picasso.Picasso;

public class ThongBaoChiTietActivity extends AppCompatActivity {
    TextView textthongbao, chitietthongbao;
    ImageView imagethongbao;
    ThongBao thongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao_chi_tiet);
        textthongbao = findViewById(R.id.textthongbaochitiet);
        chitietthongbao = findViewById(R.id.chitietthongbao);
        imagethongbao = findViewById(R.id.imagethongbao);
        Toolbar toolbar = findViewById(R.id.toolbartbct);
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
            String texttb = intent.getStringExtra("tentb");
            String imagetb = intent.getStringExtra("imagetb");
            String chitiettb = intent.getStringExtra("chitiettb");
            thongBao = new ThongBao(texttb, imagetb, chitiettb);
            setImage(imagetb);
            textthongbao.setText(texttb);
            chitietthongbao.setText(chitiettb);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    void setImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(imagethongbao);
    }

}