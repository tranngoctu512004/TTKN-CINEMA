package com.example.ttkncinema.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Forgotpass_Activity extends AppCompatActivity {
    private static final String TAG = "Forgotpass_Activity";

    TextInputEditText edtsdtforgotpass;
    Button btnforgot;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        edtsdtforgotpass = findViewById(R.id.edtsdtforgotpass);
        btnforgot = findViewById(R.id.btnforgot);
        Toolbar toolbar = findViewById(R.id.toolbarforgot);
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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Called if verification is completed automatically (e.g., on emulator)
                // You may choose to handle this case if needed
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Log the error for debugging
                Log.e(TAG, "onVerificationFailed: " + e.getMessage());
                Toast.makeText(Forgotpass_Activity.this, "Verification failed. Check logs for details.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Log success for debugging
                Log.d(TAG, "onCodeSent: Verification code sent successfully");
                // Start the OTP input activity
                Intent sendIntent = new Intent(Forgotpass_Activity.this, ImportOTPActivity.class);
                sendIntent.putExtra("phoneNumber", edtsdtforgotpass.getText().toString());
                sendIntent.putExtra("verificationId", verificationId);
                startActivity(sendIntent);
            }
        };
        btnforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber = edtsdtforgotpass.getText().toString().trim();
                DatabaseReference reference = mDatabase.child("users");
                reference.orderByChild("sdt").equalTo(phonenumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            getOTP(phonenumber);
                        } else {
                            Toast.makeText(Forgotpass_Activity.this, "Số điện thoại chưa được đăng ký", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled: " + error.getMessage());
                    }
                });
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
        startActivity(new Intent(Forgotpass_Activity.this,Login_Activity.class ));
        return super.onOptionsItemSelected(item);
    }
}
