
package com.smarttech.store.models;

public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private int imageResource;

    public Product(int id, String name, String description,
                   double price, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}

