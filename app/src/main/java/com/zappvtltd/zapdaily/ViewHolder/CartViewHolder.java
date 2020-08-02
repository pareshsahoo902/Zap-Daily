package com.zappvtltd.zapdaily.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zappvtltd.zapdaily.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public ImageView product_image;
    public TextView pname,total_price,quantity;
    public Button remove,save_item;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        pname=itemView.findViewById(R.id.namecart);
        product_image=itemView.findViewById(R.id.cartimage);
        total_price=itemView.findViewById(R.id.cart_total_price);
        quantity=itemView.findViewById(R.id.cartQuantity);
        remove=itemView.findViewById(R.id.remove);
        save_item=itemView.findViewById(R.id.save_item);


    }
}
