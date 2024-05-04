package com.example.ttkncinema.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.ThongBao;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder>{
    private List<ThongBao> list;
    private OnItemClickListener listener;

    public ThongBaoAdapter(List<ThongBao> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ThongBao thongBao);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemthongbao,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBao thongBao = list.get(position);
        holder.setImage(thongBao.getImage());
        holder.textthongbao.setText(thongBao.getText());
        holder.time.setText(thongBao.getTime());
        holder.date.setText(thongBao.getDate());
        holder.bind(thongBao);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView textthongbao,date,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagethongbao);
            textthongbao = itemView.findViewById(R.id.textthongbao);
            date = itemView.findViewById(R.id.textday);
            time = itemView.findViewById(R.id.texttime);

        }
        void setImage(String imageUrl) {
            Picasso.get().load(imageUrl).into(image);
        }
        public void bind(final ThongBao thongBao) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(thongBao);
                }
            });
        }
    }
}
