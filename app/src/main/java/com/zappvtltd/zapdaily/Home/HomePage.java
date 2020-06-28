package com.zappvtltd.zapdaily.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.LoginActivity;
import com.zappvtltd.zapdaily.MainActivity;
import com.zappvtltd.zapdaily.R;

public class HomePage extends AppCompatActivity {

    private ImageView signout;
    FirebaseAuth mAuth;
    DrawerLayout dl;
    NavigationView nv;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        signout =(ImageView)findViewById(R.id.NotificationButton);
        menu =(ImageView)findViewById(R.id.MenuButton);
        mAuth=FirebaseAuth.getInstance();
        dl = findViewById(R.id.dl);
        nv = findViewById(R.id.nv);
        View v =nv.getHeaderView(0);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });


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