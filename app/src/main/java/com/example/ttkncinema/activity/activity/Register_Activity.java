package com.example.ttkncinema.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register_Activity extends AppCompatActivity {
    TextInputEditText edtho, edtten, edtusername, edtemail, edtsdt, edtmatkhau;
    Button btnregister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtho = findViewById(R.id.edtho);
        edtusername = findViewById(R.id.edtusername);
        edtten = findViewById(R.id.edtten);
        edtemail = findViewById(R.id.edtemail);
        edtsdt = findViewById(R.id.edtsdt);
        edtmatkhau = findViewById(R.id.edtmatkhau);
        btnregister = findViewById(R.id.btndangki);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbarregister);
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

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ho = edtho.getText().toString();
                String ten = edtten.getText().toString();
                String username = edtusername.getText().toString();
                String email = edtemail.getText().toString();
                String sdt = edtsdt.getText().toString();
                String matkhau = edtmatkhau.getText().toString();
                if (ho.isEmpty() || ten.isEmpty() || username.isEmpty() || email.isEmpty() || sdt.isEmpty() || matkhau.isEmpty()) {
                    // Kiểm tra xem các trường thông tin đã được điền đầy đủ chưa
                    Toast.makeText(Register_Activity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
//                    mAuth.createUserWithEmailAndPassword(email, matkhau).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Nếu đăng ký thành công
//                                String userId = username;
//                                User user = new User(ho, ten, username, email, sdt, matkhau);
//                                mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            // Nếu lưu thông tin người dùng thành công
//                                            Toast.makeText(Register_Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
//                                            startActivity(intent);
//                                        } else {
//                                            // Nếu lưu thông tin người dùng thất bại
//                                            Toast.makeText(Register_Activity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
                    checkPhoneNumberExists(sdt, ho, ten, username, email, sdt, matkhau);
                }
            }
        });

    }

    private void checkPhoneNumberExists(final String phonenunber, final String ho, final String ten, final String username, final String email, final String sdt, final String matkhau) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("sdt").equalTo(phonenunber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(Register_Activity.this, "Số điện thoại đã được đăng kí", Toast.LENGTH_SHORT).show();
                } else {
                    checkUsernameExists(ho, ten, username, email, sdt, matkhau);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Register_Activity.this, "Lỗi kiểm tra số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUsernameExists(final String ho, final String ten, final String username, final String email, final String sdt, final String matkhau) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(Register_Activity.this, "Tên tài khoản đã được đăng kí", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(ho, ten, username, email, sdt, matkhau);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Register_Activity.this, "Lỗi Xác thực username", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String ho, String ten, String username, String email, String sdt, String matkhau) {
        mAuth.createUserWithEmailAndPassword(email, matkhau).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String hashedPassword = hashPassword(matkhau);
                    // Nếu đăng ký thành công
                    String userId = username;
                    User user = new User(ho, ten, username, email, sdt, hashedPassword);
                    mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Nếu lưu thông tin người dùng thành công
                                Toast.makeText(Register_Activity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                                startActivity(intent);
                            } else {
                                // Nếu lưu thông tin người dùng thất bại
                                Toast.makeText(Register_Activity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
        startActivity(new Intent(Register_Activity.this, Login_Activity.class));
        return super.onOptionsItemSelected(item);
    }
}
