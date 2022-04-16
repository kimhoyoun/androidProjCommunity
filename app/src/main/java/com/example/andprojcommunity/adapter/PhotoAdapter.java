package com.example.andprojcommunity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andprojcommunity.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private ArrayList<Bitmap> photoList;


    public PhotoAdapter(ArrayList<Bitmap> list){
        photoList = list;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_photo, parent, false);

        PhotoViewHolder viewHolder = new PhotoViewHolder(context, view, photoList, this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

        if(getItemCount()!= 0){
            holder.imgItem.setImageBitmap(photoList.get(position));

        }
    }

    @Override
    public int getItemCount() {
        if(photoList!= null) {
            return photoList.size();
        }else{
            return 0;
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgItem;
        public ImageButton btnImgDelete;

        public PhotoViewHolder(Context context, @NonNull View itemView, ArrayList<Bitmap> itemList, PhotoAdapter adapter) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.photoImg);
            btnImgDelete = itemView.findViewById(R.id.btnphotoDelete);


            btnImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        photoList.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
