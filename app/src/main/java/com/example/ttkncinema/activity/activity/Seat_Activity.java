package com.example.ttkncinema.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ttkncinema.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seat_Activity extends AppCompatActivity implements View.OnClickListener {

    ViewGroup layout;
    List<TextView> seatViewList = new ArrayList<>();

    int seatSize = 80;
    int seatGaping = 15;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    int STATUS_SELECTE = 4;
    String selectedIds = "";
    public DatabaseReference databaseReference, reference, seatsSelectedRef;
    ;
    String capnhat;

    long count;
    TextView suatchieu, ngaychieu, tenphim, thoiluong, soghe, tongtien;
    ArrayList<String> ghe = new ArrayList<>();
    int tongtienghe;
    String idnguoidung, iduserselected, tenphimseat, ngaychieuseat, thoiluongseat, suatchieuseat, idphong, idphim, imganh;
    List<Integer> idseats;
    int idghe;
    Button btntieptucseat;
    ImageView imageView;
    private HashMap<Integer, Boolean> seatSelectionMap = new HashMap<>();
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        layout = findViewById(R.id.layoutSeat);
        tenphim = findViewById(R.id.tenphimseat);
        suatchieu = findViewById(R.id.suatchieuphimseat);
        ngaychieu = findViewById(R.id.ngaychieuphimseat);
        thoiluong = findViewById(R.id.thoiluongseat);
        soghe = findViewById(R.id.soghe);
        imageView = findViewById(R.id.imagedghe);
        tongtien = findViewById(R.id.tongtienseat);
        btntieptucseat = findViewById(R.id.btntieptucseat);
        Toolbar toolbar = findViewById(R.id.toolbarseat);
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
            tenphimseat = tenphim.getText().toString();
            ngaychieuseat = ngaychieu.getText().toString();
            suatchieuseat = suatchieu.getText().toString();
            thoiluongseat = thoiluong.getText().toString();
            tenphimseat = bundle.getString("title");
            thoiluongseat = bundle.getString("time");
            idphim = bundle.getString("id");
            ngaychieuseat = bundle.getString("date");
            suatchieuseat = bundle.getString("time1");
            idphong = bundle.getString("room1");
            imganh = bundle.getString("image");

        }
        tenphim.setText(tenphimseat);
        ngaychieu.setText(ngaychieuseat);
        suatchieu.setText(suatchieuseat);
        thoiluong.setText(thoiluongseat);
        setImage(imganh);

        retrieveAndDisplayUserInfo();
        Layiduserselected();
        seatsSelectedRef = FirebaseDatabase.getInstance().getReference("SeatsSelected");
        seatsSelectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Duyệt qua các con của dataSnapshot
                        String key2 = dataSnapshot.getKey();
                        if (key2.equalsIgnoreCase(idnguoidung)) {
                            for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                // Lấy giá trị của node seat
                                String key = seatSnapshot.getKey();
                                if (key.equalsIgnoreCase(idphim)) {
                                    for (DataSnapshot snapshot1 : seatSnapshot.getChildren()) {
                                        String key1 = snapshot1.getKey();
                                        if (key1.equalsIgnoreCase(ngaychieuseat)) {
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                String key3 = snapshot2.getKey();
                                                if (key3.equalsIgnoreCase(suatchieuseat)) {
                                                    count = snapshot2.getChildrenCount();
                                                    Log.d("dem", "kq" + count);
                                                }
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
        databaseReference = FirebaseDatabase.getInstance().getReference("seats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Duyệt qua các con của dataSnapshot
                        String key2 = dataSnapshot.getKey();
                        if (key2.equalsIgnoreCase(idphim)) {
                            for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                // Lấy giá trị của node seat
                                String key = seatSnapshot.getKey();
                                if (key.equalsIgnoreCase(ngaychieuseat)) {
                                    for (DataSnapshot snapshot1 : seatSnapshot.getChildren()) {
                                        String key1 = snapshot1.getKey();
                                        if (key1.equalsIgnoreCase(suatchieuseat)) {
                                            String updatedSeatValue = snapshot1.child("seat").getValue(String.class);
                                            capnhat = updatedSeatValue;
                                            if (updatedSeatValue != null) {
                                                updateSeatLayout(updatedSeatValue);
                                                updateUI();

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

        btntieptucseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundlechuyendata = new Bundle();
                bundlechuyendata.putString("idphim", idphim);
                bundlechuyendata.putString("ngaychieuseat", ngaychieuseat);
                bundlechuyendata.putString("suatchieuseat", suatchieuseat);
                bundlechuyendata.putStringArrayList("idghe", ghe);
                bundlechuyendata.putString("thoiluongseat", thoiluongseat);
                bundlechuyendata.putString("idphong", idphong);
                bundlechuyendata.putString("tenphim", tenphimseat);
                bundlechuyendata.putString("anhseat", imganh);
                bundlechuyendata.putString("iduser", iduserselected);
                bundlechuyendata.putInt("tongtienghe", tongtienghe);
                Intent intent1 = new Intent(Seat_Activity.this, Thanhtoan_Activity.class);
                intent1.putExtras(bundlechuyendata);
                startActivity(intent1);
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
                        idnguoidung = username;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Layiduserselected() {
        reference = FirebaseDatabase.getInstance().getReference("SeatsSelected");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Xử lý dữ liệu khi có sự thay đổi
                idseats = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Lấy giá trị idnguoidung (là key của mỗi nút người dùng trong "SeatsSelected")
                    String idnguoidungselected = userSnapshot.getKey();
                    iduserselected = idnguoidungselected;

                    for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        if (key.equalsIgnoreCase(idphim)) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String key1 = dataSnapshot1.getKey();
                                if (key1.equalsIgnoreCase(ngaychieuseat)) {
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                        String key2 = dataSnapshot2.getKey();
                                        if (key2.equalsIgnoreCase(suatchieuseat)) {
                                            // Lấy danh sách các giá trị idseat cho người dùng hiện tại
                                            for (DataSnapshot seatSnapshot : dataSnapshot2.getChildren()) {
                                                // Lấy key của mỗi nút, giả sử nó có dạng "seat1", "seat2", ...
                                                String seatKey = seatSnapshot.getKey();

                                                // Kiểm tra xem key có bắt đầu bằng "seat" không
                                                if (seatKey != null && seatKey.startsWith("seat")) {
                                                    // Lấy giá trị của idseat từ mỗi nút
                                                    Object idseatObject = seatSnapshot.getValue();
                                                    if (idseatObject instanceof Integer) {
                                                        Integer idseat = (Integer) idseatObject;
                                                        // Thêm idseat vào danh sách
                                                        idseats.add(idseat);
                                                    } else {
                                                        // Xử lý khi giá trị idseat không phải là kiểu Integer
                                                    }
                                                }
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

    private void SaveSeatsSelected(String userID, int seatID, String idphimselected, String idngaychieuselected, String idsuatchieuselected) {
        reference = FirebaseDatabase.getInstance().getReference("SeatsSelected").child(userID).child(idphimselected).child(idngaychieuselected).child(idsuatchieuselected);
        reference.child("seat" + seatID).setValue(true);
    }

    private void DeleteSeatSelected(String userID, int seatID, String idphimselected, String idngaychieuselected, String idsuatchieuselected) {
        reference = FirebaseDatabase.getInstance().getReference("SeatsSelected").child(userID).child(idphimselected).child(idngaychieuselected).child(idsuatchieuselected);
        reference.child("seat" + seatID).removeValue();
    }

    public void updateSeatStatusRealtime(String status) {
        databaseReference.child(idphim).child(ngaychieuseat).child(suatchieuseat).child("seat").setValue(status);
    }


    private void updateSeatLayout(String seatData) {
        layout.removeAllViews();
        seatViewList.clear();
        seatData = "/" + seatData;

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(5 * seatGaping, 5 * seatGaping, 5 * seatGaping, 5 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;

        int count = 0;

        for (int index = 0; index < seatData.length(); index++) {
            if (seatData.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seatData.charAt(index) == 'U' || seatData.charAt(index) == 'C' || seatData.charAt(index) == 'L') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);

                if (seatData.charAt(index) == 'U') {
                    view.setBackgroundResource(R.drawable.ghevipdaban);
                }
                if (seatData.charAt(index) == 'C') {
                    view.setBackgroundResource(R.drawable.ghethuongdaban);
                }
                if (seatData.charAt(index) == 'L') {
                    view.setBackgroundResource(R.drawable.ghedoidaban);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(125, 80);
                    view.setLayoutParams(layoutParams2);
                }
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_BOOKED);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seatData.charAt(index) == 'V' || seatData.charAt(index) == 'N' || seatData.charAt(index) == 'D') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);

                if (seatData.charAt(index) == 'V') {
                    view.setBackgroundResource(R.drawable.ghevip);
                    view.setLayoutParams(layoutParams);
                }
                if (seatData.charAt(index) == 'N') {
                    view.setBackgroundResource(R.drawable.ghethuong);
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(110, 110);
                    view.setLayoutParams(layoutParams1);
                }
                if (seatData.charAt(index) == 'D') {
                    view.setBackgroundResource(R.drawable.ghedoi);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(125, 80);
                    view.setLayoutParams(layoutParams2);
                }
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seatData.charAt(index) == 'F' || seatData.charAt(index) == 'G' || seatData.charAt(index) == 'H') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);


                if (seatSelectionMap.containsKey(count) && seatSelectionMap.get(count)) {
                    if (seatData.charAt(index) == 'F') {
                        view.setBackgroundResource(R.drawable.ghevipdangchon);
                        view.setTag(STATUS_SELECTE);
                    }
                    if (seatData.charAt(index) == 'G') {
                        view.setBackgroundResource(R.drawable.ghethuongdangchon);
                        view.setTag(STATUS_SELECTE);
                    }
                    if (seatData.charAt(index) == 'H') {
                        view.setBackgroundResource(R.drawable.ghedoidangchon);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(125, 80);
                        view.setLayoutParams(layoutParams2);
                        view.setTag(STATUS_SELECTE);
                    }
                } else {
                    if (seatData.charAt(index) == 'F') {
                        view.setBackgroundResource(R.drawable.ghevipdaduocchon);
                        view.setTag(STATUS_RESERVED);
                    }
                    if (seatData.charAt(index) == 'G') {
                        view.setBackgroundResource(R.drawable.ghethuongdaduocchon);
                        view.setTag(STATUS_RESERVED);
                    }
                    if (seatData.charAt(index) == 'H') {
                        view.setBackgroundResource(R.drawable.ghedoidaduocchon);
                        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(125, 80);
                        view.setLayoutParams(layoutParams3);
                        view.setTag(STATUS_RESERVED);
                    }
                }
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.WHITE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seatData.charAt(index) == 'T') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("Lối Vào");
                layout.addView(view);
            } else if (seatData.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int seatId = v.getId();
        idghe = seatId;
        int tien = 0;
        if ((int) v.getTag() == STATUS_AVAILABLE) {
            seatSelectionMap.put(seatId, true);
            if (seatId > 0 && seatId < 23) {
                for (int i = 0; i < capnhat.length(); i++) {
                    tien = 90000;
                    if (i < 11) {
                        if (i == seatId - 1) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'F';
                            String update = new String(newchar);
                            tongtienghe = tongtienghe + tien;
                            ghe.add("A" + seatId);
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else {
                        if (i == seatId) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'F';
                            String update = new String(newchar);
                            ghe.add("B" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    }
                }
            } else if (seatId > 22 && seatId < 65) {
                for (int i = 0; i < capnhat.length(); i++) {
                    tien = 70000;
                    if (i > 22 && i <= 34) {
                        if (i == seatId + 1) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'G';
                            String update = new String(newchar);
                            ghe.add("C" + seatId);

                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (i > 34 && i <= 46) {
                        if (i == seatId + 2) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'G';
                            String update = new String(newchar);
                            ghe.add("D" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (i > 46 && i <= 58) {
                        if (i == seatId + 4) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'G';
                            String update = new String(newchar);
                            ghe.add("E" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (i > 58 && i <= 70) {
                        if (i == seatId + 6) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'G';
                            String update = new String(newchar);
                            ghe.add("F" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < capnhat.length(); i++) {
                    tien = 150000;
                    if (seatId == 65) {
                        if (i == seatId + 8) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'H';
                            String update = new String(newchar);
                            ghe.add("G" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 66) {
                        if (i == seatId + 9) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'H';
                            String update = new String(newchar);
                            ghe.add("G" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 67) {
                        if (i == seatId + 10) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'H';
                            String update = new String(newchar);
                            ghe.add("G" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 68) {
                        if (i == seatId + 11) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'H';
                            String update = new String(newchar);
                            ghe.add("G" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 69) {
                        if (i == seatId + 12) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'H';
                            String update = new String(newchar);
                            ghe.add("G" + seatId);
                            tongtienghe = tongtienghe + tien;
                            SaveSeatsSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    }
                }
            }
        } else if ((int) v.getTag() == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + seatId + " is Booked", Toast.LENGTH_SHORT).show();
        } else if ((int) v.getTag() == STATUS_RESERVED) {
            // Đã chọn ghế trước đó, không thay đổi gì cả
            Toast.makeText(this, "Seat " + seatId + " is Reserved", Toast.LENGTH_SHORT).show();
        } else if ((int) v.getTag() == STATUS_SELECTE) {
            // Nếu ghế đã được chọn, hủy chọn nó
//            selectedIds = selectedIds.replace(seatId + ",", "");
            // Thay đổi hình nền ghế đã chọn
            seatSelectionMap.put(seatId, true);
            if (seatId > 0 && seatId < 23) {
                for (int i = 0; i < capnhat.length(); i++) {
                    tien = 90000;
                    if (i < 11) {
                        if (i == seatId - 1) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'V';
                            String update = new String(newchar);
                            ghe.remove("A" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else {
                        if (i == seatId) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'V';
                            String update = new String(newchar);
                            ghe.remove("B" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    }
                }
            } else if (seatId > 22 && seatId < 65) {
                for (int i = 0; i < capnhat.length(); i++) {
                    tien = 70000;
                    if (i > 22 && i <= 34) {
                        if (i == seatId + 1) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'N';
                            String update = new String(newchar);
                            ghe.remove("C" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (i > 34 && i <= 46) {
                        if (i == seatId + 2) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'N';
                            String update = new String(newchar);
                            ghe.remove("D" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (i > 46 && i <= 58) {
                        if (i == seatId + 4) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'N';
                            String update = new String(newchar);
                            ghe.remove("E" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (i > 58 && i <= 70) {
                        if (i == seatId + 6) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'N';
                            String update = new String(newchar);
                            ghe.remove("F" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < capnhat.length(); i++) {
                    tien = 150000;
                    if (seatId == 65) {
                        if (i == seatId + 8) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'D';
                            String update = new String(newchar);
                            ghe.remove("G" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 66) {
                        if (i == seatId + 9) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'D';
                            String update = new String(newchar);
                            ghe.remove("G" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 67) {
                        if (i == seatId + 10) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'D';
                            String update = new String(newchar);
                            ghe.remove("G" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 68) {
                        if (i == seatId + 11) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'D';
                            String update = new String(newchar);
                            ghe.remove("G" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    } else if (seatId == 69) {
                        if (i == seatId + 12) {
                            char[] newchar = capnhat.toCharArray();
                            newchar[i] = 'D';
                            String update = new String(newchar);
                            ghe.remove("G" + seatId);
                            tongtienghe -= tien;
                            DeleteSeatSelected(idnguoidung, seatId, idphim, ngaychieuseat, suatchieuseat);
                            updateSeatStatusRealtime(update);
                            break;
                        }
                    }
                }
            }
        }
    }

    void setImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        for(int n=0;n<count;n++) {}
        if (idghe > 0 && idghe < 65) {
            char[] newchar = capnhat.toCharArray();
            for (int i = 0; i < capnhat.length(); i++) {
                if (i < 11) {
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId - 1 && entry.getValue() && capnhat.charAt(i) == 'F') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'V';
                        }
                    }
                }
                else if (i>=11 &&i<=22){
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId && entry.getValue() && capnhat.charAt(i) == 'F') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'V';
                        }
                    }
                }
            // Sau khi cập nhật tất cả các ghế, chuyển mảng ký tự mới thành chuỗi và cập nhật
//            String update = new String(newchar);
//            updateSeatStatusRealtime(update);
//        } else if (idghe > 22 && idghe < 65) {
//            char[] newchar = capnhat.toCharArray();
//            for (int i = 0; i < capnhat.length(); i++) {
                else if (i > 22 && i <= 34) {
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId + 1 && entry.getValue() && capnhat.charAt(i) == 'G') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'N';
                        }
                    }
                } else if (i > 34 && i <= 46) {
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId +2 && entry.getValue() && capnhat.charAt(i) == 'G') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'N';
                        }
                    }
                } else if (i > 46 && i <= 58) {
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId +4 && entry.getValue() && capnhat.charAt(i) == 'G') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'N';
                        }
                    }
                } else if (i > 58 && i <= 70) {
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId +6 && entry.getValue() && capnhat.charAt(i) == 'G') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'N';
                        }
                    }
                }
                if (idghe == 65) {
                    for (Map.Entry<Integer, Boolean> entry : seatSelectionMap.entrySet()) {
                        int selectedSeatId = entry.getKey();
                        if (i == selectedSeatId +8 && entry.getValue() && capnhat.charAt(i) == 'L') {
                            // Nếu i trùng với selectedSeatId, ghế này đang chọn và là 'F', thì cập nhật
                            newchar[i] = 'D';
                        }
                    }
                } else if (idghe == 66) {
                    if (i == idghe + 9) {

                        newchar[i] = 'D';
                        String update = new String(newchar);
                        updateSeatStatusRealtime(update);
                        break;
                    }
                } else if (idghe == 67) {
                    if (i == idghe + 10) {

                        newchar[i] = 'D';
                        String update = new String(newchar);
                        updateSeatStatusRealtime(update);
                        break;
                    }
                } else if (idghe == 68) {
                    if (i == idghe + 11) {

                        newchar[i] = 'D';
                        String update = new String(newchar);
                        updateSeatStatusRealtime(update);
                        break;
                    }
                } else if (idghe == 69) {
                    if (i == idghe + 12) {

                        String update = new String(newchar);
                        updateSeatStatusRealtime(update);
                        break;
                    }
                }
                String update = new String(newchar);
                updateSeatStatusRealtime(update);
            }

        }
        if (check == false) {
            check = true;
            reference = FirebaseDatabase.getInstance().getReference("SeatsSelected").child(idnguoidung);
            reference.removeValue();
        }

        finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Đặt giá trị về rỗng khi thoát activity
        clearData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
    }

    public void clearData() {
        soghe.setText("");
        tongtien.setText("");
    }

    private void updateUI() {
        if (ghe.size() > 0) {
            String textghe = "";
            for (String a : ghe) {
                textghe += "," + a;
                String result = textghe.replaceFirst("^,", "");
                soghe.setText(result);
            }
            tongtien.setText(tongtienghe + "đ");
        } else {
            soghe.setText("");
            tongtien.setText("");
        }
    }
    private void updateSeatStatus(char[] newchar) {
        String update = new String(newchar);
        updateSeatStatusRealtime(update);
    }
}