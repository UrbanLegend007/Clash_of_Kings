package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.HashMap;
import java.util.Scanner;

public class Get extends Command{

    private CommandManager worldCommandManager;
    private Inventory inventory = new Inventory();
    Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Integer> itemValues = new HashMap<>();

    public Get(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    static {
        itemValues.put("krystals", 30);
        itemValues.put("resources", 15);
        itemValues.put("scrolls", 35);
        itemValues.put("metals", 20);
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.print("Enter item to get (krystals, resources, scrolls, metals): ");
        String request = scanner.nextLine();

        if (!currentKingdom.getResources().containsKey(request) || currentKingdom.getResources().get(request) == 0) {
            return "This kingdom has no " + request + " to trade.";
        }

        System.out.print("Enter what you offer (krystals, resources, scrolls, metals, help): ");
        String offeredItem = scanner.nextLine();

        if (!itemValues.containsKey(offeredItem)) {
            return "You can't trade with that item.";
        }

        System.out.println("Enter the amount you would like to offer: ");
        int amount = scanner.nextInt();

        int offerValue = itemValues.get(offeredItem);
        int requiredValue = getRequiredValue(request);
        int availableAmount = currentKingdom.getResources().getOrDefault(request, 0);

        if (offerValue*amount >= requiredValue && availableAmount > 0) {
            currentKingdom.collectItems(request, availableAmount, "items");
            switch(offeredItem){
                case "resources":
                    inventory.removeItem(1, amount);
                    break;
                case "scrolls":
                    inventory.removeItem(2, amount);
                    break;
                case "metals":
                    inventory.removeItem(3, amount);
                    break;
                case "krystals":
                    inventory.removeItem(4, amount);
                    break;
            }
            switch(request){
                case "resources":
                    inventory.addItem(1, availableAmount);
                    break;
                case "scrolls":
                    inventory.addItem(2, availableAmount);
                    break;
                case "metals":
                    inventory.addItem(3, availableAmount);
                    break;
                case "krystals":
                    inventory.addItem(4, availableAmount);
                    break;
            }
            return "Offer accepted. You traded " + amount + " " + offeredItem + ".\nYou have collected all " + availableAmount + " " + request + ".";
        } else if(availableAmount <= 0){
            return "No " + request + " available in this kingdom.";
        } else {
            return "Trade rejected. Your offer was too low.";
        }
    }

    private int getRequiredValue(String resource) {
        switch (resource) {
            case "krystals": return 40;
            case "resources": return 25;
            case "scrolls": return 35;
            case "metals": return 30;
            default: return 50;
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
