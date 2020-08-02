package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Home.HomePage;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth =FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                authenticateUser();
                finish();
            }
        }, 3000);

    }


    private void authenticateUser() {
        final AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(MainActivity.this, "Logging In..");
        dialog.show();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            dialog.dismiss();
            startActivity(new Intent(MainActivity.this,HomePage.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            overridePendingTransition(R.anim.splashscreen_transiton_in,R.anim.splashscrren_transition_out);
            finish();

        }else {
            dialog.dismiss();
            startActivity(new Intent(MainActivity.this,LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            overridePendingTransition(R.anim.splashscreen_transiton_in,R.anim.splashscrren_transition_out);
            finish();

        }
    }


}