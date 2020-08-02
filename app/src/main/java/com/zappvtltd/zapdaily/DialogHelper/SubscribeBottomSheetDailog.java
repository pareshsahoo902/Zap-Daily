package com.zappvtltd.zapdaily.DialogHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.zappvtltd.zapdaily.Adapter.BottomTabLayoutAdapter;
import com.zappvtltd.zapdaily.R;

import net.kyouko.lockablebottomsheet.LockableBottomSheetDialogFragment;

import static android.graphics.Color.*;

public class SubscribeBottomSheetDailog extends BottomSheetDialogFragment {
    ImageView pimage;
    TextView pname,pprice,pquantity;
    TabLayout tabLayout;
    ViewPager viewPager;
    String product_name,product_price,product_quantity,product_image,product_id,total_amount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bottomsheet,container,false);

        Bundle bundle =this.getArguments();
        product_image =bundle.getString("pimage");
        product_name =bundle.getString("pname");
        product_price =bundle.getString("pprice");
        product_quantity =bundle.getString("pquantity");
        product_id =bundle.getString("pid");

        int total= Integer.parseInt(product_price)*Integer.parseInt(product_quantity);
        total_amount= Integer.toString(total);
        pname=view.findViewById(R.id.nameptext);
        pprice=view.findViewById(R.id.priceptext);
        pquantity=view.findViewById(R.id.quantityptext);
        pimage=view.findViewById(R.id.pic_image);
        setDetails();
        viewPager=view.findViewById(R.id.bottom_viewpager);
        tabLayout=view.findViewById(R.id.bottom_tab);

        tabLayout.addTab(tabLayout.newTab().setText("Select").setIcon(R.drawable.sure));
        tabLayout.addTab(tabLayout.newTab().setText("Delivery Address").setIcon(R.drawable.bottom_location));
        tabLayout.addTab(tabLayout.newTab().setText("Frequency").setIcon(R.drawable.bottommenu));
        tabLayout.addTab(tabLayout.newTab().setText("Pay").setIcon(R.drawable.payment));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final BottomTabLayoutAdapter bottomTabLayoutAdapter = new BottomTabLayoutAdapter(getChildFragmentManager(),getContext(),tabLayout.getTabCount(),product_id);
        viewPager.setAdapter(bottomTabLayoutAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    private void setDetails() {
        pname.setText(product_name);
        pprice.setText("₹"+total_amount);
        pquantity.setText("Quantity: "+product_quantity+"\nTotal Amount:\n ");
        Picasso.with(getContext()).load(product_image).into(pimage);
    }
}
