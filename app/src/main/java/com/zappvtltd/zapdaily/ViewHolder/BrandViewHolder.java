package com.zappvtltd.zapdaily.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zappvtltd.zapdaily.R;

public class BrandViewHolder extends RecyclerView.ViewHolder {
    public ImageView logo;
    public TextView name;
    public BrandViewHolder(@NonNull View itemView) {
        super(itemView);

        logo=(ImageView)itemView.findViewById(R.id.brand_logo);
        name=(TextView)itemView.findViewById(R.id.namebrand);

    }
}
