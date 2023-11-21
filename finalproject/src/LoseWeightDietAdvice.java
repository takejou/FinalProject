public class LoseWeightDietAdvice implements DietAdvice {
    @Override
    public void provideAdvice() {
        System.out.println("Here are some tips for losing weight:");
        System.out.println("1. Create a calorie deficit by consuming fewer calories than you burn.");
        System.out.println("2. Include more vegetables and whole foods in your meals.");
        System.out.println("3. Engage in regular cardiovascular exercises.");
    }
}