public interface FitnessAdapter {
    double getCaloriesBurned();
    int getStepsCount();
    void updateCalories(double burnedCalories, double initialCalories);
}
