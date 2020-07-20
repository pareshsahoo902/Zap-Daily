package com.zappvtltd.zapdaily.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.zappvtltd.zapdaily.Models.ProductImageModel;
import com.zappvtltd.zapdaily.R;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {

    private List<ProductImageModel> productImageModelList;
    private ImageView image;
    Context context;


    public ProductImageAdapter(List<ProductImageModel> productImageModelList,Context context) {
        this.productImageModelList = productImageModelList;
        this.context=context;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v= LayoutInflater.from(container.getContext()).inflate(R.layout.product_viewpager_item,container,false);
        image= (ImageView)v.findViewById(R.id.image_product);
        Picasso.with(context).load(productImageModelList.get(position).getImage()).into(image);
        container.addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return productImageModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
