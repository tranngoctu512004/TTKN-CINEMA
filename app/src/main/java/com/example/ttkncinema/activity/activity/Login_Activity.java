package com.example.ttkncinema.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login_Activity extends AppCompatActivity {
    TextInputEditText edtUsernameOrEmail, edtpassword;
    Button btnLogin, btnRegister;
    CheckBox chkcheckbox;
    TextView forgotpass;

    private String userRegister = " ";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các thành phần giao diện
        edtUsernameOrEmail = findViewById(R.id.edtuserlogin);
        edtpassword = findViewById(R.id.edtmatkhaulogin);
        btnLogin = findViewById(R.id.btndangnhaplogin);
        btnRegister = findViewById(R.id.btndangkilogin);
        chkcheckbox = findViewById(R.id.chkghinho);
        forgotpass = findViewById(R.id.forgotpass);
        Toolbar toolbar = findViewById(R.id.toolbarlogin);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Thiết lập nút Home/Up để chuyển về màn hình trước đó
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // Bắt sự kiện khi click vào nút "Đăng ký"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Register_Activity.class));
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,Forgotpass_Activity.class));
            }
        });

        // Kiểm tra xem có thông tin đăng nhập được lưu trong SharedPreferences không
        SharedPreferences sharedPreferences = getSharedPreferences("getdata", MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("isremember", false);
        if (isRemember) {
            String user = sharedPreferences.getString("userLogin", "");
            String pass = sharedPreferences.getString("passLogin", "");
            edtUsernameOrEmail.setText(user);
            edtpassword.setText(pass);
            chkcheckbox.setChecked(isRemember);
            userRegister = user;
        }

        // Bắt sự kiện khi click vào nút "Đăng nhập"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameOrEmail = edtUsernameOrEmail.getText().toString();
                String password = edtpassword.getText().toString();

                if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login_Activity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Đăng nhập người dùng
                    checkUserInDatabase(usernameOrEmail, password);
                }
            }
        });
    }

    // Phương thức kiểm tra người dùng trong database Firebase
    private void checkUserInDatabase(String usernameOrEmail, String password) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Mã hóa mật khẩu để so sánh với mật khẩu đã lưu trong database
        String enteredPasswordHash = hashPassword(password);

        // Kiểm tra xem người dùng có tồn tại trong database theo username không
        Query query = usersRef.orderByChild("username").equalTo(usernameOrEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Người dùng tồn tại, kiểm tra mật khẩu
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        String deviceId = user.getUsername();
                        String storedPasswordHash = user.getMatkhau();

                        // So sánh mật khẩu nhập vào với mật khẩu đã lưu
                        if (enteredPasswordHash.equals(storedPasswordHash)) {
                            // Nếu mật khẩu khớp, cập nhật thông tin người dùng và lưu đăng nhập
                            usersRef.child(userSnapshot.getKey()).setValue(user);
                            if (chkcheckbox.isChecked()) {
                                saveLoginInfo(usernameOrEmail, password);
                            }
                            Toast.makeText(Login_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            saveDeviceInformation(deviceId, true);
                            startActivity(new Intent(Login_Activity.this, MainActivity.class));
                            finish();
                            return;
                        }
                    }
                }

                // Nếu không tìm thấy theo username, kiểm tra theo email
                Query emailQuery = usersRef.orderByChild("email").equalTo(usernameOrEmail);
                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot emailDataSnapshot) {
                        if (emailDataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : emailDataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                String deviceId = user.getUsername();
                                String storedPasswordHash = user.getMatkhau();

                                // So sánh mật khẩu nhập vào với mật khẩu đã lưu
                                if (enteredPasswordHash.equals(storedPasswordHash)) {
                                    // Nếu mật khẩu khớp, cập nhật thông tin người dùng và lưu đăng nhập
                                    usersRef.child(userSnapshot.getKey()).setValue(user);
                                    if (chkcheckbox.isChecked()) {
                                        saveLoginInfo(usernameOrEmail, password);
                                    }
                                    Toast.makeText(Login_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    saveDeviceInformation(deviceId, true);
                                    startActivity(new Intent(Login_Activity.this, MainActivity.class));
                                    finish();
                                    return;
                                }
                            }
                        }

                        // Nếu không tìm thấy theo username hoặc email, thông báo lỗi
                        Toast.makeText(Login_Activity.this, "Tên người dùng hoặc email không tồn tại hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý lỗi trong quá trình truy vấn database
                        Toast.makeText(Login_Activity.this, "Đăng nhập thất bại: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi trong quá trình truy vấn database
                Toast.makeText(Login_Activity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức lưu thông tin thiết bị xuống Firebase Realtime Database
    private void saveDeviceInformation(String usernameOrEmail, boolean isLoggedIn) {
        DatabaseReference devicesRef = FirebaseDatabase.getInstance().getReference("devices");
        devicesRef.child(usernameOrEmail).child("isLoggedIn").setValue(isLoggedIn);
    }

    // Phương thức lưu thông tin đăng nhập vào SharedPreferences
    private void saveLoginInfo(String usernameOrEmail, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("getdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userLogin", usernameOrEmail);
        editor.putString("passLogin", password);
        editor.putBoolean("isremember", true);
        editor.apply();
    }

    // Bắt sự kiện khi nút Home/Up được nhấn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(Login_Activity.this, Welcome_Activity.class));
        return super.onOptionsItemSelected(item);
    }

    // Phương thức mã hóa mật khẩu bằng thuật toán MD5
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
