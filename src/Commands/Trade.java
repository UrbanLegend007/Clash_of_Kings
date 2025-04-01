package Commands;

import World.CommandManager;
import World.Kingdom;
import java.util.HashMap;
import java.util.Scanner;

public class Trade extends Command {

    private CommandManager worldCommandManager;
    private static final HashMap<String, Integer> itemValues = new HashMap<>();
    private Inventory inventory = new Inventory();
    private Scanner scanner = new Scanner(System.in);

    public Trade(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    static {
        itemValues.put("resources", 1);
        itemValues.put("scrolls", 2);
        itemValues.put("metals", 3);
        itemValues.put("krystals", 4);
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        try {

            if(currentKingdom.isConquered().equals("not conquered") && currentKingdom.getBattle().equals("Battling")){
                return "\nYou can't trade with this kingdom while you are at war.";
            } else if(currentKingdom.isConquered().equals("conquered")){
                return "\nThis is already your kingdom.";
            } else if(currentKingdom.isConquered().equals("not conquered") && currentKingdom.getBattle().equals("Not Battling")){

                System.out.print("\nEnter item to get from trade (resources, scrolls, metals, krystals): ");
                String request = scanner.nextLine();

                if(currentKingdom.inventoryAmount(itemValues.get(request), "items") <= 0){
                    return "\nThis kingdom has no " + request + " to trade.";
                } else if(!itemValues.containsKey(request)){
                    return "\nThere in not any " + request + " in this kingdom.";
                }

                int availableAmount = currentKingdom.inventoryAmount(itemValues.get(request), "items");

                if(availableAmount <= 0){
                    return "\nNo " + request + " available in this kingdom.";
                }

                System.out.print("\nEnter what you offer (resources, scrolls, metals, krystals): ");
                String offeredItem = scanner.nextLine();

                if (!itemValues.containsKey(offeredItem)) {
                    return "You can't trade with that item.";
                }

                int offerValue = itemValues.get(offeredItem);

                if (inventory.getResourceAmount(offerValue) > 0){

                    int requiredValue = getRequiredValue(request);

                    if (offerValue >= requiredValue) {
                        currentKingdom.collectItems(request, 1, "items");
                        currentKingdom.addItems(offeredItem, 1, "items");
                        switch(offeredItem){
                            case "resources":
                                inventory.removeItem(1, 1);
                                break;
                            case "scrolls":
                                inventory.removeItem(2, 1);
                                break;
                            case "metals":
                                inventory.removeItem(3, 1);
                                break;
                            case "krystals":
                                inventory.removeItem(4, 1);
                                break;
                        }
                        switch(request){
                            case "resources":
                                inventory.addItem(1, 1);
                                break;
                            case "scrolls":
                                inventory.addItem(2, 1);
                                break;
                            case "metals":
                                inventory.addItem(3, 1);
                                break;
                            case "krystals":
                                inventory.addItem(4, 1);
                                break;
                        }
                        currentKingdom.setLoyalty(1);
                        return "\nOffer accepted. You traded 1 " + offeredItem + ".\nYou have collected 1 " + request + ".";

                    } else {
                        return "\nTrade rejected. Your offer was too low.";
                    }

                } else {
                    return "\nYou don't have any " + offeredItem + ".";
                }

            } else {
                return "\nError while trading.";
            }
        } catch (Exception e) {
            return "An error occurred while trading.";
        }
    }

    private int getRequiredValue(String resource) {
        switch (resource) {
            case "resources": return 1;
            case "scrolls": return 2;
            case "metals": return 3;
            case "krystals": return 4;
            default: return 1;
        }
    }

    @Override
    public boolean exit() {
        return false;
    }
}
