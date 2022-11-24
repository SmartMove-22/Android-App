package pt.ua.hackaton.smartmove.data;

public class Category {

    private final long id;
    private final String category;
    private final String subCategory;

    public Category(long id, String category, String subCategory) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }
    
}
