package com.zappvtltd.zapdaily.Home;

import com.zappvtltd.zapdaily.Models.SliderModel;

import java.util.List;

public class HomePageModel {

    public static final int BANNER_SLIDER_VIEW = 0;
    public static final int BRANDLIST_VIEW = 1;
    public static final int HORIZONTAL_PRODUCT_LAYOUT = 2;
    public static final int GRID_LAYOUT_PRODUCT = 3;

    private int type;

    private List<SliderModel> sliderModelList;

    public HomePageModel() {

    }

    /////BrandView

    public HomePageModel(int type) {
        this.type = type;
    }

    /////Brandview

    /////Banner
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    /////Banner




}
