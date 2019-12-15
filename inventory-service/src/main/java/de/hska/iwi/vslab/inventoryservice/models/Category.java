package de.hska.iwi.vslab.inventoryservice.models;

public class Category {

    private Long id;
    private String name;
    private int[] products;

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

    public int[] getProducts() {
        return this.products;
    }

    public void setProducts(int[] products) {
        this.products = products;
    }
}