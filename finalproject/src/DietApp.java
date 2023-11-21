import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DietApp {
    private static Connection connection;
    private static DietStrategy primaryGoalStrategy;
    private static DietStrategy weightLossStrategy;
    private static DietStrategy weightGainStrategy;
    private static boolean primaryGoalQuestionAsked = false;
    private static double modificationPercentage = 0.0;
    private static double totalCalories = 0.0;

    public static void main(String[] args) {
        initializeDatabaseConnection();
        Scanner scanner = new Scanner(System.in);
        FitnessTracker fitnessTracker = new FitnessTracker();
        FitnessAdapter fitnessAdapter = new FitnessTrackerAdapter(fitnessTracker);
        DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance();
        double caloriesBurned = fitnessAdapter.getCaloriesBurned();
        int stepsCount = fitnessAdapter.getStepsCount();

        System.out.println("Calories Burned: " + caloriesBurned);
        System.out.println("Steps Count: " + stepsCount);
        System.out.println("\n\033[0;33m\033[1;4YYour diet assistant\033[0m");

        System.out.print("\n\033[0;36m\033[1;4NName:\033[0m ");
        String name = scanner.nextLine();

        System.out.print("\n\033[0;36m\033[1;4WWeight (kg):\033[0m ");
        double weight = scanner.nextDouble();

        System.out.print("\n\033[0;36m\033[1;4HHeight (cm):\033[0m ");
        double height = scanner.nextDouble();

        System.out.print("\n\033[0;36m\033[1;4AAge:\033[0m  ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("\n\033[0;36m\033[1;4GGender (male/female):\033[0m ");
        String gender = scanner.nextLine();

        primaryGoalStrategy = new MotivationalSpeechDecorator(choosePrimaryGoalStrategy(scanner));

        if (!primaryGoalQuestionAsked) {
            System.out.println("\n\033[0;36m\033[1;4dDo you want to modify calories? (yes/no):\033[0m ");
            String modifyChoice = scanner.nextLine();

            if (modifyChoice.equalsIgnoreCase("yes")) {
                System.out.print("Choose this range to facilitate weight loss. You can select the intensity based on your preference. 1 to 15: Opt for this range to support weight gain. We recommend a moderate pace for your health and well-being. (Feel free to choose your preferred percentage, but we advise against more aggressive approaches for your health. 1-15 represents a moderate pace, 16-25 is a moderate pace, and 26-35 is a rapid pace for weight gain or loss). Please enter a modification percentage within the specified ranges to customize your diet plan. \n\033[0;35m\033[1;4EEnter Modification Percentage -40 to 40:\033[0m ");
                modificationPercentage = scanner.nextDouble();
                scanner.nextLine();
            }

            primaryGoalQuestionAsked = true;
        }

        System.out.print("\n\033[0;36m\033[1;4EEnter desired weight change (in kg):\033[0m ");
        double desiredWeightChange = scanner.nextDouble();
        scanner.nextLine();
        DietPlanObserver dietPlanObserver = new DietPlanObserver(primaryGoalStrategy);
        while (true) {
            double modifiedCalories = primaryGoalStrategy.calculateNutrients(gender, age, weight, height, modificationPercentage);
            int daysToReachGoal = calculateDaysToReachGoal(modificationPercentage, weight, desiredWeightChange);

            displayDietPlanInformation(primaryGoalStrategy, modifiedCalories, caloriesBurned, daysToReachGoal);

            addProductToDailyCalories(scanner);

            System.out.print("\n\033[0;36m\033[1;4DAdd more products? (yes/no):\033[0m ");
            String addMoreChoice = scanner.nextLine();

            while (!addMoreChoice.equalsIgnoreCase("yes") && !addMoreChoice.equalsIgnoreCase("no")) {
                addMoreChoice = scanner.nextLine();
            }

            if (addMoreChoice.equalsIgnoreCase("no")) {
                break; // Exit the loop if the user enters "no"
            }
            ((MotivationalSpeechDecorator) primaryGoalStrategy).incrementProductCounter();
        }

        System.out.println("\nChoose an option:");
        System.out.println("1. Finish and display total calories");
        System.out.println("2. Diet tips");
        System.out.println("3. Change personal information");

        int menuChoice = scanner.nextInt();
        scanner.nextLine();

        DietPlanObserver menuObserver = new DietPlanObserver(primaryGoalStrategy);
        menuObserver.setTargetCalories(desiredWeightChange);
        switch (menuChoice) {
            case 1:
                menuObserver.setDisplayTotalCalories(true);
                boolean goalAchieved = checkGoalAchievement(primaryGoalStrategy, totalCalories, menuObserver.getTargetCalories());
                menuObserver.update(totalCalories, menuObserver.getTargetCalories(), null);
                break;
                // some advices if you need
            case 2:
                DietAdvice dietAdvice = DietAdviceFactory.createDietAdvice(primaryGoalStrategy);
                if (dietAdvice != null) {
                    dietAdvice.provideAdvice();
                } else {
                    System.out.println("1. Increase your protein intake.");
                    System.out.println("2. Focus on strength training exercises.");
                    System.out.println("3. Ensure you are consuming enough calories for muscle growth.");
                }
                break;

            case 3:
                choosePrimaryGoalStrategy(scanner);
                break;
            default:
                System.out.println("Wrong choice!");
                System.exit(0);
        }

    }

    private static boolean checkGoalAchievement(DietStrategy strategy, double totalCalories, double targetCalories) {
        if (strategy instanceof LoseWeightStrategy) {
            return totalCalories < targetCalories;
        } else if (strategy instanceof GainWeightStrategy || strategy instanceof GainMuscleMassStrategy) {
            return totalCalories >= targetCalories;
        }
        return false;
    }

    private static void initializeDatabaseConnection() {
        DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance();
        connection = databaseSingleton.getConnection();
        String jdbcURL = "jdbc:postgresql://localhost:5432/dietdatabase";
        String user = "postgres";
        String password = "Gantzl10";

        try {
            connection = DriverManager.getConnection(jdbcURL, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeDatabaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance();
        databaseSingleton.closeConnection();
    }

    private static DietStrategy choosePrimaryGoalStrategy(Scanner scanner) {
        System.out.println("\n\033[0;36m\033[1;4CChoose your primary goal:\033[0m ");
        System.out.println("1. Lose Weight");
        System.out.println("2. Gain Weight");

        int primaryGoalChoice = scanner.nextInt();
        scanner.nextLine();

        switch (primaryGoalChoice) {
            case 1:
                return chooseWeightLossStrategy(scanner);
            case 2:
                return chooseWeightGainStrategy(scanner);
            default:
                System.out.println("Wrong answer!");
                System.exit(0);
                return null;
        }
    }

    private static DietStrategy chooseWeightLossStrategy(Scanner scanner) {
        if (weightLossStrategy == null) {
            System.out.println("\n\033[0;36m\033[1;4CChoose your weight loss strategy:\033[0m ");
            System.out.println("1. Lose Fat");
            System.out.println("2. Lose Total Weight");

            int weightLossChoice = scanner.nextInt();
            scanner.nextLine();

            switch (weightLossChoice) {
                case 1:
                    weightLossStrategy = new LoseWeightStrategy();
                    break;
                case 2:
                    weightLossStrategy = new GainWeightStrategy();
                    break;
                default:
                    System.out.println("Wrong answer!");
                    System.exit(0);
            }
        }
        return weightLossStrategy;
    }

    private static DietStrategy chooseWeightGainStrategy(Scanner scanner) {
        if (weightGainStrategy == null) {
            System.out.println("\n\033[0;36m\033[1;4CChoose your weight gain strategy:\033[0m ");
            System.out.println("1. Gain Total Weight");
            System.out.println("2. Gain Muscle Mass");

            int weightGainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (weightGainChoice) {
                case 1:
                    weightGainStrategy = new GainWeightStrategy();
                    break;
                case 2:
                    weightGainStrategy = new GainMuscleMassStrategy();
                    break;
                default:
                    System.out.println("Wrong answer!");
                    System.exit(0);
            }
        }
        return weightGainStrategy;
    }

    private static int calculateDaysToReachGoal(double modificationPercentage, double initialWeight, double desiredWeightChange) {
        double daysToReachGoal = Math.abs(desiredWeightChange) / (Math.abs(0.01 * modificationPercentage) * initialWeight / 100.0);
        return (int) Math.ceil(daysToReachGoal);
    }

    private static void displayDietPlanInformation(DietStrategy strategy, double calories, double caloriesBurned, int daysToReachGoal) {
        System.out.println("\n\033[0;32m\033[1;4YYour diet plan:\033[0m " + strategy.getDescription());
        System.out.println("\n\033[0;32m\033[1;MModified daily calorie intake:\033[0m  " + (calories + caloriesBurned) + " kcal");
        System.out.println("\n\033[0;32m\033[1;DDays to reach goal weight:\033[0m  " + daysToReachGoal + " days");
    }

    private static void addProductToDailyCalories(Scanner scanner) {
        System.out.println("\n\033[0;36m\033[1;4PAdd product to daily calories:\033[0m ");

        try {
            // list of products
            PreparedStatement statement = connection.prepareStatement("SELECT id, name, calories FROM products");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String productName = resultSet.getString("name");
                double productCalories = resultSet.getDouble("calories");
                System.out.println(productId + ". " + productName + " - " + productCalories + " kcal");
            }

            while (true) {
                System.out.print("Enter the product ID (or 'done' to finish): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("done")) {
                    break;
                }

                int selectedProductId = Integer.parseInt(input);

                System.out.print("Enter the quantity in grams: ");
                double quantity = scanner.nextDouble();

                double calories = getProductCalories(selectedProductId);
                double productTotalCalories = (calories / 100.0) * quantity;

                totalCalories += productTotalCalories;

                System.out.println("Added " + quantity + " grams of the selected product.");
                System.out.println("Total calories added: " + productTotalCalories + " kcal");
                System.out.println("Total calories so far: " + totalCalories + " kcal");

                scanner.nextLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double getProductCalories(int productId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT calories FROM products WHERE id = ?");
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("calories");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}
