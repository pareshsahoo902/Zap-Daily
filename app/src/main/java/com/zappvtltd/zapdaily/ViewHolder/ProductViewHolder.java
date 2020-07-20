package com.zappvtltd.zapdaily.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zappvtltd.zapdaily.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView image,add;
    public TextView p_name,p_price;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        image=(ImageView)itemView.findViewById(R.id.pimageview);
        add=(ImageView)itemView.findViewById(R.id.addcart);
        p_name=(TextView) itemView.findViewById(R.id.pnametext);
        p_price=(TextView) itemView.findViewById(R.id.ppricetext);
    }
}
