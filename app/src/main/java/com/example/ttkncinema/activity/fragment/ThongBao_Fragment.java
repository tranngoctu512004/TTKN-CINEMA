package com.example.ttkncinema.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.activity.taikhoan.ThongBaoChiTietActivity;
import com.example.ttkncinema.activity.adapter.ThongBaoAdapter;
import com.example.ttkncinema.activity.model.ThongBao;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThongBao_Fragment extends Fragment {
    RecyclerView recyclerthongbao;
    List<ThongBao> list;
    ThongBaoAdapter adapter;
    ThongBaoAdapter.OnItemClickListener clickListener;

    public ThongBao_Fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_bao_, container, false);
        recyclerthongbao = view.findViewById(R.id.recyclerviewthongbao);
        list = new ArrayList<>();
        recyclerthongbao.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new ThongBaoAdapter(list, (ThongBaoAdapter.OnItemClickListener) clickListener);

        FirebaseDatabase.getInstance().getReference("thongbao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    ThongBao thongBao = movieSnapshot.getValue(ThongBao.class);
                    list.add(thongBao);
                }
                // Dừng hiệu ứng shimmer và hiển thị RecyclerView
                ShimmerFrameLayout shimmerLayout = view.findViewById(R.id.shimmer_layoutthongbao);
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                adapter = new ThongBaoAdapter(list, new ThongBaoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ThongBao thongBao) {
                        Intent intent = new Intent(getActivity(), ThongBaoChiTietActivity.class);
                        intent.putExtra("tentb", thongBao.getText());
                        intent.putExtra("imagetb", thongBao.getImage());
                        intent.putExtra("chitiettb", thongBao.getChitiettext());
                        startActivity(intent);
                    }
                });
                recyclerthongbao.setAdapter(adapter);
                recyclerthongbao.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}