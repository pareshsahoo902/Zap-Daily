package com.zappvtltd.zapdaily.DrawerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.EditProfile;
import com.zappvtltd.zapdaily.R;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class Account extends AppCompatActivity {

    ImageView back,settings,userProfileImage;
    CardView edit_profile;
    TextView username,userphone,userAddress,userEmail;
    FirebaseAuth mAuth;
    DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        back = (ImageView) findViewById(R.id.BackChal);
        settings = (ImageView) findViewById(R.id.settings_account);
        userProfileImage = (ImageView) findViewById(R.id.account_profile_image);
        edit_profile=(CardView) findViewById(R.id.edit_profile_button);
        username=(TextView) findViewById(R.id.user_name_text);
        userphone=(TextView) findViewById(R.id.user_phonenumber);
        userAddress=(TextView) findViewById(R.id.account_address);
        userEmail=(TextView) findViewById(R.id.account_email);

        LoadUserInfo();


        PushDownAnim.setPushDownAnimTo(edit_profile,back,settings)
                .setScale(MODE_SCALE,
                        PushDownAnim.DEFAULT_PUSH_SCALE)

                .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        switch (v.getId()) {

                            case R.id.settings_account:
                            case R.id.BackChal:
                                finish();
                                break;

                            case R.id.edit_profile_button:
                                Intent intent = new Intent(Account.this, EditProfile.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });

    }

    private void LoadUserInfo() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Loading...");
        dialog.show();
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getInstance().getCurrentUser().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("address") && snapshot.hasChild("Profilepic_URL") && snapshot.hasChild("pincode") && snapshot.hasChild("email") && snapshot.hasChild("gender")){
                    String displayphone =snapshot.child("phone_number").getValue().toString();
                    String displayname =snapshot.child("name").getValue().toString();
                    String displayadress =snapshot.child("address").getValue().toString();
                    String displaypincode =snapshot.child("pincode").getValue().toString();
                    String displayemail =snapshot.child("email").getValue().toString();
                    String picUrl =snapshot.child("Profilepic_URL").getValue().toString();

                    Picasso.with(getApplicationContext()).load(picUrl).into(userProfileImage);
                    username.setText(displayname);
                    userphone.setText("+91-"+displayphone);
                    userAddress.setText(displayadress+"\nPincode: "+displaypincode+"\n ");
                    userEmail.setText(displayemail);
                    dialog.dismiss();
                }
                else {
                    String displayphone =snapshot.child("phone_number").getValue().toString();
                    String displayname =snapshot.child("name").getValue().toString();

                    username.setText(displayname);
                    userphone.setText("+91-"+displayphone);
                    dialog.dismiss();
                    Toast.makeText(Account.this, "Edit Profile Details", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();

            }
        });

    }
}