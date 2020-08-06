package com.zappvtltd.zapdaily.DrawerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.squareup.timessquare.CalendarPickerView;
import com.zappvtltd.zapdaily.MainActivity;
import com.zappvtltd.zapdaily.R;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

public class Subscriptions extends AppCompatActivity {

    private ImageView back;
    private CalendarPickerView calendar;
    DatePickerDialog picker;
    private EditText startDate,endDate;
    CardView Vacation;
    ImageView subimage;
    TextView namesub;
    String subid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        subid= getIntent().getStringExtra("subid");


        back= findViewById(R.id.BackChal);
        Vacation=findViewById(R.id.Card_Vacation);
        subimage=findViewById(R.id.subimage);
        namesub=findViewById(R.id.subscription_name);
        startDate=findViewById(R.id.start_date);
        endDate=findViewById(R.id.end_date);
        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        loadSubscriptionDetails();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 7);

        calendar =findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime()).inMode(RANGE)
                .withSelectedDate(today);
        Vacation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVacation();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(startDate);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(endDate);
            }
        });
    }

    private void loadSubscriptionDetails() {
        DatabaseReference subscribersref= FirebaseDatabase.getInstance().getReference();
        subscribersref.child("subscribers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(subid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                String pid=snapshot.child("pid").getValue().toString();
                adddetails(pid);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void adddetails(String pid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        ref.child("products").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name= snapshot.child("product_name").getValue().toString();
                    String image= snapshot.child("image1").getValue().toString();

                    namesub.setText(name);
                    Picasso.with(getApplicationContext()).load(image).into(subimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void startVacation() {
        Toast.makeText(this, "Vacation Has started !\n Please Notify us when you want to resume.", Toast.LENGTH_SHORT).show();
    }

    private void pickDate(final EditText eText) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(Subscriptions.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

}