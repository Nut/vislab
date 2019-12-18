package de.hska.iwi.vslab.inventoryservice.models;

public class Category {

    private Long id;
    private String name;
    private Long[] products;

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

    public Long[] getProducts() {
        return this.products;
    }

    public void setProducts(Long[] products) {
        this.products = products;
    }
}