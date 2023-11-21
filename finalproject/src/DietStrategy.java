public interface DietStrategy {
    double calculateNutrients(String gender, int age, double weight, double height, double modificationPercentage);
    String getDescription();
    void setMotivationalSpeechEnabled(boolean motivationalSpeechEnabled);
    String getRandomMotivationalSpeech();
    void incrementProductCounter();
}
