package com.example.ttkncinema.activity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class TinTuc_Fragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentAdapter fragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tin_tuc_,container,false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);

        fragmentAdapter = new FragmentAdapter(getActivity());
        viewPager.setAdapter(fragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("KHUYẾN MÃI LỚN");
                        tabLayout.setBackgroundResource(R.drawable.tabbackground);
                        break;
                    case 1:
                        tab.setText("TIN BÊN LỀ");
                        break;
                }
            }
        }).attach();
        return view;
    }
}