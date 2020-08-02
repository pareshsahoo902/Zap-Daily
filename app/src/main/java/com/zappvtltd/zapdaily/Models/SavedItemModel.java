package com.zappvtltd.zapdaily.Models;

public class SavedItemModel {

    String p_name,p_price,pid,image;
    public SavedItemModel() {
    }

    public SavedItemModel(String p_name, String p_price, String pid, String image) {
        this.p_name = p_name;
        this.p_price = p_price;
        this.pid = pid;
        this.image = image;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
