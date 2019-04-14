package com.example.smartmechanic.smart_mechanic.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartmechanic.smart_mechanic.Interface.ItemClickListener;
import com.example.smartmechanic.smart_mechanic.R;

public class PartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView part_name;
    public ImageView part_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PartViewHolder(@NonNull View itemView) {
        super(itemView);

        part_name = (TextView)itemView.findViewById(R.id.part_name);
        part_image = (ImageView)itemView.findViewById(R.id.part_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
