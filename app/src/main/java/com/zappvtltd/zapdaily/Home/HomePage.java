package com.zappvtltd.zapdaily.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.Adapter.SliderAdapter;
import com.zappvtltd.zapdaily.DialogHelper.LocationDialog;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.DrawerUI.About;
import com.zappvtltd.zapdaily.DrawerUI.Account;
import com.zappvtltd.zapdaily.DrawerUI.Cart;
import com.zappvtltd.zapdaily.DrawerUI.ContactUS;
import com.zappvtltd.zapdaily.DrawerUI.Notification;
import com.zappvtltd.zapdaily.DrawerUI.SavedItem;
import com.zappvtltd.zapdaily.DrawerUI.SearchActivity;
import com.zappvtltd.zapdaily.DrawerUI.Subscriptions;
import com.zappvtltd.zapdaily.LoginActivity;
import com.zappvtltd.zapdaily.Models.BrandModel;
import com.zappvtltd.zapdaily.Models.CategoryModel;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.Models.SliderModel;
import com.zappvtltd.zapdaily.PaymentActivity;
import com.zappvtltd.zapdaily.R;
import com.zappvtltd.zapdaily.ViewAllPage;
import com.zappvtltd.zapdaily.ViewHolder.BrandViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.CategoryViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.ProductViewHolder;
import com.zappvtltd.zapdaily.ViewProductPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class HomePage extends AppCompatActivity implements android.location.LocationListener {

    private ImageView notification, menu, cart;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    DrawerLayout dl;
    ImageView Image_LoggedInUserProfilePicture;
    NavigationView nv;
    FrameLayout searchBox;
    RecyclerView homepageRecycler;
    LinearLayout locationBox;
    TextView locationText, Text_LoggedInUsername, Text_LoggedInUserPhone;
    FrameLayout NV_home, NV_ContactUs, NV_Wallet, NV_About, NV_Subscriptions, NV_Cart, NV_SavedItems, NV_Account, NV_LogOut;


    private List<SliderModel> sliderModelList;

    private ArrayList<CategoryModel> arrayList;
    private FirebaseRecyclerOptions<CategoryModel> options;
    private FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder> categoryadapter;


    LocationManager locationManager;
    RecyclerView recyclerView;
    private static final int REQUEST_LOCATION = 1;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //setting location
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        // initialize the homepage fields
        notification = (ImageView) findViewById(R.id.NotificationButton);
        menu = (ImageView) findViewById(R.id.MenuButton);
        cart = (ImageView) findViewById(R.id.cartButton);
        locationBox = (LinearLayout) findViewById(R.id.locationbox);
        locationText = (TextView) findViewById(R.id.locationText);
        searchBox = (FrameLayout) findViewById(R.id.Frame_Search);

        mAuth = FirebaseAuth.getInstance();
        dl = findViewById(R.id.dl);
        nv = findViewById(R.id.nv);
        View v = nv.getHeaderView(0);


        recyclerView = findViewById(R.id.Recyclerview_HorizontalCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        loadRecyclerforCategory();

        arrayList = new ArrayList<CategoryModel>();
        //nav fragments
        NV_home = nv.findViewById(R.id.NV_Home);
        NV_Subscriptions = nv.findViewById(R.id.NV_Subscriptions);
        NV_Cart = nv.findViewById(R.id.NV_cart);
        NV_SavedItems = nv.findViewById(R.id.NV_Saved);
        NV_Account = nv.findViewById(R.id.NV_Account);
        NV_ContactUs = nv.findViewById(R.id.NV_contactus);
        NV_Wallet = nv.findViewById(R.id.NV_Wallet);
        NV_About = nv.findViewById(R.id.NV_About);
        NV_LogOut = nv.findViewById(R.id.NV_logout);
        Text_LoggedInUsername = v.findViewById(R.id.user_name);
        Text_LoggedInUserPhone = v.findViewById(R.id.user_phone);
        Image_LoggedInUserProfilePicture = v.findViewById(R.id.propic);


        getUserInfo();
        sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.drawable.banner4));
        sliderModelList.add(new SliderModel(R.drawable.banner5));

        sliderModelList.add(new SliderModel(R.drawable.banner1));
        sliderModelList.add(new SliderModel(R.drawable.banner2));
        sliderModelList.add(new SliderModel(R.drawable.banner3));
        sliderModelList.add(new SliderModel(R.drawable.banner4));
        sliderModelList.add(new SliderModel(R.drawable.banner5));

        sliderModelList.add(new SliderModel(R.drawable.banner1));
        sliderModelList.add(new SliderModel(R.drawable.banner2));
        //////////HOMEPAGE   RECYCLER

        homepageRecycler = (RecyclerView) findViewById(R.id.homepage_recycler);
        homepageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0, sliderModelList));
        homePageModelList.add(new HomePageModel(1));
        homePageModelList.add(new HomePageModel(2));
        homePageModelList.add(new HomePageModel(3));
        homePageModelList.add(new HomePageModel(2));
        homePageModelList.add(new HomePageModel(1));


        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        homepageRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //////////HOMEPAGE   RECYCLER


        PushDownAnim.setPushDownAnimTo(menu, notification, cart, NV_home,
                NV_Subscriptions, NV_Cart, NV_SavedItems, NV_Account, NV_About, NV_ContactUs, NV_Wallet, NV_LogOut,
                searchBox, locationBox)
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

                            case R.id.MenuButton:

                                dl.openDrawer(Gravity.LEFT);

                                break;

                            case R.id.NotificationButton:

                                Intent intent = new Intent(getApplicationContext(), Notification.class);
                                startActivity(intent);
                                break;

                            case R.id.cartButton:

                            case R.id.NV_cart:

                                dl.closeDrawer(Gravity.LEFT);
                                Intent AddBusiness = new Intent(getApplicationContext(), Cart.class);
                                startActivity(AddBusiness);
                                break;


                            case R.id.NV_Home:
                                dl.closeDrawer(Gravity.LEFT);
                                break;

                            case R.id.NV_Subscriptions:

                                dl.closeDrawer(Gravity.LEFT);
                                Intent subscription = new Intent(getApplicationContext(), Subscriptions.class);
                                startActivity(subscription);


                                break;


                            case R.id.NV_Saved:

                                dl.closeDrawer(Gravity.LEFT);
                                Intent saved = new Intent(getApplicationContext(), SavedItem.class);
                                startActivity(saved);


                                break;

                            case R.id.NV_Account:

                                dl.closeDrawer(Gravity.LEFT);
                                Intent account = new Intent(getApplicationContext(), Account.class);
                                startActivity(account);


                                break;

                            case R.id.NV_contactus:

                                dl.closeDrawer(Gravity.LEFT);
                                Intent contactus = new Intent(getApplicationContext(), ContactUS.class);
                                startActivity(contactus);


                                break;

                            case R.id.NV_Wallet:

                                dl.closeDrawer(Gravity.LEFT);
                                Intent wallet = new Intent(getApplicationContext(), PaymentActivity.class);
                                startActivity(wallet);
                                break;


                            case R.id.NV_About:
                                dl.closeDrawer(Gravity.LEFT);
                                Intent about = new Intent(getApplicationContext(), About.class);
                                startActivity(about);
                                break;

                            case R.id.NV_logout:
                                dl.closeDrawer(Gravity.LEFT);
                                final AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(HomePage.this, "Please wait..");
                                dialog.show();
                                FirebaseAuth.getInstance().signOut();
                                dialog.dismiss();
                                startActivity(new Intent(HomePage.this, LoginActivity.class));
                                finish();
                                break;

                            case R.id.Frame_Search:

                                startActivity(new Intent(HomePage.this, SearchActivity.class));

                                break;
                            case R.id.locationbox:
                                openLocationDialog();


                        }
                    }
                });


    }


    private void getUserInfo() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Loading...");
        dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Profilepic_URL")) {
                    String displayphone = snapshot.child("phone_number").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();
                    String picUrl = snapshot.child("Profilepic_URL").getValue().toString();

                    Text_LoggedInUsername.setText(username);
                    Text_LoggedInUserPhone.setText("+91-" + displayphone);
                    Picasso.with(getApplicationContext()).load(picUrl).into(Image_LoggedInUserProfilePicture);
                    dialog.dismiss();
                } else {
                    String displayphone = snapshot.child("phone_number").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();

                    Text_LoggedInUsername.setText(username);
                    Text_LoggedInUserPhone.setText("+91-" + displayphone);
                    dialog.dismiss();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void openLocationDialog() {

        LocationDialog dialog = new LocationDialog();

        dialog.show(getSupportFragmentManager(), "get location");
    }


    //location handling and updating
    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String cityName = addresses.get(0).getLocality();
            String postalcode = addresses.get(0).getPostalCode();
            String locality = addresses.get(0).getSubLocality();
            locationText.setText(locality + ", " + cityName);


        } catch (IOException e) {
            Toast.makeText(this, "error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public void loadRecyclerforCategory() {


        final DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("category");

        options = new FirebaseRecyclerOptions.Builder<CategoryModel>().setQuery(categoryRef, CategoryModel.class).build();

        categoryadapter = new FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i, @NonNull final CategoryModel categoryModel) {

                categoryViewHolder.categoryname.setText(categoryModel.getCategory_name().toLowerCase());
                Picasso.with(getApplicationContext()).load(categoryModel.getIcon_url()).into(categoryViewHolder.categoryicon);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                animation.setDuration(500);
                categoryViewHolder.itemView.setAnimation(animation);

                categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(HomePage.this, ViewAllPage.class)
                                .putExtra("id", categoryRef.getKey());
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(HomePage.this, R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent, options.toBundle());
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CategoryViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.category_items, parent, false));
            }
        };

        recyclerView.setAdapter(categoryadapter);

        categoryadapter.startListening();

    }

}