package com.zappvtltd.zapdaily.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zappvtltd.zapdaily.R;

public class GridProductViewHolder extends RecyclerView.ViewHolder {

    public ImageView image,add;
    public TextView p_name,p_price;

    public GridProductViewHolder(@NonNull View itemView) {
        super(itemView);

        image=(ImageView)itemView.findViewById(R.id.gridimageview);
        add=(ImageView)itemView.findViewById(R.id.gridaddcart);
        p_name=(TextView) itemView.findViewById(R.id.gridnametext);
        p_price=(TextView) itemView.findViewById(R.id.gridpricetext);
    }
}
