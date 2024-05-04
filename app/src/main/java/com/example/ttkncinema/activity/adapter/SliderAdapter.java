package com.example.ttkncinema.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<Movie> list;
    private Context context;

    public SliderAdapter(List<Movie> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemphim, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Movie movie = list.get(position);
        if (movie != null) {
            holder.setImage(movie.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.anhslide);
        }
        void setImage(String imageUrl) {
            Picasso.get().load(imageUrl).into(imageView);
        }
    }
}
