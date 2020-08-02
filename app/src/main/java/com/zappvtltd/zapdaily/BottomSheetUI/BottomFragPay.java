package com.zappvtltd.zapdaily.BottomSheetUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zappvtltd.zapdaily.ConfirmPayment;
import com.zappvtltd.zapdaily.PaymentActivity;
import com.zappvtltd.zapdaily.R;


public class BottomFragPay extends Fragment {
    String product_id;
    TextView userAddress;
    CardView pay;
    private DatabaseReference reference;
    private String totalamount;
    private ImageView imagepic;
    private TextView pricebill;

    public BottomFragPay() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bottom_frag_pay, container, false);
        Bundle bundle =this.getArguments();
        product_id =bundle.getString("pid");
        userAddress=view.findViewById(R.id.userad);
        pay=view.findViewById(R.id.Card_PAY);
        imagepic=view.findViewById(R.id.billimage);
        pricebill=view.findViewById(R.id.billpprice);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int currentBalance=Integer.parseInt(snapshot.child("wallet_balance").getValue().toString());
                            if (currentBalance>=Integer.parseInt(totalamount)){
                                startActivity(new Intent(getActivity(), ConfirmPayment.class).putExtra("totalamount",totalamount)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                            else {
                                startActivity(new Intent(getActivity(), PaymentActivity.class).putExtra("totalamount",totalamount)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        // Inflate the layout for this fragment
        fillUserDetails();
        return view;
    }

    private void fillUserDetails() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address=snapshot.child("address").getValue().toString();
                String pincode=snapshot.child("pincode").getValue().toString();
                userAddress.setText(address+"\n"+pincode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("products").child(product_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image=snapshot.child("image1").getValue().toString();
                    String price= snapshot.child("product_price").getValue().toString();
                    Picasso.with(getContext()).load(image).into(imagepic);
                    pricebill.setText("Price :"+"₹"+price);
                    totalamount=price;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}