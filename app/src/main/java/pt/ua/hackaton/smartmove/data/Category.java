package pt.ua.hackaton.smartmove.data;

import pt.ua.hackaton.smartmove.utils.ExerciseCategory;

public class Category {

    private final long id;
    private final ExerciseCategory category;
    private final String subCategory;

    public Category(long id, ExerciseCategory category, String subCategory) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
    }

    public long getId() {
        return id;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }
    
}
