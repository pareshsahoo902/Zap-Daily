package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.zappvtltd.zapdaily.Adapter.ProductImageAdapter;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Home.HomePage;
import com.zappvtltd.zapdaily.Models.ProductImageModel;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.ViewHolder.ProductViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ViewProductPage extends AppCompatActivity {
    String pid,brand;
    ElegantNumberButton elegantNumberButton;
    TextView add_cart,subscribe,pname,pprice,pdescription,pbrand;
    ViewPager viewPager;
    DotsIndicator dotsIndicator;
    ImageView back;
    RecyclerView productrecycler;
    Button viewall;

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
        add_cart=(TextView) findViewById(R.id.add_cart);
        subscribe=(TextView) findViewById(R.id.subscribe);
        pbrand=(TextView) findViewById(R.id.brand);
        pname=(TextView) findViewById(R.id.product_name);
        pprice=(TextView) findViewById(R.id.product_price);
        pdescription=(TextView) findViewById(R.id.description);

        productrecycler=(RecyclerView)findViewById(R.id.similarproductrecycler);
        productrecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productrecycler.setHasFixedSize(true);


        viewall=(Button)findViewById(R.id.view_all1);

        viewPager=(ViewPager)findViewById(R.id.viewPager_product);
        dotsIndicator=(DotsIndicator)findViewById(R.id.dots_indicator);


        productImageModelList=new ArrayList<ProductImageModel>();
        productref= FirebaseDatabase.getInstance().getReference().child("products");

        loadSimilarProducts();
        getProductDetails();

    }

    private void loadSimilarProducts() {
        productoptions=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(productref.orderByChild("product_brand").startAt(brand),ProductModel.class).build();
        productadapter= new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(productoptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final ProductModel productModel) {
                productViewHolder.p_name.setText(productModel.getProduct_name());
                productViewHolder.p_price.setText("₹" + productModel.getProduct_price());
                Picasso.with(getApplicationContext()).load(productModel.getImage1()).into(productViewHolder.image);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ViewProductPage.this, ViewProductPage.class).putExtra("pid",productModel.getPid())
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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


    private void getProductDetails() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Loading...");
        dialog.show();

        productref.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name= snapshot.child("product_name").getValue().toString();
                    String description= snapshot.child("product_description").getValue().toString();
                    brand= snapshot.child("product_brand").getValue().toString();
                    String price= snapshot.child("product_price").getValue().toString();
                    String category= snapshot.child("product_category").getValue().toString();
                    String image1= snapshot.child("image1").getValue().toString();
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


                    dialog.dismiss();
                }
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        productadapter.startListening();
    }
}