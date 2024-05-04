package com.example.ttkncinema.activity.activity.taikhoan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ThongtincanhanActivity extends AppCompatActivity {
    private TextInputEditText edtho, edtten, edtSdt, edtUsername, edtEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtincanhan);

        // Ánh xạ các thành phần giao diện
        edtho = findViewById(R.id.edthottcn);
        edtten = findViewById(R.id.edttenttcn);
        edtSdt = findViewById(R.id.edtSdtttcn);
        edtUsername = findViewById(R.id.edtUsernamettcn);
        edtEmail = findViewById(R.id.edtEmailttcn);
        Toolbar toolbar = findViewById(R.id.toolbarthongtincanhan);

        // Khởi tạo Firebase Authentication
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Thiết lập nút Home/Up để chuyển về màn hình trước đó
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Gọi hàm để lấy và hiển thị thông tin người dùng
        retrieveAndDisplayUserInfo();
    }

    // Phương thức lấy và hiển thị thông tin người dùng
    private void retrieveAndDisplayUserInfo() {
        // Đọc dữ liệu từ SharedPreferences để lấy tên người dùng hoặc email
        SharedPreferences sharedPreferences = getSharedPreferences("getdata", MODE_PRIVATE);
        String usernameOrEmail = sharedPreferences.getString("userLogin", "");

        // Tham chiếu đến node "users" trong Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Tạo truy vấn để lấy thông tin người dùng theo username hoặc email
        Query query = usersRef.orderByChild("username").equalTo(usernameOrEmail);

        // Lắng nghe sự kiện khi có dữ liệu thay đổi
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra xem có dữ liệu người dùng hay không
                if (dataSnapshot.exists()) {
                    // Lặp qua tất cả các snapshot để lấy thông tin người dùng
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Lấy thông tin người dùng từ snapshot
                        String ho = userSnapshot.child("ho").getValue(String.class);
                        String ten = userSnapshot.child("ten").getValue(String.class);
                        String sdt = userSnapshot.child("sdt").getValue(String.class);
                        String username = userSnapshot.child("username").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);

                        // Hiển thị thông tin người dùng trên giao diện
                        edtho.setText(ho);
                        edtten.setText(ten);
                        edtSdt.setText(sdt);
                        edtUsername.setText(username);
                        edtEmail.setText(email);
                    }
                } else {
                    // Thông báo nếu không tìm thấy thông tin người dùng
                    Toast.makeText(ThongtincanhanActivity.this, "User information not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(ThongtincanhanActivity.this, "Error retrieving user information: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Bắt sự kiện khi nút Home/Up được nhấn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Chuyển về màn hình chính khi nút Home/Up được nhấn
        finish();
        return super.onOptionsItemSelected(item);
    }
}
