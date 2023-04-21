package com.yesipov.gusto.Models;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    int fullprice;
    String number, status, adress, username;
    ArrayList<OrderItem> listOrderItem;
    boolean isCour, isCard;

    public Order() {}

    public Order(String number, String status, int fullprice, ArrayList<OrderItem> listOrderItem) {
        this.number = number;
        this.fullprice = fullprice;
        this.status = status;
        this.listOrderItem = listOrderItem;
    }

    public Order(String number, String status, int fullprice, ArrayList<OrderItem> listOrderItem, String adress, boolean isCour, boolean isCard, String username) {
        this.username = username;
        this.number = number;
        this.fullprice = fullprice;
        this.status = status;
        this.adress = adress;
        this.listOrderItem = listOrderItem;
        this.isCour = isCour;
        this.isCard = isCard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFullprice() {
        return fullprice;
    }

    public void setFullprice(int fullprice) {
        this.fullprice = fullprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public ArrayList<OrderItem> getListOrderItem() {
        return listOrderItem;
    }

    public void setListOrderItem(ArrayList<OrderItem> listOrderItem) {
        this.listOrderItem = listOrderItem;
    }

    public boolean isCour() {
        return isCour;
    }

    public void setCour(boolean cour) {
        isCour = cour;
    }

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean card) {
        isCard = card;
    }
}
