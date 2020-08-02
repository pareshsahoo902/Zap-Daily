package com.zappvtltd.zapdaily;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ConfirmPayment extends AppCompatActivity {

    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        price=getIntent().getStringExtra("totalamount");
    }
}