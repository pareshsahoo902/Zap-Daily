package com.zappvtltd.zapdaily.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zappvtltd.zapdaily.R;


public class WalletViewHolder extends RecyclerView.ViewHolder {

    public TextView details,amount;

    public WalletViewHolder(@NonNull View itemView) {
        super(itemView);

        details=itemView.findViewById(R.id.details);
        amount=itemView.findViewById(R.id.amount);

    }
}
