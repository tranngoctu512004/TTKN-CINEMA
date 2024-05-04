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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputEditText edtnhapmatkhau, edtconfirmpassword;
    Button btnxacnhanmatkhau;
    private FirebaseAuth mAuth;
    Toolbar toolbar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        edtnhapmatkhau = findViewById(R.id.edtnhapmatkhau);
        edtconfirmpassword = findViewById(R.id.edtconfirmpassword);
        btnxacnhanmatkhau = findViewById(R.id.btnxacnhanmatkhau);
        toolbar = findViewById(R.id.toolbarreset);
        mAuth = FirebaseAuth.getInstance();
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

        Intent intent = getIntent();
        final String phoneNumber = intent.getStringExtra("phoneNumber");
        btnxacnhanmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForNewPassword(phoneNumber);
            }
        });
    }

    private void showDialogForNewPassword(final String phoneNumber) {

        String newPassword = edtnhapmatkhau.getText().toString();
        String confirmPassword = edtconfirmpassword.getText().toString();
        String haspassword  = hashPassword(newPassword);

        if (newPassword.isEmpty() || confirmPassword.isEmpty() ){
            Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ResetPasswordActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
        } else {
            // Kiểm tra số điện thoại tồn tại trong Firebase Realtime Database
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
                                        Toast.makeText(ResetPasswordActivity.this, "Cập nhật mật khẩu thành công trong Firebase", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ResetPasswordActivity.this, Login_Activity.class);
                                        startActivity(intent);
                                        finish(); // Close this activity
                                    } else {
                                        Toast.makeText(ResetPasswordActivity.this, "Cập nhật mật khẩu thất bại trong Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Số điện thoại không tồn tại trong Firebase", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
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
        startActivity(new Intent(ResetPasswordActivity.this,ImportOTPActivity.class ));
        return super.onOptionsItemSelected(item);
    }

}