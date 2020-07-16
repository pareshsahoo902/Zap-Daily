package com.zappvtltd.zapdaily;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

import com.dpizarro.pinview.library.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Home.HomePage;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class OtpVerificationFragment extends AppCompatDialogFragment {
    private PinView pinView;
    private CardView verify;
    private TextView resendOtp;
    private FirebaseAuth mAuth;
    String OTP,mVerificationId, username , phone_no;
    static AlertDialog alertDialog;
    private DatabaseReference databaseReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Bundle bundle =this.getArguments();
        mVerificationId =bundle.getString("verificationId");
        username =bundle.getString("name");
        phone_no =bundle.getString("phone");
        View view = inflater.inflate(R.layout.otp_fragment,null);

        builder.setView(view);
        builder.setCancelable(false);


        mAuth =FirebaseAuth.getInstance();
        pinView= view.findViewById(R.id.pinview);
        verify= view.findViewById(R.id.verifyotp);
        resendOtp= view.findViewById(R.id.resendotp);

        pinView.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, final String pinResults) {
                //Do what you want
                if (completed) {
                    OTP =pinResults;
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, OTP);
                signInWithPhoneAuthCredential(credential);


            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Otp Sent!",Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(getActivity(), "Verifying Otp..");
        dialog.show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            updateUSER();
                            dialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            // ...
                        } else {
                            dialog.dismiss();
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getContext(),"Failed Try again later. ",Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getContext(),"WRONG OTP",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });


    }

    private void updateUSER() {

        databaseReference =FirebaseDatabase.getInstance().getReference();
        final String uid =mAuth.getCurrentUser().getUid();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap= new HashMap<>();
                hashMap.put("name",username);
                hashMap.put("phone_number",phone_no);
                hashMap.put("uid",uid);

                databaseReference.child("users").child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){


                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child("address").exists() && snapshot.child("pincode").exists() && snapshot.child("email").exists() && snapshot.child("gender").exists()){

                                        startActivity(new Intent(getActivity(),HomePage.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Check your network connection", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(),EditProfile.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                        else {
                            Toast.makeText(getActivity(), "Check your network connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
