package com.zappvtltd.zapdaily.Models;

public class Users {
    private String name,uid , phone_number ,password,pincode , Addres ,Profilepic_URL;

    public Users()
    {

    }

    public Users(String name, String phone_number, String password, String pincode, String addres, String uid, String Profilepic_URL) {
        this.name = name;
        this.uid =uid;
        this.phone_number = phone_number;
        this.password = password;
        this.pincode = pincode;
        Addres = addres;
        this.Profilepic_URL = Profilepic_URL;
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
