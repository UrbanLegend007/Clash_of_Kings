package Commands;

import World.CommandManager;
import World.Kingdom;
import java.util.HashMap;
import java.util.Scanner;

public class Trade extends Command {

    private CommandManager worldCommandManager;
    private static final HashMap<String, Integer> itemValues = new HashMap<>();
    private int items = 1;
    private int kingItems = 10;

    public Trade(CommandManager worldCommandManager) {
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
        String result = "";

        try (Scanner scanner = new Scanner(System.in)) {
            if(worldCommandManager.atWar()) {
                return "\nYou are at war.\nYou cannot currently trade.";
            } else {
                System.out.print("Enter item to trade (krystals, resources, scrolls, metals): ");
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

                if (offerValue * amount >= requiredValue && kingItems > 0) {
                    items += kingItems;
                    kingItems = 0;
                    currentKingdom.setLoyalty(1);
                    result = "Offer accepted. You traded all " + offeredItem + ".\nYou have collected all " + kingItems + " " + request + " and now you have " + items + " items.";
                } else if (kingItems <= 0) {
                    result = "No " + request + " available in this kingdom.";
                } else {
                    result = "Trade rejected. Your offer was too low.";
                }
            }
        } catch (Exception e) {
            result = "An error occurred. Please try again.";
        }

        return result;
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
