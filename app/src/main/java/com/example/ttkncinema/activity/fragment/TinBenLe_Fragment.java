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
import com.example.ttkncinema.activity.activity.taikhoan.KhuyenMaiChiTietActivity;
import com.example.ttkncinema.activity.adapter.TinTucAdapter;
import com.example.ttkncinema.activity.adapter.Upcomingadapter;
import com.example.ttkncinema.activity.model.TinTuc;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TinBenLe_Fragment extends Fragment {
    RecyclerView recyclerkhuyenmai;
    List<TinTuc> list;
    TinTucAdapter adapter;
    TinTucAdapter.OnItemClickListener clickListener;

    public TinBenLe_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tinbenle_fragment, container, false);
        recyclerkhuyenmai = view.findViewById(R.id.recyclerviewtinbenle);
        list = new ArrayList<>();
        recyclerkhuyenmai.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new TinTucAdapter(list, (TinTucAdapter.OnItemClickListener) clickListener);

        FirebaseDatabase.getInstance().getReference("TinBenLe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    TinTuc thongBao = movieSnapshot.getValue(TinTuc.class);
                    list.add(thongBao);
                }
                // Dừng hiệu ứng shimmer và hiển thị RecyclerView
                ShimmerFrameLayout shimmerLayout = view.findViewById(R.id.shimmer_layouttinbenle);
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                adapter = new TinTucAdapter(list, new TinTucAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TinTuc tinTucThongBao) {
                        Intent intent = new Intent(getActivity(), KhuyenMaiChiTietActivity.class);
                        intent.putExtra("tentt", tinTucThongBao.getText());
                        intent.putExtra("imagett", tinTucThongBao.getImage());
                        intent.putExtra("chitiettt", tinTucThongBao.getChitiettext());
                        startActivity(intent);
                    }
                });
                recyclerkhuyenmai.setAdapter(adapter);
                recyclerkhuyenmai.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
