package com.zappvtltd.zapdaily.BottomSheetUI;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zappvtltd.zapdaily.R;

import java.util.ArrayList;

public class BottomFragSelectAddress extends Fragment {
    String product_id;
    TextView result,address;
    CardView selectbtn;
    EditText pincode;
    String validpincode[]={"751001","765002","751002","751024"};
    String useraddress,userpincode;
    DatabaseReference databaseReference;

    public BottomFragSelectAddress() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_bottom_frag_select_address, container, false);
        Bundle bundle =this.getArguments();
        product_id =bundle.getString("pid");
        result=view.findViewById(R.id.result_location);
        address=view.findViewById(R.id.adrestext);
        selectbtn=view.findViewById(R.id.SelectButton);
        pincode=view.findViewById(R.id.Edit_Location);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getAddressDetails();



        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i=0;i<validpincode.length;i++){
                    if (!pincode.getText().toString().equals(validpincode[i])){
                        result.setVisibility(View.VISIBLE);

                    }else {
                        result.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Delivery Possible! procced Further", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        return view;

    }

    private void getAddressDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    useraddress=snapshot.child("address").getValue().toString();
                    userpincode=snapshot.child("pincode").getValue().toString();
                    address.setText("Current Address :"+useraddress+"\n"+userpincode);
                    pincode.setText(userpincode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}