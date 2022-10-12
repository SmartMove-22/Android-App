package pt.ua.hackaton.smartmove.data;

public class Exercise {

    private final long id;
    private final Coach coach;
    private final String name;
    private final Category category;
    private final int sets;
    private final int reps;
    private final double calories;

    public Exercise(long id, Coach coach, String name, Category category, int sets, int reps, double calories) {
        this.id = id;
        this.coach = coach;
        this.name = name;
        this.category = category;
        this.sets = sets;
        this.reps = reps;
        this.calories = calories;
    }


    public long getId() {
        return id;
    }

    public Coach getCoach() {
        return coach;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getCalories() {
        return calories;
    }
}
