public class FitnessTrackerAdapter implements FitnessAdapter{
    private FitnessTracker fitnessTracker;

    public FitnessTrackerAdapter(FitnessTracker fitnessTracker) {
        this.fitnessTracker = fitnessTracker;
    }

    public double getCaloriesBurned() {
        return fitnessTracker.getCaloriesBurned();
    }

    public int getStepsCount() {
        return fitnessTracker.getStepsCount();
    }

    public void updateCalories(double burnedCalories, double initialCalories) {
        //the calories burned through fitness activities
        double modifiedCalories = initialCalories + burnedCalories;
        System.out.println("Modified daily calorie intake (including burned calories): " + modifiedCalories + " kcal");
    }
}
