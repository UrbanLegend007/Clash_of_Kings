package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Get extends Command{

    private CommandManager worldCommandManager;
    private Inventory inventory = new Inventory();
    private Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Integer> itemValues = new HashMap<>();

    public Get(CommandManager worldCommandManager) {
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
                return "\nYou can't get resources from this kingdom while you are at war.";
            } else if(currentKingdom.isConquered().equals("conquered")){
                return "\nThis is already your kingdom.";
            } else {

                System.out.print("\nEnter resources to get (resources, scrolls, metals, krystals): ");
                String request = scanner.nextLine();

                if(currentKingdom.inventoryAmount(itemValues.get(request), "resources") <= 0){
                    return "\nThis kingdom has no " + request + " to trade.";
                } else if(!itemValues.containsKey(request)){
                    return "\nThere in not any " + request + " in this kingdom.";
                }

                int availableAmount = currentKingdom.inventoryAmount(itemValues.get(request), "resources");

                if(availableAmount <= 0){
                    return "\nNo " + request + " available in this kingdom.";
                }

                if(currentKingdom.isConquered().equals("conquered")) {
                    currentKingdom.collectItems(request, availableAmount, "resources");

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

                    return "\nYou have collected all " + availableAmount + " " + request + ".";

                } else if(currentKingdom.isConquered().equals("not conquered") && currentKingdom.getBattle().equals("Not Battling")){
                    System.out.print("\nEnter what you offer (resources, scrolls, metals, krystals): ");
                    String offeredItem = scanner.nextLine();

                    if (!itemValues.containsKey(offeredItem)) {
                        return "\nYou can't offer that item.";
                    }

                    int offerValue = itemValues.get(offeredItem);

                    if (inventory.getResourceAmount(offerValue) > 0){

                        int requiredValue = getRequiredValue(request);

                        if (offerValue >= requiredValue) {
                            currentKingdom.collectItems(request, availableAmount, "resources");
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
                            currentKingdom.setLoyalty(1);
                            return "\nOffer accepted. You traded 1 " + offeredItem + ".\nYou have collected all " + availableAmount + " " + request + ".";
                        } else {
                            return "\nTrade rejected. Your offer was too low.";
                        }
                    } else {
                        return "\nYou don't have any " + offeredItem + ".";

                    }
                } else {
                    return "\nError while getting items.";
                }
            }

        } catch (InputMismatchException e) {
            return "\nInvalid amount entered. Please enter a valid integer.";
        } catch (Exception e) {
            return "\nError getting resources.";
        }
    }

    private int getRequiredValue(String resource) {
        switch (resource) {
            case "resources":
                return 1;
            case "scrolls":
                return 2;
            case "metals":
                return 3;
            case "krystals":
                return 4;
            default: return 1;
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
