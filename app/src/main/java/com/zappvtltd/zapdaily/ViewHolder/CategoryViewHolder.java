package com.zappvtltd.zapdaily.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zappvtltd.zapdaily.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView categoryname;
    public ImageView categoryicon;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryicon=(ImageView) itemView.findViewById(R.id.categoryicon);
        categoryname=(TextView) itemView.findViewById(R.id.categorytext);

    }
}
