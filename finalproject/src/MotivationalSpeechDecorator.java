import java.util.List;
import java.util.ArrayList;
public class MotivationalSpeechDecorator implements DietStrategy {
    private DietStrategy baseStrategy;
    private List<Observer> observers = new ArrayList<>();

    public MotivationalSpeechDecorator(DietStrategy baseStrategy) {
        this.baseStrategy = baseStrategy;
    }

    @Override
    public void incrementProductCounter() {
        if (baseStrategy instanceof DietStrategy) {
            DietStrategy strategy = (DietStrategy) baseStrategy;
            strategy.incrementProductCounter();
        }
    }

    @Override
    public double calculateNutrients(String gender, int age, double weight, double height, double modificationPercentage) {
        double result = baseStrategy.calculateNutrients(gender, age, weight, height, modificationPercentage);

        return result;
    }

    @Override
    public String getDescription() {
        return baseStrategy.getDescription() + ". " + getRandomMotivationalSpeech();
    }

    @Override
    public void setMotivationalSpeechEnabled(boolean motivationalSpeechEnabled) {
        baseStrategy.setMotivationalSpeechEnabled(motivationalSpeechEnabled);
    }

    @Override
    public String getRandomMotivationalSpeech() {
        if (baseStrategy instanceof DietStrategy) {
            return baseStrategy.getRandomMotivationalSpeech();
        }
        return "";
    }
}