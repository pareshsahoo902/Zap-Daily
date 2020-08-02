package com.zappvtltd.zapdaily.BottomSheetUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.R;
import com.zappvtltd.zapdaily.ViewHolder.ProductViewHolder;
import com.zappvtltd.zapdaily.ViewProductPage;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class BottomFragSubscribe extends Fragment {

    CardView Sub_btn;
    RecyclerView recyclerView;
    TextView similar;
    Button viewAll;
    String suggestion="ghee",product_id;

    String category,brand,out_of_stock;
    DatabaseReference productref;
    private FirebaseRecyclerOptions<ProductModel> productoptions;
    private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> productadapter;

    public BottomFragSubscribe() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.subscription_bottomsheet, container, false);

        Bundle bundle =this.getArguments();
        product_id =bundle.getString("pid");
        Sub_btn =view.findViewById(R.id.card_Subscribe);
        viewAll =view.findViewById(R.id.view_all1);
        similar =view.findViewById(R.id.similar_product);

        similar.setText("People Subscribe it with");
        viewAll.setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.similarproductrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        Sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        productref= FirebaseDatabase.getInstance().getReference().child("products");

        getProductDatabse();

        return view;
    }

    private void getProductDatabse() {

        productref.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    category=snapshot.child("product_category").getValue().toString();
                    brand=snapshot.child("product_brand").getValue().toString();
                    out_of_stock=snapshot.child("isavailable").getValue().toString();
                    loadProductSuggestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadProductSuggestion() {
        if (category.equals("milk")|| category.equals("yogurt") || category.equals("butter")){
            suggestion="bread";
        }
        else if(category.equals("bread")|| category.equals("cheese")|| category.equals("paneer")){
            suggestion="ghee";
        }
        else {
            suggestion="milk";
        }
        productoptions=new FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(productref.orderByChild("product_category").startAt(suggestion),ProductModel.class).build();
        productadapter= new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(productoptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final ProductModel productModel) {
                productViewHolder.p_name.setText(productModel.getProduct_name());
                productViewHolder.p_price.setText("₹" + productModel.getProduct_price());
                Picasso.with(getContext()).load(productModel.getImage1()).into(productViewHolder.image);

//                PushDownAnim.setPushDownAnimTo(productViewHolder.itemView)
//                        .setScale(MODE_SCALE,
//                                PushDownAnim.DEFAULT_PUSH_SCALE)
//
//                        .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
//                        .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
//                        .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
//                        .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
//                        .setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent=new Intent(getActivity(), ViewProductPage.class).putExtra("pid",productModel.getPid()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(),R.anim.fade_in,R.anim.fade_out);
//                                startActivity(intent,options.toBundle());
//                            }
//                        });
                productViewHolder.add.setImageResource(R.drawable.add_pro);
                productViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "addded to list", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.product_items, parent, false));
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
        recyclerView.setAdapter(productadapter);
        productadapter.startListening();


    }
}
