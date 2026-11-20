package com.smarttech.store.models;

public class Product {

    private int id;
    private String documentId;
    private String name;
    private String description;
    private double price;
    private int imageResource;
    private String imageUrl;

    public Product() {
        // Constructor vacío para compatibilidad con Firebase.
    }

    // Constructor compatible con el código anterior.
    public Product(
            int id,
            String name,
            String description,
            double price,
            int imageResource
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.imageUrl = "";
    }

    // Constructor compatible con el código anterior.
    public Product(
            int id,
            String documentId,
            String name,
            String description,
            double price,
            int imageResource
    ) {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.imageUrl = "";
    }

    // Constructor para productos con imagen desde Firebase Storage.
    public Product(
            int id,
            String documentId,
            String name,
            String description,
            double price,
            int imageResource,
            String imageUrl
    ) {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
