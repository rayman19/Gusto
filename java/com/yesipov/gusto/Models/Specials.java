package com.yesipov.gusto.Models;

public class Specials {
    private String title;
    private String describer;
    private int flagResource;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriber() {
        return describer;
    }

    public void setDescriber(String describer) {
        this.describer = describer;
    }

    public int getFlagResource() {
        return flagResource;
    }

    public void setFlagResource(int flagResource) {
        this.flagResource = flagResource;
    }

    public Specials() {}

    public Specials(String title, String describer, int flagResource) {
        this.title = title;
        this.describer = describer;
        this.flagResource = flagResource;
    }
}