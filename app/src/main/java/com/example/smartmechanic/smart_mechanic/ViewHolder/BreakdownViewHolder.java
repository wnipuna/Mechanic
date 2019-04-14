package com.example.smartmechanic.smart_mechanic.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartmechanic.smart_mechanic.Interface.ItemClickListener;
import com.example.smartmechanic.smart_mechanic.R;

public class BreakdownViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView reqModel,reqvehicleNo,reqDescription,reqLocation,reqType,reqTransType,reqFuelType;
    public ImageView reqImage;

    private ItemClickListener itemClickListener;

    public BreakdownViewHolder(@NonNull View itemView) {
        super(itemView);
        reqModel=(TextView)itemView.findViewById(R.id.editrModel);
        //reqType=(TextView)itemView.findViewById(R.id.editrType);
        reqFuelType=(TextView)itemView.findViewById(R.id.editrFuelType);
        //reqTransType=(TextView)itemView.findViewById(R.id.editrTransType);
        reqDescription=(TextView)itemView.findViewById(R.id.editrDescription);
        reqvehicleNo=(TextView)itemView.findViewById(R.id.editrNumber);
        //reqLocation=(TextView)itemView.findViewById(R.id.editrAddress);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }
}
