package com.zappvtltd.zapdaily.Models;

public class WalletModel {

    String customer_id,transaction_id,amount,date,time,type;

    public WalletModel(String customer_id, String transaction_id, String amount, String date, String time, String type) {
        this.customer_id = customer_id;
        this.transaction_id = transaction_id;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public WalletModel() {
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
