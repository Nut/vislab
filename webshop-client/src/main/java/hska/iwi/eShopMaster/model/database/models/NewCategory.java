package hska.iwi.eShopMaster.model.database.models;

public class NewCategory {

    private String categoryName;

    public NewCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
