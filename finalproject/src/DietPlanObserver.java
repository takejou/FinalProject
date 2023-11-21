import java.util.ArrayList;
import java.util.List;

public class DietPlanObserver implements Observer {
    private List<Observer> observers;
    private DietStrategy dietStrategy;
    private double targetCalories;
    private boolean displayTotalCalories;

    public DietPlanObserver(DietStrategy dietStrategy) {
        this.observers = new ArrayList<>();
        this.dietStrategy = dietStrategy;
        this.targetCalories = 0.0;
        this.displayTotalCalories = false;
    }

    public double getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(double targetCalories) {
        this.targetCalories = targetCalories;
    }

    public void setDisplayTotalCalories(boolean displayTotalCalories) {
        this.displayTotalCalories = displayTotalCalories;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void update(double totalCalories, double targetCalories, String message) {
        if (displayTotalCalories) {
            System.out.println("Total calories: " + totalCalories);
            System.out.println("Congratulations! You've achieved your goal.");
            // target calories
            String resultMessage = message;

            notifyObservers(totalCalories, targetCalories, resultMessage);
        }
    }

    private boolean checkGoalAchievement(DietStrategy strategy, double totalCalories, double targetCalories) {
        if (strategy instanceof LoseWeightStrategy) {
            return totalCalories < targetCalories;
        } else if (strategy instanceof GainWeightStrategy || strategy instanceof GainMuscleMassStrategy) {
            return totalCalories >= targetCalories;
        }
        return false;
    }

    private void notifyObservers(double totalCalories, double targetCalories, String message) {
        for (Observer observer : observers) {
            observer.update(totalCalories, targetCalories, message);
        }
    }
}
