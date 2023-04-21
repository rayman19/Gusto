package com.yesipov.gusto.Models;

public class OrderItem {
    Food food;
    int count = 1, fullPrice = 0;

    OrderItem() {}

    public OrderItem(Food food) {
        this.food = food;
        this.count = 1;
        this.fullPrice = food.getPrice() * count;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(int fullPrice) {
        this.fullPrice = fullPrice;
    }
}
