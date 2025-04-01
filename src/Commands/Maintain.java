package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

public class Maintain extends Command {

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);
    private Inventory inventory = new Inventory();

    public Maintain(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {

        try {
            System.out.print("\nEnter, if you want to maintain: \n1) properties \n2) gear\n");
            String request = scanner.nextLine();

            if (request.equals("1") || request.equals("properties")) {

                if(inventory.getResourceAmount(1) > 0){
                    getMyKingdom().setStrength(0.1);
                    return "\nYou maintained your properties and now your army is stronger by 0,1.";
                } else {
                    return "\nNot enough resources to maintain.";
                }

            } else if (request.equals("2") || request.equals("gear")) {

                if(inventory.getResourceAmount(3) > 0){
                    getMyKingdom().setStrength(0.2);
                    return "\nYou maintained your gear and now your army is stronger by 0,2.";
                } else {
                    return "\nNot enough metals to maintain.";
                }
            } else {
                return "\nInvalid input.";
            }
        } catch (Exception e) {
            return "\nAn error occurred while attempting to maintain.";
        }
    }

    private Kingdom getMyKingdom() {
        return worldCommandManager.world.get(worldCommandManager.start);
    }

    @Override
    public boolean exit() {
        return false;
    }
}
