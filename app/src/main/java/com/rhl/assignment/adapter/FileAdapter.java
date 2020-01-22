package com.rhl.assignment.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rhl.assignment.R;
import com.rhl.assignment.bean.FileBean;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private String CAMERA_IMG_PATH = Environment.getExternalStorageDirectory() + "/" + "Assignment" + "/img/%s.jpg";
    private Context mContext;
    private List<FileBean> mData;

    public FileAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();

    }

    public void addAll(List<FileBean> mData) {
        this.mData.clear();
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(String.format(CAMERA_IMG_PATH, mData.get(position).getFileName())).into(holder.ivPoster);
        holder.tvLat.setText(String.format(mContext.getResources().getString(R.string.lat_format), String.valueOf(mData.get(position).getLat())));
        holder.tvLongi.setText(String.format(mContext.getResources().getString(R.string.long_format), String.valueOf(mData.get(position).getLongi())));
        holder.tvFileName.setText(String.format(mContext.getResources().getString(R.string.filename_format), mData.get(position).getFileName()));

    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvLat, tvLongi, tvFileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvLat = itemView.findViewById(R.id.tv_lat);
            tvLongi = itemView.findViewById(R.id.tv_longi);
            tvFileName = itemView.findViewById(R.id.tv_filename);
        }
    }
}
