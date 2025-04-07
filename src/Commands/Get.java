package Commands;

import World.CommandManager;
import World.Kingdom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Příkaz Get umožňuje hráči získat suroviny z království,
 * a to buď obchodem, nebo pokud je království dobyto, tak bezplatně.
 */
public class Get extends Command{

    private CommandManager worldCommandManager;
    private Inventory inventory = new Inventory();
    private Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Integer> itemValues = new HashMap<>();

    /**
     * Konstruktor třídy Get.
     * @param worldCommandManager Správce příkazů ve hře.
     */
    public Get(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
        loadItemValues();
    }

    /**
     * Vrací hodnotu potřebnou k výměně dané suroviny.
     * @param resource Název suroviny.
     * @return Hodnota požadované suroviny.
     */
    private int getRequiredValue(String resource) {
        return switch (resource) {
            case "resources" -> itemValues.get("resources");
            case "scrolls" -> itemValues.get("scrolls");
            case "metals" -> itemValues.get("metals");
            case "krystals" -> itemValues.get("krystals");
            default -> 1;
        };
    }

    public void loadItemValues() {
        try (BufferedReader br = new BufferedReader(new FileReader("res/itemValues"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                itemValues.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            System.out.println("Error loading item values from file.");
        } catch (Exception e) {
            System.out.println("Error loading item values.");
        }
    }

    /**
     * Provede příkaz získání surovin z aktuálního království.
     * Hráč může suroviny získat zdarma, pokud království dobyl,
     * nebo musí provést obchod, pokud království není jeho.
     * @return Výsledek operace jako textová zpráva.
     */
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

                if(currentKingdom.isConquered().equals("conquered")) {
                    currentKingdom.collectItems(request, availableAmount, "resources");

                    switch(request){
                        case "resources":
                            inventory.addItem(1, availableAmount);
                            break;
                        case "scrolls":
                            currentKingdom.setScrolls();
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

                        System.out.println("\nEnter the amount you would like to offer: ");
                        int amount = 1;
                        try{
                            amount = scanner.nextInt();
                        } catch (Exception e){
                            return "\nInvalid amount.";
                        }

                        if(amount <= 0){
                            return "\nYou can't offer that amount.";
                        } else if(amount > inventory.getResourceAmount(offerValue)){
                            return "\nYou don't have that amount.";
                        }

                        int requiredValue = getRequiredValue(request);

                        if (offerValue * amount >= requiredValue) {
                            currentKingdom.collectItems(request, availableAmount, "resources");
                            switch(offeredItem){
                                case "resources":
                                    inventory.removeItem(1, amount);
                                    currentKingdom.addItems(offeredItem,amount, "items");
                                    break;
                                case "scrolls":
                                    inventory.removeItem(2, amount);
                                    currentKingdom.addItems(offeredItem,amount, "items");
                                    break;
                                case "metals":
                                    inventory.removeItem(3, amount);
                                    currentKingdom.addItems(offeredItem,amount, "items");
                                    break;
                                case "krystals":
                                    inventory.removeItem(4, amount);
                                    currentKingdom.addItems(offeredItem,amount, "items");
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

    /**
     * Určuje, zda tento příkaz má ukončit běh.
     * @return Vždy vrací false.
     */
    @Override
    public boolean exit() {
        return false;
    }

}
