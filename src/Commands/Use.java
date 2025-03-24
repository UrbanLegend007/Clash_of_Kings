package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

public class Use extends Command {

    private CommandManager worldCommandManager;
    private Scanner scanner = new Scanner(System.in);
    private Inventory inventory = new Inventory();

    public Use(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        String result = "";
        try {
            System.out.println("Enter the amount of krystals you would like to use:");
            int krystal = scanner.nextInt();

            if (inventory.getResourceAmount(4) >= krystal) {
                inventory.removeItem(4, krystal);
                result = "You have used " + krystal + " krystals.";
            } else {
                result = "You don't have enough krystals left";
            }
        } catch (Exception e) {
            result = "Invalid input! Please enter a valid number.";
        } finally {
            scanner.close();
        }
        return result;
    }

    @Override
    public boolean exit() {
        return false;
    }

}
