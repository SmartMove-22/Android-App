package pt.ua.hackaton.smartmove.data;

public class AssignedExercise {

    private final Trainee trainee;
    private final boolean completed;
    private final double correctness;
    private final double performance;
    private final double improvement;
    private final double caloriesBurn;
    private final int grade;

    public AssignedExercise(Trainee trainee, boolean completed, double correctness, double performance, double improvement, double caloriesBurn, int grade) {
        this.trainee = trainee;
        this.completed = completed;
        this.correctness = correctness;
        this.performance = performance;
        this.improvement = improvement;
        this.caloriesBurn = caloriesBurn;
        this.grade = grade;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public boolean isCompleted() {
        return completed;
    }

    public double getCorrectness() {
        return correctness;
    }

    public double getPerformance() {
        return performance;
    }

    public double getImprovement() {
        return improvement;
    }

    public double getCaloriesBurn() {
        return caloriesBurn;
    }

    public int getGrade() {
        return grade;
    }

}
