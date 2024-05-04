package com.example.ttkncinema.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttkncinema.R;
import com.example.ttkncinema.activity.model.TinTuc;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TinTucAdapter extends RecyclerView.Adapter<TinTucAdapter.ViewHolder>{
    private List<TinTuc> list;
    private OnItemClickListener listener;

    public TinTucAdapter(List<TinTuc> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TinTuc tinTucThongBao);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtintuc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinTuc tinTucThongBao = list.get(position);
        holder.setImage(tinTucThongBao.getImage());
        holder.textthongbao.setText(tinTucThongBao.getText());
        holder.bind(tinTucThongBao);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView textthongbao;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagetintucthongbao);
            textthongbao = itemView.findViewById(R.id.texttintuc);

        }
        void setImage(String imageUrl) {
            Picasso.get().load(imageUrl).into(image);
        }
        public void bind(final TinTuc tinTucThongBao) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(tinTucThongBao);
                }
            });
        }

    }
}
