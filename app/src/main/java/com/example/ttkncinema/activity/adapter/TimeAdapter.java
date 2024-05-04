package com.example.ttkncinema.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.Movie;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    List<Movie> listmovie;

    // Constructor để nhận danh sách phim
    public TimeAdapter(List<Movie> listmovie) {
        this.listmovie = listmovie;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy thời gian chiếu tại vị trí hiện tại trong danh sách

        // Hiển thị thời gian chiếu trong TextView
    }

    @Override
    public int getItemCount() {
        return listmovie.size();
    }

    // Cập nhật danh sách phim khi có sự thay đổi
    public void setMovies(List<Movie> movies) {
        this.listmovie = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvtime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtime = itemView.findViewById(R.id.episode_text_view);
        }
    }
}
