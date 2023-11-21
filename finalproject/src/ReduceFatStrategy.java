public class ReduceFatStrategy implements DietStrategy {
    private boolean motivationalSpeechEnabled = false;
    private static final String[] MOTIVATIONAL_PHRASES = {
            "Stay motivated! Keep working towards your weight gain goal!",
            "Believe in yourself! You can achieve your weight gain target!",
            "Persistence is key! Your efforts will pay off!",
            "Keep pushing! Your journey to gaining weight is a step closer."
    };
    @Override
    public double calculateNutrients(String gender, int age, double weight, double height, double modificationPercentage) {
        double calories = CalorieCalculator.calculateCalories(gender, age, weight, height);
        double modifiedCalories = calories * (1.0 - Math.abs(modificationPercentage) / 100.0);

        return modifiedCalories;
    }
    private int productCounter = 0;

    @Override
    public void incrementProductCounter() {
        productCounter++;
    }
    @Override
    public String getDescription() {
        return "Reduce Fat Strategy";
    }
    @Override
    public String getRandomMotivationalSpeech() {
        int randomIndex = (int) (Math.random() * MOTIVATIONAL_PHRASES.length);
        return MOTIVATIONAL_PHRASES[randomIndex];
    }
    @Override
    public void setMotivationalSpeechEnabled(boolean motivationalSpeechEnabled) {
        this.motivationalSpeechEnabled = motivationalSpeechEnabled;
    }

}
