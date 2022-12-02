package pt.ua.hackaton.smartmove.handlers;

public class PotentialHandler {

    private static PotentialHandler instance;

    private PotentialHandler() { }

    public double calculatePotential(double imc, double totalExerciseTime, double totalCorrectnessAverage, double totalCaloriesBurned) {
        return (-0.2 * imc / 10 + 1.2 * totalCorrectnessAverage + 0.8 * totalCaloriesBurned + 0.05 * totalExerciseTime) / 1000;
    }

    public static PotentialHandler getInstance() {
        if (instance == null)
            instance = new PotentialHandler();
        return instance;
    }

}
