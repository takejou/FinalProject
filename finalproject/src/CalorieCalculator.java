public class CalorieCalculator implements CalculatorInterface {

    public static double calculateCalories(String gender, int age, double weight, double height) {
        double calories;
        /// equation
        if (gender.equalsIgnoreCase("male")) {
            if (age < 3) {
                calories = 59.512 * weight - 30.4;
            } else if (age >= 3 && age <= 10) {
                calories = 22.706 * weight + 504.3;
            } else if (age > 10 && age <= 18) {
                calories = 17.686 * weight + 658.2;
            } else if (age > 18 && age <= 30) {
                calories = 15.057 * weight + 692.2;
            } else if (age > 30 && age <= 60) {
                calories = 11.472 * weight + 873.1;
            } else {
                calories = 11.711 * weight + 587.7;
            }
        } else if (gender.equalsIgnoreCase("female")) {
            if (age < 3) {
                calories = 58.317 * weight - 31.1;
            } else if (age >= 3 && age <= 10) {
                calories = 20.315 * weight + 485.9;
            } else if (age > 10 && age <= 18) {
                calories = 13.384 * weight + 692.6;
            } else if (age > 18 && age <= 30) {
                calories = 14.818 * weight + 486.6;
            } else if (age > 30 && age <= 60) {
                calories = 8.126 * weight + 845.6;
            } else {
                calories = 9.082 * weight + 658.5;
            }
        } else {
            throw new IllegalArgumentException("Invalid gender");
        }

        return calories;
    }
}