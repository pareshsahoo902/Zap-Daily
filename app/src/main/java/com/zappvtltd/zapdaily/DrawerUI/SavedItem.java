package com.zappvtltd.zapdaily.DrawerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.zappvtltd.zapdaily.Models.CartModel;
import com.zappvtltd.zapdaily.Models.SavedItemModel;
import com.zappvtltd.zapdaily.R;
import com.zappvtltd.zapdaily.ViewHolder.CartViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.SavedItemsViewHolder;

public class SavedItem extends AppCompatActivity {

    private ImageView back;
    private RecyclerView recycler;
    private FirebaseRecyclerOptions<SavedItemModel> saveditemoptions;
    private FirebaseRecyclerAdapter<SavedItemModel, SavedItemsViewHolder> saveditemadapter;
    DatabaseReference saveditemref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_item);

        back=(ImageView)findViewById(R.id.BackChal);
        recycler=findViewById(R.id.saved_recycler);
        recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadSavedItems();
    }

    private void loadSavedItems() {
        saveditemref= FirebaseDatabase.getInstance().getReference().child("saved_items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        saveditemoptions= new FirebaseRecyclerOptions.Builder<SavedItemModel>().setQuery(saveditemref,SavedItemModel.class).build();
        saveditemadapter=new FirebaseRecyclerAdapter<SavedItemModel, SavedItemsViewHolder>(saveditemoptions) {
            @Override
            protected void onBindViewHolder(@NonNull SavedItemsViewHolder savedItemsViewHolder, int i, @NonNull SavedItemModel savedItemModel) {
                Picasso.with(getApplicationContext()).load(savedItemModel.getImage()).into(savedItemsViewHolder.image);
                savedItemsViewHolder.p_name.setText(savedItemModel.getP_name());
                savedItemsViewHolder.p_price.setText("₹"+savedItemModel.getP_price());

                savedItemsViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            @NonNull
            @Override
            public SavedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new SavedItemsViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_product_itemview, parent, false));
            }
        };
        recycler.setAdapter(saveditemadapter);
        saveditemadapter.startListening();

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        saveditemadapter.startListening();
    }
}