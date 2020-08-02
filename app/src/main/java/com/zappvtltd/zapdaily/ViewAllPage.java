package com.zappvtltd.zapdaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.DialogHelper.ProgressDialogLoading;
import com.zappvtltd.zapdaily.Models.BrandModel;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.ViewHolder.BrandViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.GridProductViewHolder;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class ViewAllPage extends AppCompatActivity {

    RecyclerView brandproductsrecycler;
    private FirebaseRecyclerOptions<ProductModel> productoptions;
    private FirebaseRecyclerAdapter<ProductModel, GridProductViewHolder> productadapter;
    String bid,brandName,brandicon;
    DatabaseReference productref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_page);

        bid=getIntent().getStringExtra("id");
        DatabaseReference brandref = FirebaseDatabase.getInstance().getReference().child("brands").child(bid);
        brandref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    brandName=snapshot.child("b_name").getValue().toString();
                    brandicon=snapshot.child("logo").getValue().toString();
                    loadProducts();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        brandproductsrecycler=findViewById(R.id.view_all_recycler);
        brandproductsrecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false));

        productref = FirebaseDatabase.getInstance().getReference().child("products");

    }

    private void loadProducts() {
        final android.app.AlertDialog dialog = ProgressDialogLoading.getLoadingDialog(this, "Loading...");
        dialog.show();
        productoptions = new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(productref.orderByChild("product_brand").equalTo(brandName),ProductModel.class).build();

        productadapter = new FirebaseRecyclerAdapter<ProductModel, GridProductViewHolder>(productoptions) {
            @Override
            protected void onBindViewHolder(@NonNull GridProductViewHolder productViewHolder, int i, @NonNull final ProductModel productModel) {
                productViewHolder.p_name.setText(productModel.getProduct_name());
                productViewHolder.p_price.setText("₹" + productModel.getProduct_price());
                Picasso.with(getApplicationContext()).load(productModel.getImage1()).into(productViewHolder.image);


                dialog.dismiss();
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
                productViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Product added ! check Cart", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @NonNull
            @Override
            public GridProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new GridProductViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_product_itemview,parent,false));
            }
        };

        brandproductsrecycler.setAdapter(productadapter);
        productadapter.startListening();
    }
}