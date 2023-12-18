package machine;

import java.util.Scanner;

public class CoffeeMachine {
    final int numCups = 1;
    final String[] ingredients = {"water", "milk", "coffee beans"};
    final String[] units = {"ml", "ml", "g"};
    final int[][] allQuantitiesPerCup = {
            {250, 0, 16},
            {350, 75, 20},
            {200, 100, 12}
    };
    final int[] costs = {4, 7, 6};
    int disposableCups = 9;
    int money = 550;
    int[] availableQuantities = {400, 540, 120};
    int[] neededQuantities = new int[3];
    int[] possibleCupsPerIngredient = new int[3];
    boolean enoughQuantities = true;
    int possibleCups;
    private final Scanner scanner = new Scanner(System.in);
    boolean[] lackIngredients = {false, false, false};


    public CoffeeMachine() {
    }

    public static void main(String[] args) {
        CoffeeMachine machine = new CoffeeMachine();
        machine.processInput();
    }

    private void processInput() {
        boolean breakLoop = false;
        while (true) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            String action = scanner.next();
            System.out.println();
            switch (action) {
                case "buy":
                    buy();
                    break;
                case "fill":
                    fill();
                    break;
                case "take":
                    take();
                    break;
                case "remaining":
                    showState();
                    break;
                case "exit":
                    breakLoop = true;
                    break;
                default:
                    break;
            }
            if (breakLoop) {
                break;
            }
            System.out.println();
        }

    }

    private void buy() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String action = scanner.next();
        if ("back".equals(action)) {
            return;
        }
        int drinkIndex = Integer.parseInt(action) - 1;
        buyDrink(drinkIndex);
    }

    public void buyDrink(int index) {
        int[] quantitiesPerCup = allQuantitiesPerCup[index];
        computeNeededQuantities(quantitiesPerCup);
        determineEnoughQuantities();
        if (!enoughQuantities) {
            showLackingIngredients();
            enoughQuantities = true;
            return;
        }
        System.out.println("I have enough resources, making you a coffee!");
        for (int i = 0; i < quantitiesPerCup.length; i++) {
            availableQuantities[i] -= neededQuantities[i];
        }
        money += costs[index];
        disposableCups--;
    }

    public void fill() {
        String[] messages = {
                "Write how many ml of water you want to add:",
                "Write how many ml of milk you want to add:",
                "Write how many grams of coffee beans you want to add:"
        };
        for (int i = 0; i < messages.length; i++) {
            System.out.println(messages[i]);
            availableQuantities[i] += scanner.nextInt();
        }
        System.out.println("Write how many disposable cups you want to add:");
        disposableCups += scanner.nextInt();
    }

    private void take() {
        System.out.printf("I gave you $%d", money);
        money = 0;
    }


    private void computePossibleCupsPerIngredient(int[] quantitiesPerCup) {
        for (int i = 0; i < availableQuantities.length; i++) {
            possibleCupsPerIngredient[i] = availableQuantities[i] / quantitiesPerCup[i];
        }
    }

    private void computeNeededQuantities(int[] quantitiesPerCup) {
        for (int i = 0; i < availableQuantities.length; i++) {
            neededQuantities[i] = quantitiesPerCup[i] * numCups;
        }
    }

    private void determineEnoughQuantities() {
        for (int i = 0; i < neededQuantities.length; i++) {
            if (availableQuantities[i] < neededQuantities[i]) {
                lackIngredients[i] = true;
                enoughQuantities = false;
                break;
            }
        }
    }


    public void showState() {
        System.out.print("The coffee machine has:");
        for (int i = 0; i < ingredients.length; i++) {
            String ingredient = ingredients[i];
            int quantity = availableQuantities[i];
            String unit = units[i];
            System.out.printf("%n%d %s of %s", quantity, unit, ingredient);
        }
        System.out.printf("%n%d disposable cups", disposableCups);
        System.out.printf("%n$%d of money", money);
        System.out.print("\n\n");
    }

    public void showLackingIngredients() {
        for (int i = 0; i < lackIngredients.length; i++) {
            if (lackIngredients[i]) {
                System.out.printf("Sorry, not enough %s!%n", ingredients[i]);
            }
        }
    }
}
