package com.example.ttkncinema.activity.activity.taikhoan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ttkncinema.R;

public class DieukhoansudungActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieukhoansudung);
        Toolbar toolbar = findViewById(R.id.toolbardksd);
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

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}