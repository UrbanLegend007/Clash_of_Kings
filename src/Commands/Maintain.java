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
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.print("Enter, if you want to maintain: \n1) gear \n2) properties\n");
        String request = scanner.nextLine();

        if(request.equals("1") || request.equals("gear")){
            System.out.print("Enter amount of metals you want to use to maintain: \n");
            int metal = scanner.nextInt();
            if(metal > 0 && metal <= inventory.getResourceAmount(3)){
                inventory.removeItem(3, metal);
                return "You maintained gears for your army with " + metal + " metals.";
            }else if(metal > inventory.getResourceAmount(3)){
                return "Not enough metals for maintaining gear.";
            }else{
                return "Invalid amount of metals.";
            }

        }else if(request.equals("2") || request.equals("properties")){
            System.out.print("Enter amount of resources you want to use to maintain: \n");
            int resources = scanner.nextInt();
            if(resources > 0 && resources <= inventory.getResourceAmount(1)){
                inventory.removeItem(1, resources);
                return "You maintained properties of your army with " + resources + " resources.";
            }else if(resources > inventory.getResourceAmount(1)){
                return "Not enough resources for maintaining properties.";
            }else{
                return "Invalid amount of resources.";
            }
        }else{
            return "Invalid input.";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
