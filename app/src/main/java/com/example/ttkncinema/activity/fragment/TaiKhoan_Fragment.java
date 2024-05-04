package com.example.ttkncinema.activity.fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.Login_Activity;
import com.example.ttkncinema.activity.activity.taikhoan.ChinhsachbaomatActivity;
import com.example.ttkncinema.activity.activity.taikhoan.DieukhoansudungActivity;
import com.example.ttkncinema.activity.activity.taikhoan.DoimatkhauActivity;
import com.example.ttkncinema.activity.activity.taikhoan.ThongTindoanhnghiepActivity;
import com.example.ttkncinema.activity.activity.taikhoan.ThongtincanhanActivity;
import com.example.ttkncinema.activity.activity.taikhoan.VedamuaActivity;
import com.example.ttkncinema.activity.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaiKhoan_Fragment extends Fragment {
    LinearLayout vedamua, thongtincanhan, doimatkhau, dienkhoansudung, chinhsachbaomat, Thongtindoanhnghiep, dangxuat, xoataikhoan, hotline, zalo;
    private String userRegister = "";
    User user;
    private String deviceId;

    public TaiKhoan_Fragment(String userRegister) {
        this.userRegister = userRegister;
    }

    public TaiKhoan_Fragment() {
        // Empty public constructor
    }

    public static TaiKhoan_Fragment newInstance(String param1, String param2) {
        TaiKhoan_Fragment fragment = new TaiKhoan_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tai_khoan_, container, false);

        // Ánh xạ các thành phần giao diện
        vedamua = view.findViewById(R.id.vedamua);
        thongtincanhan = view.findViewById(R.id.Thongtincanhan);
        doimatkhau = view.findViewById(R.id.doimatkhau);
        dienkhoansudung = view.findViewById(R.id.dienkhoansudung);
        chinhsachbaomat = view.findViewById(R.id.chinhsachbaomat);
        Thongtindoanhnghiep = view.findViewById(R.id.Thongtindoanhnghiep);
        dangxuat = view.findViewById(R.id.dangxuat);
        hotline = view.findViewById(R.id.hotline);
        xoataikhoan = view.findViewById(R.id.xoataikhoan);
        zalo = view.findViewById(R.id.zalo);

        // Bắt sự kiện khi click vào nút Zalo để mở ứng dụng Zalo
        zalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zaloUrl = "https://zalo.me/g/pkqhzz117";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(zaloUrl));
                startActivity(intent);
            }
        });

        // Bắt sự kiện khi click vào nút Hotline để gọi điện thoại
        hotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:" + "0333845232";
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                startActivity(dial);
            }
        });

        // Bắt sự kiện khi click vào nút "Vé đã mua" để mở màn hình vé đã mua
        vedamua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VedamuaActivity.class));
            }
        });

        // Bắt sự kiện khi click vào nút "Thông tin cá nhân" để mở màn hình thông tin cá nhân
        thongtincanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ThongtincanhanActivity.class));
            }
        });

        // Bắt sự kiện khi click vào nút "Đổi mật khẩu" để mở màn hình đổi mật khẩu
        doimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DoimatkhauActivity.class));
            }
        });

        // Bắt sự kiện khi click vào nút "Điều khoản sử dụng" để mở màn hình điều khoản sử dụng
        dienkhoansudung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DieukhoansudungActivity.class));
            }
        });

        // Bắt sự kiện khi click vào nút "Chính sách bảo mật" để mở màn hình chính sách bảo mật
        chinhsachbaomat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChinhsachbaomatActivity.class));
            }
        });

        // Bắt sự kiện khi click vào nút "Thông tin doanh nghiệp" để mở màn hình thông tin doanh nghiệp
        Thongtindoanhnghiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ThongTindoanhnghiepActivity.class));
            }
        });

        // Bắt sự kiện khi click vào nút "Đăng xuất"
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở dialog xác nhận đăng xuất
                DailogDangxuat();
                // Log thông báo đăng xuất vào console
                Log.d("TAG", "Đăng xuất người dùng: " + userRegister);
                // Lưu thông tin thiết bị xuống Firebase Realtime Database
                saveDeviceInformation(deviceId, false);
            }
        });

        // Bắt sự kiện khi click vào nút "Xóa tài khoản"
        xoataikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở dialog xác nhận xóa tài khoản
                DailogXoaTaiKhoan();
            }
        });

        return view;
    }

    // Phương thức hiển thị dialog xác nhận xóa tài khoản
    public void DailogXoaTaiKhoan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dailogxoataikhoan, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        Button dong, xoa;

        dong = view.findViewById(R.id.bntdong);
        xoa = view.findViewById(R.id.btnxoa);

        // Bắt sự kiện khi click vào nút "Đóng"
        dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        // Bắt sự kiện khi click vào nút "Xóa"
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý xóa tài khoản (chưa cung cấp mã nguồn xử lý)
            }
        });
    }

    // Phương thức hiển thị dialog xác nhận đăng xuất
    public void DailogDangxuat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dailogdangxuat, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        Button dong, dangxuat;

        dong = view.findViewById(R.id.bntdong);
        dangxuat = view.findViewById(R.id.btndangxuat);

        // Bắt sự kiện khi click vào nút "Đóng"
        dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        // Bắt sự kiện khi click vào nút "Đăng xuất"
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở màn hình đăng nhập
                startActivity(new Intent(getActivity(), Login_Activity.class));
                // Đóng dialog
                alertDialog.dismiss();
            }
        });
    }

    // Phương thức lưu thông tin thiết bị xuống Firebase Realtime Database
    private void saveDeviceInformation(String username, boolean isLoggedIn) {
        DatabaseReference devicesRef = FirebaseDatabase.getInstance().getReference("devices");

        // Truy vấn và lưu trạng thái đăng nhập của thiết bị
        devicesRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                        String deviceId = deviceSnapshot.getKey();
                        devicesRef.child(deviceId).child("isLoggedIn").setValue(isLoggedIn);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}
