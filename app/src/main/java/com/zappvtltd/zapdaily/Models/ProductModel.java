package com.zappvtltd.zapdaily.Models;

public class ProductModel {
    String product_name,product_description,product_brand,product_category,product_price,pid,image1,image2,image3,isavailable;

    public ProductModel(){

    }

    public ProductModel(String product_name, String product_description, String product_brand, String product_category, String product_price, String pid, String image1, String image2, String image3, String isavailable) {
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_brand = product_brand;
        this.product_category = product_category;
        this.product_price = product_price;
        this.pid = pid;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.isavailable = isavailable;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(String isavailable) {
        this.isavailable = isavailable;
    }
}
