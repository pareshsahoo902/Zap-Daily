package com.zappvtltd.zapdaily.Home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.IndexedNode;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.zappvtltd.zapdaily.Adapter.SliderAdapter;
import com.zappvtltd.zapdaily.LoginActivity;
import com.zappvtltd.zapdaily.Models.BrandModel;
import com.zappvtltd.zapdaily.Models.ProductModel;
import com.zappvtltd.zapdaily.Models.SliderModel;
import com.zappvtltd.zapdaily.R;
import com.zappvtltd.zapdaily.ViewAllPage;
import com.zappvtltd.zapdaily.ViewHolder.BrandViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.GridProductViewHolder;
import com.zappvtltd.zapdaily.ViewHolder.ProductViewHolder;
import com.zappvtltd.zapdaily.ViewProductPage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.content.ContextCompat.startActivity;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER_VIEW;
            case 1:
                return HomePageModel.BRANDLIST_VIEW;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_LAYOUT;
            case 3:
                return HomePageModel.GRID_LAYOUT_PRODUCT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER_VIEW:
                return new BannerSliderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.banner_layout, parent, false));

            case HomePageModel.BRANDLIST_VIEW:
                return new BrandListViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.brand_list, parent, false));

            case HomePageModel.HORIZONTAL_PRODUCT_LAYOUT:
                return new HorizontalProductViewHOlder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.horizontal_product_layout, parent, false));

            case HomePageModel.GRID_LAYOUT_PRODUCT:
                return new GridLayoutProduct(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_grid_layout, parent, false));

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER_VIEW:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerpageView(sliderModelList);
                break;
            case HomePageModel.BRANDLIST_VIEW:
                ((BrandListViewHolder)holder).loadRecyclerForBrand();
                break;
            case HomePageModel.HORIZONTAL_PRODUCT_LAYOUT:
                ((HorizontalProductViewHOlder)holder).getProductHorizontalRecycler();
                break;
            case HomePageModel.GRID_LAYOUT_PRODUCT:
                ((GridLayoutProduct)holder).getProductGridRecycler();
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////BRANDLIST LAYOUT

    public class BrandListViewHolder extends RecyclerView.ViewHolder {

        private FirebaseRecyclerOptions<BrandModel> brandoptions;
        private FirebaseRecyclerAdapter<BrandModel, BrandViewHolder> brandadapter;
        RecyclerView brand_recycler;

        public BrandListViewHolder(@NonNull View itemView) {
            super(itemView);

            brand_recycler = itemView.findViewById(R.id.similarproductrecycler);
            brand_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));


        }

        private void loadRecyclerForBrand() {
            DatabaseReference brandref = FirebaseDatabase.getInstance().getReference().child("brands");
            brandoptions = new FirebaseRecyclerOptions.Builder<BrandModel>().setQuery(brandref, BrandModel.class).build();
            brandadapter = new FirebaseRecyclerAdapter<BrandModel, BrandViewHolder>(brandoptions) {
                @Override
                protected void onBindViewHolder(@NonNull BrandViewHolder brandViewHolder, int i, @NonNull final BrandModel brandModel) {
                    brandViewHolder.name.setText(brandModel.getB_name());
                    Picasso.with(itemView.getContext()).load(brandModel.getLogo()).into(brandViewHolder.logo);
                    PushDownAnim.setPushDownAnimTo(brandViewHolder.itemView)
                            .setScale(MODE_SCALE,
                                    PushDownAnim.DEFAULT_PUSH_SCALE)

                            .setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
                            .setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
                            .setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
                            .setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(itemView.getContext(), ViewAllPage.class)
                                            .putExtra("id",brandModel.getBrand_id());
                                    ActivityOptions options = ActivityOptions.makeCustomAnimation(itemView.getContext(),R.anim.fade_in,R.anim.fade_out);
                                    itemView.getContext().startActivity(intent,options.toBundle());
                                }
                            });

                }

                @NonNull
                @Override
                public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new BrandViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_holder_view, parent, false));
                }
            };
            brand_recycler.setAdapter(brandadapter);
            brandadapter.startListening();

        }

    }


    ///////////////////////////////////////////////////////////////////////////////////BANNER SLIDER

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        ViewPager bannerpage;
        DotsIndicator dotsIndicator;
        private int current_page = 2;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerpage = (ViewPager) itemView.findViewById(R.id.viewPager);
            dotsIndicator = (DotsIndicator) itemView.findViewById(R.id.dots_indicator);


        }

        private void setBannerpageView(final List<SliderModel> sliderModelList) {
            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannerpage.setAdapter(sliderAdapter);
            bannerpage.setClipToPadding(false);
            bannerpage.setPageMargin(15);
            dotsIndicator.setViewPager(bannerpage);


            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    current_page = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(sliderModelList);
                    }

                }
            };
            bannerpage.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideshow(sliderModelList);

            bannerpage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(sliderModelList);
                    stopBannerSlideshow();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideshow(sliderModelList);
                    }
                    return false;
                }
            });
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (current_page == sliderModelList.size() - 2) {
                current_page = 2;
                bannerpage.setCurrentItem(current_page, false);
            }
            if (current_page == 1) {
                current_page = sliderModelList.size() - 3;
                bannerpage.setCurrentItem(current_page, false);
            }

        }

        private void startBannerSlideshow(final List<SliderModel> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (current_page >= sliderModelList.size()) {
                        current_page = 1;
                    }
                    bannerpage.setCurrentItem(current_page++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopBannerSlideshow() {
            timer.cancel();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////HORIZONTAL LAYOUT

    public class HorizontalProductViewHOlder extends RecyclerView.ViewHolder{

        private FirebaseRecyclerOptions<ProductModel> productoptions;
        private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> productadapter;
        RecyclerView product_recycler_horizontal;
        Button viewall;
        public HorizontalProductViewHOlder(@NonNull View itemView) {
            super(itemView);
            viewall=itemView.findViewById(R.id.viewall);
            product_recycler_horizontal=itemView.findViewById(R.id.product_recycler_view);
            product_recycler_horizontal.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));

        }
        private void getProductHorizontalRecycler(){
            DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("products");
            productoptions = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(productref.orderByChild("product_category").equalTo("milk"), ProductModel.class).build();
            productadapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(productoptions) {
                @Override
                protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull final ProductModel productModel) {
                    productViewHolder.p_name.setText(productModel.getProduct_name());
                    productViewHolder.p_price.setText("₹" + productModel.getProduct_price());
                    Picasso.with(itemView.getContext()).load(productModel.getImage1()).into(productViewHolder.image);

                    Animation animation;
                    animation = AnimationUtils.loadAnimation(itemView.getContext(),R.anim.fade_in);
                    animation.setDuration(500);
                    productViewHolder.itemView.setAnimation(animation);

                    if (getItemCount()<7){
                        viewall.setVisibility(View.INVISIBLE);

                    }
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
                                    Intent intent=new Intent(itemView.getContext(),ViewProductPage.class).putExtra("pid",productModel.getPid());
                                    ActivityOptions options = ActivityOptions.makeCustomAnimation(itemView.getContext(),R.anim.fade_in,R.anim.fade_out);
                                    itemView.getContext().startActivity(intent,options.toBundle());
                                }
                            });
                    productViewHolder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            addProductToCart(productModel.getPid());
                            Toast.makeText(itemView.getContext(), "Product added ! check Cart", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items,parent,false));
                }

                @Override
                public int getItemCount() {

                    if (super.getItemCount()>8){
                        return 8;
                    }
                    else {
                        return super.getItemCount();
                    }


                }

            };

            product_recycler_horizontal.setAdapter(productadapter);
            productadapter.startListening();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////GRID LAYOUT


    public class GridLayoutProduct extends RecyclerView.ViewHolder{
        private FirebaseRecyclerOptions<ProductModel> productoptions;
        private FirebaseRecyclerAdapter<ProductModel, GridProductViewHolder> productadapter;
        RecyclerView product_recycler;
        Button view_all;

        public GridLayoutProduct(@NonNull View itemView) {
            super(itemView);

            view_all=itemView.findViewById(R.id.view_all1);
            product_recycler = itemView.findViewById(R.id.gridRecycler);
            product_recycler.setLayoutManager(new GridLayoutManager(itemView.getContext(),2,GridLayoutManager.VERTICAL,false));

        }
        private void getProductGridRecycler(){
            DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("products");
            productoptions = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(productref.orderByChild("product_brand").equalTo("Amul"), ProductModel.class).build();
            productadapter = new FirebaseRecyclerAdapter<ProductModel, GridProductViewHolder>(productoptions) {
                @Override
                protected void onBindViewHolder(@NonNull final GridProductViewHolder productViewHolder, int i, @NonNull final ProductModel productModel) {
                    productViewHolder.p_name.setText(productModel.getProduct_name());
                    productViewHolder.p_price.setText("₹" + productModel.getProduct_price());
                    Picasso.with(itemView.getContext()).load(productModel.getImage1()).into(productViewHolder.image);

                    Animation animation;
                    animation = AnimationUtils.loadAnimation(itemView.getContext(),R.anim.fade_in);
                    animation.setDuration(500);
                    productViewHolder.itemView.setAnimation(animation);
                    if (getItemCount()<5){
                        view_all.setVisibility(View.INVISIBLE);

                    }
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
                                    Intent intent=new Intent(itemView.getContext(),ViewProductPage.class).putExtra("pid",productModel.getPid());
                                    ActivityOptions options = ActivityOptions.makeCustomAnimation(itemView.getContext(),R.anim.fade_in,R.anim.fade_out);
                                    itemView.getContext().startActivity(intent,options.toBundle());
                                }
                            });


                    productViewHolder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            addProductToCart(productModel.getPid());
                            Toast.makeText(itemView.getContext(), "Product added ! check Cart", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


                @NonNull
                @Override
                public GridProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new GridProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_itemview,parent,false));
                }

                @Override
                public int getItemCount() {
                    if (super.getItemCount()>4){
                        return 4;
                    }
                    else {
                        return super.getItemCount();
                    }

                }

            };

            product_recycler.setAdapter(productadapter);
            productadapter.startListening();
        }
    }


}
