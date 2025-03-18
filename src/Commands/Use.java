package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

public class Use extends Command {

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);
    private Inventory inventory = new Inventory();

    public Use(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.println("Enter the amount of krystals you would like to use:");
        int krystal = scanner.nextInt();
        if(inventory.getResourceAmount(4) >= krystal){
            inventory.removeItem(4, krystal);
            return "You have used " + krystal + " krystals.";
        }else{
            return "You don't have enough krystals left";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
