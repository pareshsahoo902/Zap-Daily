package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;

import java.util.HashMap;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class EditProfile extends AppCompatActivity {
    private EditText name,email,address,pincode;
    private Spinner spinner;
    private ImageView profile_pic, settings, back;
    private FloatingActionButton fab_btn;
    private CardView save_btn;
    private static final int TAKE_PICTURE1 = 111;

    private DatabaseReference userReference;
    String pic_url="",gender;
    private StorageTask uploadtask;
    private StorageReference userStorage;
    Uri SelectedPhotoFilePath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            loadUserInfoFromFirebase();
        }

        name =(EditText)findViewById(R.id.Edit_Name);
        email =(EditText)findViewById(R.id.Edit_Email);
        address =(EditText)findViewById(R.id.Edit_Address);
        pincode =(EditText)findViewById(R.id.Edit_Pincode);
        profile_pic =(ImageView) findViewById(R.id.imageview_account_profile);
        settings =(ImageView) findViewById(R.id.settins_profile);
        back =(ImageView) findViewById(R.id.BackChal);

        save_btn =(CardView) findViewById(R.id.Card_Save);
        fab_btn=(FloatingActionButton)findViewById(R.id.floatingActionButton);

        spinner = (Spinner) findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        userStorage= FirebaseStorage.getInstance().getReference().child("Customer Profile pic");





        PushDownAnim.setPushDownAnimTo(fab_btn, save_btn, settings, back)
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

                            case R.id.Card_Save:
                                validateUser();

                                break;

                            case R.id.floatingActionButton:
                                addProfilePic();
                                break;

                            case R.id.settins_profile:

                            case R.id.BackChal:
                                finish();
                                break;



                        }

                    }
                });
    }

    private void loadUserInfoFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("address")  && snapshot.hasChild("Profilepic_URL") && snapshot.hasChild("pincode") && snapshot.hasChild("email") && snapshot.hasChild("gender")){
                    String username =snapshot.child("name").getValue().toString();
                    String useremail =snapshot.child("email").getValue().toString();
                    String useraddress =snapshot.child("address").getValue().toString();
                    String userpincode =snapshot.child("pincode").getValue().toString();
                    String picUrl =snapshot.child("Profilepic_URL").getValue().toString();

                    name.setText(username);
                    email.setText(useremail);
                    address.setText(useraddress);
                    pincode.setText(userpincode);

                    Picasso.with(getApplicationContext()).load(picUrl).into(profile_pic);
                }
                else {
                    String username =snapshot.child("name").getValue().toString();

                    name.setText(username);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void addProfilePic() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_PICTURE1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();

                CropImage.activity(uri)
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(this);
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                SelectedPhotoFilePath = result.getUri();

                Glide.with(getApplicationContext())
                        .load(SelectedPhotoFilePath)
                        .into(profile_pic);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void validateUser() {

        String u_name=name.getText().toString();
        String u_email=email.getText().toString();
        String u_address=address.getText().toString();
        String u_pincode=pincode.getText().toString();


        if(TextUtils.isEmpty(u_name))
        {
            Toast.makeText(getApplicationContext(),"enter all fields",Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(u_email))
        {
            Toast.makeText(getApplicationContext(),"enter all fields",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(u_address))
        {
            Toast.makeText(getApplicationContext(),"enter all fields",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(u_pincode))
        {
            Toast.makeText(getApplicationContext(),"enter all fields",Toast.LENGTH_SHORT).show();
        }
        else {
            updateUserInfoToFirebase();
        }



    }

    private void updateUserInfoToFirebase() {
        if (SelectedPhotoFilePath != null){
            updateUserProfilePic();
//            Toast.makeText(getApplicationContext(),"Please select your photo"+FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();

        }
        else {
            updateUserDataOnly();
            //write logic for updating without pic
        }




    }

    private void updateUserDataOnly() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Updating Profile..");
        dialog.show();
        gender = spinner.getSelectedItem().toString();

        userReference = FirebaseDatabase.getInstance().getReference().child("users");

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("email",email.getText().toString().trim());
        hashMap.put("name",name.getText().toString());
        hashMap.put("address",address.getText().toString());
        hashMap.put("gender",gender);
        hashMap.put("pincode",pincode.getText().toString().trim());

        userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);

        dialog.dismiss();
        Toast.makeText(getApplicationContext(), "Profile Updated !", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void updateUserProfilePic() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Updating Profile..");
        dialog.show();

        final StorageReference filepath = userStorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
        uploadtask=filepath.putFile(SelectedPhotoFilePath);

        uploadtask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadurl =task.getResult();
                    pic_url=downloadurl.toString();

                    gender = spinner.getSelectedItem().toString();

                    userReference = FirebaseDatabase.getInstance().getReference().child("users");

                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("email",email.getText().toString().trim());
                    hashMap.put("name",name.getText().toString());
                    hashMap.put("address",address.getText().toString());
                    hashMap.put("gender",gender);
                    hashMap.put("pincode",pincode.getText().toString().trim());
                    hashMap.put("Profilepic_URL",pic_url);

                    userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Profile Updated !", Toast.LENGTH_SHORT).show();

                    finish();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Uploading Picture.. Try again", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


}
