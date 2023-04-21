package com.yesipov.gusto.Models;

public class Food {
    private String name, category, container;
    private int price, weight;
    private int flagResource;

    public Food() {}

    public Food(String name, String category, String container, int price, int weight, int flagResource) {
        this.name = name;
        this.category = category;
        this.container = container;
        this.price = price;
        this.weight = weight;
        this.flagResource = flagResource;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFlagResource() {
        return flagResource;
    }

    public void setFlagResource(int flagResource) {
        this.flagResource = flagResource;
    }
}
