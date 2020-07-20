package com.zappvtltd.zapdaily.Models;

public class BrandModel {
    String b_name,b_type,logo;

    public BrandModel(String b_name, String b_type, String logo) {
        this.b_name = b_name;
        this.b_type = b_type;
        this.logo = logo;
    }

    public BrandModel(){

    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_type() {
        return b_type;
    }

    public void setB_type(String b_type) {
        this.b_type = b_type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
