package com.example.flightapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flightapp.Interface.ItemClickListener;
import com.example.flightapp.R;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtDestinationName;
    public ImageView imageView;


    private ItemClickListener itemClickListener;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        txtDestinationName = (TextView) itemView.findViewById(R.id.destination_name);
        imageView = (ImageView) itemView.findViewById(R.id.destination_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(itemView,getAdapterPosition(),false);
    }
}
