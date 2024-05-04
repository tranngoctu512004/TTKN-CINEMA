package com.example.ttkncinema.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.time.setText(movie.getTime());
        Glide.with(holder.itemView.getContext())
                .load(movie.getImage()) // Đường link hình ảnh từ Firebase
                .placeholder(R.drawable.a2)
                .override(2500,2000)// Đặt hình ảnh placeholder nếu có
                .into(holder.imgItem);

//         Thêm các nút thời gian vào LinearLayout
        for (String time : movie.getTimes("date1")) {
            Button button = new Button(holder.itemView.getContext());
            button.setText(time);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            holder.timesLayout.addView(button);
        }



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView title;
        TextView time;
        LinearLayout timesLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            timesLayout = itemView.findViewById(R.id.timesLayout);
        }
    }
}
