package com.example.flightapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flightapp.Interface.ItemClickListener;
import com.example.flightapp.R;

public class AirlineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView airline_name;
    public ImageView airline_image,fav_image;
    private ItemClickListener itemClickListener;



    public AirlineViewHolder(@NonNull View itemView) {
        super(itemView);

        airline_name = (TextView) itemView.findViewById(R.id.airlines_name);
        airline_image = (ImageView) itemView.findViewById(R.id.airlines_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(itemView,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
