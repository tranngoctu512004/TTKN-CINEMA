
package com.example.ttkncinema.activity.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.taikhoan.ChitietgiaodichActivity;
import com.example.ttkncinema.activity.model.CreateOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Thanhtoan_Activity extends AppCompatActivity {

    TextView lblZpTransToken, txtToken;
    Button  btnPay;
    private int tongtienghe;
    String idphimtt, tenphimtt, ngaychieutt, suatchieutt, thoiluongtt, anhghett, idusertt, phong, idnguoidungtt;
    ArrayList<String> ghett = new ArrayList<>();
    ArrayList<String> idghett = new ArrayList<>();
    ArrayList<String> idseatstt = new ArrayList<>();
    LinearLayout thanhToanNoidia;
    LinearLayout thanhToanQuocTe;
    LinearLayout thanhtoanZaloPay;
    LinearLayout thanhToanMomo;
    private LinearLayout selectedOption;
    DatabaseReference databaseReference, reference;

    private boolean isPaymentSuccessful = false;
    boolean check = false;
    boolean isCheck = false;

    private void BindView() {
        txtToken = findViewById(R.id.txtToken);
        lblZpTransToken = findViewById(R.id.lblZpTransToken);
        btnPay = findViewById(R.id.btnPay);
         thanhToanNoidia = findViewById(R.id.thenoidia);
         thanhToanQuocTe = findViewById(R.id.thequocte);
         thanhToanMomo = findViewById(R.id.momo);
         thanhtoanZaloPay=findViewById(R.id.ZaloPay);
        IsLoading();
    }

    private void IsLoading() {
        lblZpTransToken.setVisibility(View.INVISIBLE);
        txtToken.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.VISIBLE);
    }

    private void IsDone() {
        lblZpTransToken.setVisibility(View.VISIBLE);
        txtToken.setVisibility(View.VISIBLE);
        btnPay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
//        btnthanhtoan = findViewById(R.id.btnthanhtoan);

        retrieveAndDisplayUserInfo();
        laychuoighe();


        Toolbar toolbar = findViewById(R.id.toolbarthanht);
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
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                tongtienghe = bundle.getInt("tongtienghe");
                idphimtt = bundle.getString("idphim");
                idusertt = bundle.getString("iduser");
                tenphimtt = bundle.getString("tenphim");
                anhghett = bundle.getString("anhseat");
                ngaychieutt = bundle.getString("ngaychieuseat");
                suatchieutt = bundle.getString("suatchieuseat");
                thoiluongtt = bundle.getString("thoiluongseat");
                phong = bundle.getString("idphong");
                ghett = bundle.getStringArrayList("idghe");
                if (ghett != null) {
                    for (String so : ghett) {
                        String letters = so.replaceAll("[^a-zA-Z]", ""); // Lấy chữ
                        String numbers = so.replaceAll("[^0-9]", ""); // Lấy số
                        idghett.add(numbers);
                    }
                }

        }

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BindView();
        thanhToanNoidia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(thanhToanNoidia);
                showMaintenanceAlert();
            }
        });

        // Thiết lập OnClickListener cho thanhToanQuocTe
        thanhToanQuocTe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(thanhToanQuocTe);
                showMaintenanceAlert();
            }
        });
        thanhtoanZaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(thanhtoanZaloPay);

            }
        });

        // Thiết lập OnClickListener cho thanhToanMomo
        thanhToanMomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(thanhToanMomo);
                showMaintenanceAlert();
            }
        });
        // ZaloPay SDK Init
        ZaloPaySDK.init(2554, Environment.SANDBOX);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")

            @Override
            public void onClick(View v) {
                if (thanhtoanZaloPay.isSelected()) {

                    CreateOrder orderApi = new CreateOrder();

                    try {
                        JSONObject data = orderApi.createOrder(String.valueOf(tongtienghe));
                        Log.d("Amount", String.valueOf(tongtienghe));
                        lblZpTransToken.setVisibility(View.VISIBLE);
                        String code = data.getString("return_code");
//                    Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                        if (code.equals("1")) {
                            lblZpTransToken.setText("zptranstoken");
                            txtToken.setText(data.getString("zp_trans_token"));
                            IsDone();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String token = txtToken.getText().toString();
                    ZaloPaySDK.getInstance().payOrder(Thanhtoan_Activity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(Thanhtoan_Activity.this)
                                            .setTitle("Payment Success")
                                            .setMessage(String.format("Bạn đã thanh toán thành công"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }

                            });
                            IsLoading();
                        }

                        @Override
                        public void onPaymentCanceled(String zpTransToken, String appTransID) {
                            new AlertDialog.Builder(Thanhtoan_Activity.this)
                                    .setTitle("User Cancel Payment")
                                    .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }


                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {

                            new AlertDialog.Builder(Thanhtoan_Activity.this)
                                    .setTitle("Payment Fail")
                                    .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            isPaymentSuccessful = true;
                                            isCheck = false;
                                            laychuoighe();

                                            startActivity(new Intent(Thanhtoan_Activity.this, ChitietgiaodichActivity.class));

                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    });
                }
            }

        });

    }

    private void updatetrangthaighe(String status){
        ArrayList<String> seatsselectedtt = new ArrayList<>();
        for (String b : idghett) {
            String letters = b.replaceAll("[^a-zA-Z]", ""); // Lấy chữ
            String numbers = b.replaceAll("[^0-9]", ""); // Lấy số
            seatsselectedtt.add(numbers);
        }

        ArrayList<String> seatsselectedtt1 = new ArrayList<>();
        for (String b : idseatstt) {
            String letters = b.replaceAll("[^a-zA-Z]", ""); // Lấy chữ
            String numbers = b.replaceAll("[^0-9]", ""); // Lấy số
            seatsselectedtt1.add(numbers);
        }
        if (isPaymentSuccessful == true){
            if (idnguoidungtt.equals(idusertt)) {
                // Thay đổi hình nền ghế đã chọn
                for(int i=0;i<seatsselectedtt.size();i++){
                    for(int j=0;j<seatsselectedtt1.size();j++){
                        if(seatsselectedtt.get(i).equals(seatsselectedtt1.get(j))){
                            if (Integer.parseInt(seatsselectedtt.get(i)) > 0 && Integer.parseInt(seatsselectedtt.get(i)) < 23) {
                                for (int m = 0; m < status.length(); m++) {
                                    if (m < 11) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) - 1 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m] == 'F'){
                                                newchar[m] = 'U';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='F'){
                                                newchar[m] = 'U';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    }
                                }
                            } else if (Integer.parseInt(seatsselectedtt.get(i)) > 22 && Integer.parseInt(seatsselectedtt.get(i)) < 65) {
                                for (int m = 0; m < status.length(); m++) {
                                    if (m > 22 && m <= 34) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 1 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m] == 'G'){
                                                newchar[m] = 'C';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (m > 34 && m <= 46) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 2 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='G'){
                                                newchar[m] = 'C';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (m > 46 && m <= 58) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 4 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m] == 'G'){
                                                newchar[m] = 'C';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (m > 58 && m <= 70) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 6 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='G'){
                                                newchar[m] = 'C';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check =true;
                                            }
                                        }
                                    }
                                }
                            } else {
                                for (int m = 0; m < status.length(); m++) {
                                    if (Integer.parseInt(seatsselectedtt.get(i)) == 65) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 8 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='H'){
                                                newchar[m] = 'L';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (Integer.parseInt(seatsselectedtt.get(i)) == 66) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 9 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='H'){
                                                newchar[m] = 'L';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (Integer.parseInt(seatsselectedtt.get(i)) == 67) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 10 && check == false) {

                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='H'){
                                                newchar[m] = 'L';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (Integer.parseInt(seatsselectedtt.get(i)) == 68) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 11 && check == false) {

                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='H'){
                                                newchar[m] = 'L';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    } else if (Integer.parseInt(seatsselectedtt.get(i)) == 69) {
                                        if (m == Integer.parseInt(seatsselectedtt.get(i)) + 12 && check == false) {
                                            char[] newchar = status.toCharArray();
                                            if (newchar[m]=='H'){
                                                newchar[m] = 'L';
                                                String update = new String(newchar);
                                                updateSeatStatusRealtime(update);
                                                check = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }else {

                        }
                    }
                }
                if (isCheck == false){
                    isCheck = true;
                    reference = FirebaseDatabase.getInstance().getReference("SeatsSelected").child(idusertt);
                    reference.removeValue();
                }
            } else {
                System.out.println("Loi");
            }
        }
    }

    private void updateSeatStatusRealtime(String status) {
        databaseReference = FirebaseDatabase.getInstance().getReference("seats");
        databaseReference.child(idphimtt).child(ngaychieutt).child(suatchieutt).child("seat").setValue(status);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void Layiduserselected() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SeatsSelected");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Lấy giá trị idnguoidung (là key của mỗi nút người dùng trong "SeatsSelected")
                    String idnguoidungselected = userSnapshot.getKey();
                    idusertt = idnguoidungselected;

                    for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        if (key.equalsIgnoreCase(idphimtt)) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String key1 = dataSnapshot1.getKey();
                                if (key1.equalsIgnoreCase(ngaychieutt)) {
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                        String key2 = dataSnapshot2.getKey();
                                        if (key2.equalsIgnoreCase(suatchieutt)) {
                                            // Lấy danh sách các giá trị idseat cho người dùng hiện tại
                                            for (DataSnapshot seatSnapshot : dataSnapshot2.getChildren()) {
                                                // Lấy key của mỗi nút, giả sử nó có dạng "seat1", "seat2", ...
                                                String seatKey = seatSnapshot.getKey();
                                                idseatstt.add(seatKey);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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
                        String username = userSnapshot.child("username").getValue(String.class);
                        idnguoidungtt = username;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void laychuoighe() {
        databaseReference = FirebaseDatabase.getInstance().getReference("seats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Duyệt qua các con của dataSnapshot
                        String key2 = dataSnapshot.getKey();
                        if (key2.equalsIgnoreCase(idphimtt)) {
                            for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                // Lấy giá trị của node seat
                                String key = seatSnapshot.getKey();
                                if (key.equalsIgnoreCase(ngaychieutt)) {
                                    for (DataSnapshot snapshot1 : seatSnapshot.getChildren()) {
                                        String key1 = snapshot1.getKey();
                                        if (key1.equalsIgnoreCase(suatchieutt)) {
                                            String updatedSeatValue = snapshot1.child("seat").getValue(String.class);
                                            check = false;
                                            Layiduserselected();
                                            updatetrangthaighe(updatedSeatValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void handleOptionClick(LinearLayout selectedLayout) {
        // Kiểm tra xem đã có nút nào được chọn trước đó không
        if (selectedOption != null) {
            // Đặt lại trạng thái của nút trước đó về trạng thái bình thường
            selectedOption.setSelected(false);
        }

        // Đặt trạng thái của nút mới được chọn
        selectedLayout.setSelected(true);

        // Cập nhật biến theo dõi nút được chọn
        selectedOption = selectedLayout;
    }
    private void showMaintenanceAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Phương thức thanh toán đang bảo trì! Vui lòng chọn phương thức khác");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng nhấn OK (nếu cần thiết)
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
