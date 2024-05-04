package com.example.ttkncinema.activity.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ImportOTPActivity extends AppCompatActivity {
    TextInputEditText edtsendotp;
    Button btnguimaotp;
    TextView tvguimaotp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mVerificationId, phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_otpactivity);
        edtsendotp = findViewById(R.id.edtsendotp);
        btnguimaotp = findViewById(R.id.btnguimaotp);
        tvguimaotp = findViewById(R.id.tvguimaotp);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.toolbarimportotp);
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

        // Retrieve the verificationId and phoneNumber from the intent
        Intent intent = getIntent();
        mVerificationId = intent.getStringExtra("verificationId");
        phoneNumber = intent.getStringExtra("phoneNumber");

        tvguimaotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = mDatabase.child("users");
                reference.orderByChild("sdt").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            getOTP(phoneNumber);
                        } else {
                            Toast.makeText(ImportOTPActivity.this, "Số điện thoại chưa được đăng ký", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled: " + error.getMessage());
                    }
                });
            }
        });
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Called if verification is completed automatically (e.g., on emulator)
                // You may choose to handle this case if needed
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e(TAG, "onVerificationFailed: " + e.getMessage());
                Toast.makeText(ImportOTPActivity.this, "Verification failed. Check logs for details.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: Verification code sent successfully");

                // Save the new verificationId
                mVerificationId = verificationId;

                // Enable UI to request a new OTP
                tvguimaotp.setEnabled(true);
                tvguimaotp.setText("Gửi lại mã OTP");

                // You can also show a toast or perform any other UI updates as needed
                Toast.makeText(ImportOTPActivity.this, "Mã OTP đã được gửi lại", Toast.LENGTH_SHORT).show();
            }
        };

        btnguimaotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtsendotp.getText().toString().trim();
                if (!otp.isEmpty()) {
                    verifyOTP(otp);
                } else {
                    Toast.makeText(ImportOTPActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ImportOTPActivity.this, "Xác Thực thành công", Toast.LENGTH_SHORT).show();

                    // Get the phone number from the intent
                    Intent intent = getIntent();
                    String phoneNumber = intent.getStringExtra("phoneNumber");

                    // Start the ResetPasswordActivity and pass the phone number
                    Intent resetPasswordIntent = new Intent(ImportOTPActivity.this, ResetPasswordActivity.class);
                    resetPasswordIntent.putExtra("phoneNumber", phoneNumber);
                    startActivity(resetPasswordIntent);
                    finish(); // Close this activity
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(ImportOTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
