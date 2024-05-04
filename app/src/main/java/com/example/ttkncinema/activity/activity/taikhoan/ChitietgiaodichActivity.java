package com.example.ttkncinema.activity.activity.taikhoan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.MainActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ChitietgiaodichActivity extends AppCompatActivity {

    private static final int NUMBER_OF_DIGITS = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietgiaodich);
        Toolbar toolbar = findViewById(R.id.toolbarctdd);
        setSupportActionBar(toolbar);

        // Tạo nội dung ngẫu nhiên và không trùng lặp
        String content = generateRandomContent();

        // Tạo mã vạch
        Bitmap barcodeBitmap = generateBarcode(content);

        // Hiển thị mã vạch trên ImageView hoặc nơi bạn muốn
        ImageView barcodeImageView = findViewById(R.id.barcode);
        barcodeImageView.setImageBitmap(barcodeBitmap);

        // Hiển thị nút back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(ChitietgiaodichActivity.this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private String generateRandomContent() {
        Set<String> generatedContents = new HashSet<>();

        // Tạo số ngẫu nhiên và kiểm tra trùng lặp
        while (true) {
            String randomContent = generateRandomNumber(NUMBER_OF_DIGITS);
            if (!generatedContents.contains(randomContent)) {
                generatedContents.add(randomContent);
                return randomContent;
            }
        }
    }

    private String generateRandomNumber(int numberOfDigits) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numberOfDigits; i++) {
            int digit = random.nextInt(10);
            stringBuilder.append(digit);
        }
        return stringBuilder.toString();
    }

    private Bitmap generateBarcode(String content) {
        EAN13Writer writer = new EAN13Writer();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.EAN_13, 600, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
