package com.zappvtltd.zapdaily.Models;

public class CartModel {

    String date,time,cart_id,pid,p_name,p_price,image,quantity;

    public CartModel(String date, String time, String cart_id, String pid, String p_name, String p_price, String image, String quantity) {
        this.date = date;
        this.time = time;
        this.cart_id = cart_id;
        this.pid = pid;
        this.p_name = p_name;
        this.p_price = p_price;
        this.image = image;
        this.quantity = quantity;
    }

    public CartModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
