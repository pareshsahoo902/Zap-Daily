package com.zappvtltd.zapdaily.Models;

public class CategoryModel {

    String category_name,category_type,icon_url;

    public CategoryModel(){

    }

    public CategoryModel(String category_name, String category_type, String icon_url) {
        this.category_name = category_name;
        this.category_type = category_type;
        this.icon_url = icon_url;
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
