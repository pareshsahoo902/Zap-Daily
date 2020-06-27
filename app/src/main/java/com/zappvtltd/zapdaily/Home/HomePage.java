package com.zappvtltd.zapdaily.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.LoginActivity;
import com.zappvtltd.zapdaily.MainActivity;
import com.zappvtltd.zapdaily.R;

public class HomePage extends AppCompatActivity {

    private Button signout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        signout =(Button)findViewById(R.id.signout);
        mAuth=FirebaseAuth.getInstance();

//        final AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(HomePage.this, "Please wait..");
//        dialog.show();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(HomePage.this, "Please wait..");
                dialog.show();
                FirebaseAuth.getInstance().signOut();
                dialog.dismiss();
                startActivity(new Intent(HomePage.this, LoginActivity.class));

            }
        });
    }
}