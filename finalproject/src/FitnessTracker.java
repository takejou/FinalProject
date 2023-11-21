import java.util.Random;

public class FitnessTracker {

    private static final int STEPS_CALORIES_RATIO = 25; // steps and calorie proportion

    private Random random;
    private int stepsCount;

    public FitnessTracker() {
        random = new Random();
        // steps
        stepsCount = random.nextInt(14001);
    }

    public double getCaloriesBurned() {
        // steps to calories
        return stepsCount * 0.04;
    }

    public int getStepsCount() {
        return stepsCount;
    }
}
