package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zappvtltd.zapdaily.Home.HomePage;

public class MainActivity extends AppCompatActivity {
    private Button start;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start= (Button)findViewById(R.id.btn);
        mAuth =FirebaseAuth.getInstance();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });

    }

    private void authenticateUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            startActivity(new Intent(MainActivity.this,HomePage.class));

        }else {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));

        }
    }


}