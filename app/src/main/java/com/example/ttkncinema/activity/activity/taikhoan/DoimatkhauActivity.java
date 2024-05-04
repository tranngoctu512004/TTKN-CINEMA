package com.example.ttkncinema.activity.activity.taikhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.Login_Activity;
import com.example.ttkncinema.activity.activity.MainActivity;
import com.example.ttkncinema.activity.activity.Register_Activity;
import com.example.ttkncinema.activity.activity.ResetPasswordActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DoimatkhauActivity extends AppCompatActivity {
    TextInputEditText edtMKcu, edtMKmoi, edtConfirmMK;
    Button btnSavedoimk;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doimatkhau);
        edtMKcu = findViewById(R.id.edtMKcu);
        edtMKmoi = findViewById(R.id.edtMKmoi);
        edtConfirmMK = findViewById(R.id.edtcomfirmMK);
        btnSavedoimk = findViewById(R.id.btnSavedoimk);
        Toolbar toolbar = findViewById(R.id.toolbardoimatkhau);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
        // Ẩn tiêu đề trên Toolbar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        btnSavedoimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doiMatKhau();
            }
        });


    }

    private void doiMatKhau() {
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
                        String mkCu = edtMKcu.getText().toString().trim();
                        String mkMoi = edtMKmoi.getText().toString().trim();
                        String haspassword = hashPassword(mkMoi);
                        String confirmMK = edtConfirmMK.getText().toString().trim();

                        if (mkCu.isEmpty() || mkMoi.isEmpty() || confirmMK.isEmpty()) {
                            Toast.makeText(DoimatkhauActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!mkMoi.equals(confirmMK)) {
                            Toast.makeText(DoimatkhauActivity.this, "Mật khẩu mới và xác nhận không khớp", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            DatabaseReference userRef = mDatabase.child("users");
                            userRef.orderByChild("sdt").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Số điện thoại tồn tại, cập nhật mật khẩu
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String userId = snapshot.getKey();
                                            mDatabase.child("users").child(userId).child("matkhau").setValue(haspassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> dbTask) {
                                                    if (dbTask.isSuccessful()) {
                                                        Toast.makeText(DoimatkhauActivity.this, "Cập nhật mật khẩu thành công trong Firebase", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(DoimatkhauActivity.this, Login_Activity.class);
                                                        startActivity(intent);
                                                        finish(); // Close this activity
                                                    } else {
                                                        Toast.makeText(DoimatkhauActivity.this, "Cập nhật mật khẩu thất bại trong Firebase", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(DoimatkhauActivity.this, "Số điện thoại không tồn tại trong Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle error
                                }
                            });
                        }
                    }
                } else {
                    // Thông báo nếu không tìm thấy thông tin người dùng
                    Toast.makeText(DoimatkhauActivity.this, "User information not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(DoimatkhauActivity.this, "Error retrieving user information: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String hashPassword(String password) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            md.update(password.getBytes());
            // Get the hash's bytes
            byte[] bytes = md.digest();
            // Convert the byte array into hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Return complete hashed password in hexadecimal format
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your needs
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}