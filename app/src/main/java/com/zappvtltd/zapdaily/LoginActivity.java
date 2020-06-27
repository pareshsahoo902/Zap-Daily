package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Home.HomePage;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    CardView login, skip;
    EditText phone_number, name;
    FirebaseAuth mAuth;
    String mVerificationId, otpCode, phone;
    PhoneAuthProvider.ForceResendingToken mResendToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login =(CardView)findViewById(R.id.Card_Login);
        skip =(CardView)findViewById(R.id.Card_SKIP);
        phone_number =(EditText)findViewById(R.id.Edit_Phone);
        name =(EditText)findViewById(R.id.EditName);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = "+91"+phone_number.getText().toString();
                verifyUser();

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,HomePage.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private void verifyUser() {
        final AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(LoginActivity.this, "Sending Otp..");
        dialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplicationContext(),"Invalid Number",Toast.LENGTH_SHORT).show();

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(getApplicationContext(),"Phone Number In use. ",Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        dialog.dismiss();
                        mVerificationId = s;
                        mResendToken = forceResendingToken;
                        openDialog(mVerificationId);
                    }
                }
        );

    }



    private void openDialog(String otpkey) {

        OtpVerificationFragment otpVerificationFragment =new OtpVerificationFragment();
        otpVerificationFragment.show(getSupportFragmentManager(),"verify-otp");
        Bundle bundle =new Bundle();
        bundle.putString("verificationId",otpkey);
        otpVerificationFragment.setArguments(bundle);
    }

//    @Override
//    public void fetchOtp(String fetchedOtp) {
//        otpCode = fetchedOtp;
//
//    }
}