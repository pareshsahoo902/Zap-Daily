package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.Adapter.ProductImageAdapter;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.DialogHelper.SubscribeBottomSheetDailog;
import com.zappvtltd.zapdaily.DrawerUI.Cart;
import com.zappvtltd.zapdaily.Home.HomePage;
import com.zappvtltd.zapdaily.Models.ProductImageModel;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.ViewHolder.ProductViewHolder;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class ViewProductPage extends AppCompatActivity {
    String pid,brand;
    ElegantNumberButton elegantNumberButton;
    TextView pname,pprice,pdescription,pbrand;
    ViewPager viewPager;
    CardView subscribe,addtocart;
    DotsIndicator dotsIndicator;
    ImageView back;
    RecyclerView productrecycler;
    Button viewall;
    String name,price,image1,quantity;
    ImageView saved;

    private FirebaseRecyclerOptions<ProductModel> productoptions;
    private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> productadapter;
    DatabaseReference productref;

    private List<ProductImageModel> productImageModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_page);

        pid=getIntent().getStringExtra("pid");

        elegantNumberButton=(ElegantNumberButton)findViewById(R.id.elegantNumberButton);

        pbrand=(TextView) findViewById(R.id.brand);
        pname=(TextView) findViewById(R.id.product_name);
        pprice=(TextView) findViewById(R.id.product_price);
        pdescription=(TextView) findViewById(R.id.description);
        subscribe=(CardView)findViewById(R.id.Card_Subscribe);
        saved=findViewById(R.id.save_image);
        addtocart=findViewById(R.id.Card_AddCart);


        productrecycler=(RecyclerView)findViewById(R.id.similarproductrecycler);
        productrecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productrecycler.setHasFixedSize(true);


        viewall=(Button)findViewById(R.id.view_all1);

        viewPager=(ViewPager)findViewById(R.id.viewPager_product);
        dotsIndicator=(DotsIndicator)findViewById(R.id.dots_indicator);


        productImageModelList=new ArrayList<ProductImageModel>();
        productref= FirebaseDatabase.getInstance().getReference().child("products");

//        saved.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference likedref=FirebaseDatabase.getInstance().getReference().child("liked_products")
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pid);
//                likedref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            if (snapshot.child("isLiked").getValue().toString().equals("true")){
//                                dislikeProduct();
//                            }
//                            else {
//                                likeProduct();
//                            }
//                        }
//                        else {
//                            likeProduct();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSubscribeDailog();

            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToCart();
            }
        });

        getProductDetails();

    }

    private void dislikeProduct() {
        DatabaseReference likedref=FirebaseDatabase.getInstance().getReference().child("liked_products");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("isLiked","false");
        likedref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                DatabaseReference savedref=FirebaseDatabase.getInstance().getReference().child("saved_items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                savedref.child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        saved.setImageResource(R.drawable.notsave);
                    }
                });

            }
        });
    }

    private void likeProduct() {
        DatabaseReference saveref=FirebaseDatabase.getInstance().getReference().child("saved_items")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pid",pid);
        hashMap.put("p_name",name);
        hashMap.put("p_price",price);
        hashMap.put("image",image1);

        saveref.child(pid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference likedref=FirebaseDatabase.getInstance().getReference().child("liked_products");
                HashMap<String,Object> likehashMap=new HashMap<>();
                likehashMap.put("isLiked","true");
                likedref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pid)
                        .updateChildren(likehashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        saved.setImageResource(R.drawable.saved);
                    }
                });
            }
        });


    }

    private void addProductToCart() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Loading...");
        dialog.show();
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        String CurrentDate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        String CurrentTime = currenttime.format(calendar.getTime());
        DatabaseReference cartref= FirebaseDatabase.getInstance().getReference().child("carts");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pid",pid);
        hashMap.put("p_name",name);
        hashMap.put("p_price",price);
        hashMap.put("image",image1);
        hashMap.put("quantity",elegantNumberButton.getNumber());
        hashMap.put("time",CurrentTime);
        hashMap.put("date",CurrentDate);

        cartref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pid).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(ViewProductPage.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewProductPage.this, Cart.class));
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(ViewProductPage.this, "Some error occured please try again", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void getSubscribeDailog() {

        SubscribeBottomSheetDailog subscribeBottomSheetDailog = new SubscribeBottomSheetDailog();
        subscribeBottomSheetDailog.show(getSupportFragmentManager(),"subscription");

        Bundle bundle=new Bundle();
        bundle.putString("pname",name);
        bundle.putString("pprice",price);
        bundle.putString("pimage",image1);
        bundle.putString("pquantity",elegantNumberButton.getNumber());
        bundle.putString("pid",pid);
        subscribeBottomSheetDailog.setArguments(bundle);
    }

    ///similar product recyclerview
    private void loadSimilarProducts() {
        productoptions=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(productref.orderByChild("product_brand").startAt(brand),ProductModel.class).build();
        productadapter= new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(productoptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final ProductModel productModel) {
                productViewHolder.p_name.setText(productModel.getProduct_name());
                productViewHolder.p_price.setText("₹" + productModel.getProduct_price());
                Picasso.with(getApplicationContext()).load(productModel.getImage1()).into(productViewHolder.image);

                PushDownAnim.setPushDownAnimTo(productViewHolder.itemView)
                        .setScale(MODE_SCALE,
                                PushDownAnim.DEFAULT_PUSH_SCALE)

                        .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                        .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                        .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                        .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(),ViewProductPage.class).putExtra("pid",productModel.getPid()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(),R.anim.fade_in,R.anim.fade_out);
                                startActivity(intent,options.toBundle());
                            }
                        });
               productViewHolder.add.setVisibility(View.GONE);

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.product_items, parent, false));
            }
        };
        productrecycler.setAdapter(productadapter);
        productadapter.startListening();

    }

    ///////getting the details of product
    private void getProductDetails() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Loading...");
        dialog.show();

        productref.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    name = snapshot.child("product_name").getValue().toString();
                    String description= snapshot.child("product_description").getValue().toString();
                    brand= snapshot.child("product_brand").getValue().toString();
                    price = snapshot.child("product_price").getValue().toString();
                    String category= snapshot.child("product_category").getValue().toString();
                    image1 = snapshot.child("image1").getValue().toString();
                    String image2= snapshot.child("image2").getValue().toString();
                    String image3= snapshot.child("image3").getValue().toString();

                    pname.setText(name);
                    pdescription.setText(description);
                    pprice.setText("₹"+price);
                    pbrand.setText(brand);

                    productImageModelList.add(new ProductImageModel(image1));
                    productImageModelList.add(new ProductImageModel(image2));
                    productImageModelList.add(new ProductImageModel(image3));

                    ProductImageAdapter productImageAdapter =new ProductImageAdapter(productImageModelList,getApplicationContext());
                    viewPager.setAdapter(productImageAdapter);
                    viewPager.setClipToPadding(false);
                    dotsIndicator.setViewPager(viewPager);

                    loadSimilarProducts();

                    dialog.dismiss();
                }
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}