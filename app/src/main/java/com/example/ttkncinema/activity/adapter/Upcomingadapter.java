package com.example.ttkncinema.activity.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Upcomingadapter extends RecyclerView.Adapter<Upcomingadapter.UpcomingViewHolder> {
    private List<Movie> listslide;
    private OnItemClickListener listener;

    public Upcomingadapter(List<Movie> listslide, OnItemClickListener listener) {
        this.listslide = listslide;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    @NonNull
    @Override
    public Upcomingadapter.UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shimmer_item, parent, false);
        return new UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Upcomingadapter.UpcomingViewHolder holder, int position) {
        Movie movie = listslide.get(position);
        if (movie != null) {
            holder.setImage(movie.getImage());
            holder.titleShin.setText(movie.getTitle());
//            holder.ngaychieu.setText(movie.getDate1());
            holder.bind(movie);
        } else {
            Log.e("Upcomingadapter", "Movie at position " + position + " is null");
        }

    }

    @Override
    public int getItemCount() {
        return listslide.size();
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder {
        ImageView imagephim;
        TextView ngaychieu, thoigian,titleShin;

        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            imagephim = itemView.findViewById(R.id.image_shimmer);
            thoigian = itemView.findViewById(R.id.tvthoigianshimmer);
            ngaychieu = itemView.findViewById(R.id.tvngaychieushimmer);
            titleShin=itemView.findViewById(R.id.titleShin);
        }

        void setImage(String imageUrl) {
            Picasso.get().load(imageUrl).into(imagephim);
        }

        public void bind(final Movie movie) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }
}
