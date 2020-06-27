package com.zappvtltd.zapdaily.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.zappvtltd.zapdaily.LoginActivity;
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
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePage.this, LoginActivity.class));

            }
        });
    }
}