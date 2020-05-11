package com.riseofash.expensemanager.model;

public class Data {
    String type;
    int amount;
    String name;
    String id;
    String date;
    public Data(){

    }

    public Data(String id,  String name,String type,int amount , String date) {
        this.type = type;
        this.amount = amount;
        this.name = name;
        this.id = id;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
