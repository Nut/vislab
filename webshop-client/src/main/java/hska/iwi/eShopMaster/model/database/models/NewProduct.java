package hska.iwi.eShopMaster.model.database.models;

public class NewProduct {

    private String name;
    private double price;
    private int category;
    private String details;

    public NewProduct(String name, double price, int category, String details) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.details = details;
    }

    public NewProduct() {
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

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}