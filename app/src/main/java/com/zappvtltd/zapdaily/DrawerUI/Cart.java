package com.zappvtltd.zapdaily.DrawerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.zappvtltd.zapdaily.Models.CartModel;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.R;
import com.zappvtltd.zapdaily.ViewHolder.CartViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.ProductViewHolder;
import com.zappvtltd.zapdaily.ViewProductPage;

import java.util.HashMap;

public class Cart extends AppCompatActivity {
    private ImageView back;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<CartModel> cartoptions;
    private FirebaseRecyclerAdapter<CartModel, CartViewHolder> cartadapter;
    DatabaseReference cartref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        back=(ImageView)findViewById(R.id.BackChal);

        recyclerView=findViewById(R.id.cart_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadCartItems();
    }

    private void loadCartItems() {
        cartref= FirebaseDatabase.getInstance().getReference().child("carts").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartoptions= new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(cartref,CartModel.class).build();
        cartadapter= new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(cartoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final CartModel cartModel) {
                cartViewHolder.pname.setText(cartModel.getP_name());
                cartViewHolder.quantity.setText("Quantity: "+cartModel.getQuantity());
                Picasso.with(getApplicationContext()).load(cartModel.getImage()).into(cartViewHolder.product_image);

                int total= Integer.parseInt(cartModel.getP_price())*Integer.parseInt(cartModel.getQuantity());
                cartViewHolder.total_price.setText("Total price: ₹"+Integer.toString(total));

                cartViewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartref.child(cartModel.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Cart.this, "Product removed! ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                cartViewHolder.save_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference cartref= FirebaseDatabase.getInstance().getReference().child("saved_items");
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("pid",cartModel.getPid());
                        hashMap.put("p_name",cartModel.getP_name());
                        hashMap.put("p_price",cartModel.getP_price());
                        hashMap.put("image",cartModel.getImage());

                        cartref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cartModel.getPid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DatabaseReference likedref=FirebaseDatabase.getInstance().getReference().child("liked_products");
                                    HashMap<String,Object> hashMap=new HashMap<>();
                                    hashMap.put("isLiked","true");
                                    likedref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cartModel.getPid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Cart.this, "Product added to cart", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(Cart.this, "Some error occured please try again", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CartViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.cart_viewholder, parent, false));
            }
        };
        recyclerView.setAdapter(cartadapter);
        cartadapter.startListening();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        cartadapter.startListening();
    }
}