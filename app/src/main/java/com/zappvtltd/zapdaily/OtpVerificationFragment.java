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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Home.HomePage;

import java.util.concurrent.Executor;

public class OtpVerificationFragment extends AppCompatDialogFragment {
    private PinView pinView;
    private CardView verify;
    private TextView resendOtp;
    private FirebaseAuth mAuth;
    String OTP,mVerificationId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Bundle bundle =this.getArguments();
        mVerificationId =bundle.getString("verificationId");
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

        builder.create().setCanceledOnTouchOutside(false);

        return builder.create();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(getActivity(), "Verifying Otp..");
        dialog.show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(getActivity(),HomePage.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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


}
