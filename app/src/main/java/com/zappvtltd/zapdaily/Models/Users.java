package com.zappvtltd.zapdaily.Models;

public class Users {
    private String name,uid , phone_number ,password,pincode , Addres ,Profilepic_URL,wallet_balance;

    public Users()
    {

    }

    public Users(String name, String phone_number, String password, String pincode, String addres, String uid, String Profilepic_URL,String wallet_balance) {
        this.name = name;
        this.uid =uid;
        this.phone_number = phone_number;
        this.password = password;
        this.pincode = pincode;
        Addres = addres;
        wallet_balance=this.wallet_balance;
        this.Profilepic_URL = Profilepic_URL;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddres() {
        return Addres;
    }

    public void setAddres(String addres) {
        Addres = addres;
    }

    public String getProfilepic_URL() {
        return Profilepic_URL;
    }

    public void setProfilepic_URL(String image) {
        this.Profilepic_URL = image;
    }
}
