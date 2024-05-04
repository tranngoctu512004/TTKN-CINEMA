package com.example.ttkncinema.activity.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ttkncinema.activity.fragment.KhuyenMai_Fragment;
import com.example.ttkncinema.activity.fragment.TinBenLe_Fragment;
import com.example.ttkncinema.activity.model.Movie;

import java.util.List;


public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position ==0){
            return new KhuyenMai_Fragment();
        }
        return new TinBenLe_Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
