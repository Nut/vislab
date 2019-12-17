package de.hska.iwi.vslab.webshopapi.models;

public class Product {

    private Long id;
    private String name;
    private double price;
    private Category category;
    private String details;

    public Product(String name, double price, Category category, String details) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.details = details;
    }

    public Product() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}