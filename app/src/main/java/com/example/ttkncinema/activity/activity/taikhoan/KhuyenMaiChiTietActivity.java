package com.example.ttkncinema.activity.activity.taikhoan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.TinTuc;
import com.squareup.picasso.Picasso;

public class KhuyenMaiChiTietActivity extends AppCompatActivity {
    TextView texttintuc, chitiettintuc;
    ImageView imagetintuc;
    TinTuc tinTucThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyenmaichitiet);
        texttintuc = findViewById(R.id.texttintucchitiet);
        chitiettintuc = findViewById(R.id.chitiettintuc);
        imagetintuc = findViewById(R.id.imagetintuc);
        Toolbar toolbar = findViewById(R.id.toolbarkmct);
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
            String texttt = intent.getStringExtra("tentt");
            String imagett = intent.getStringExtra("imagett");
            String chitiettt = intent.getStringExtra("chitiettt");
            tinTucThongBao = new TinTuc(texttt, imagett, chitiettt);
            setImage(imagett);
            texttintuc.setText(texttt);
            chitiettintuc.setText(chitiettt);
        }
    }
    void setImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(imagetintuc);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}