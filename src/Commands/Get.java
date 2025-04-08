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
 * Třída Get představuje příkaz, který umožňuje hráči získat suroviny z aktuálního království.
 * Získání může proběhnout zdarma, pokud je království dobyté, nebo obchodem, pokud dobyté není.
 */
public class Get extends Command {

    private CommandManager worldCommandManager;
    private Inventory inventory = new Inventory();
    private Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Integer> itemValues = new HashMap<>();

    /**
     * Konstruktor třídy Get.
     *
     * @param worldCommandManager správce příkazů světa, umožňující přístup ke královstvím a jejich stavům.
     */
    public Get(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
        loadItemValues();
    }

    /**
     * Vrací hodnotu požadované suroviny na základě jejího názvu.
     *
     * @param resource název suroviny (např. "resources", "scrolls", atd.)
     * @return hodnota požadovaná k výměně dané suroviny
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

    /**
     * Načte hodnoty jednotlivých surovin ze souboru "res/itemValues" a uloží je do mapy itemValues.
     * Očekává se formát řádku: název,hodnota
     */
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
     * Vykoná samotný příkaz získání surovin z aktuálního království.
     * Hráč může:
     * - Získat suroviny zdarma, pokud je království dobyto.
     * - Nabídnout jinou surovinu k výměně, pokud království dobyté není.
     *
     * @return textová zpráva informující o výsledku pokusu o získání surovin.
     */
    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        try {
            if (worldCommandManager.currentPosition == 1) {
                return "\nThis is your kingdom.";
            }
            if (currentKingdom.isConquered().equals("not conquered") &&
                    currentKingdom.getBattle().equals("Battling")) {
                return "\nYou can't get resources from this kingdom while you are at war.";
            } else {

                System.out.print("\nEnter resources to get (resources, scrolls, metals, krystals): ");
                String request = scanner.nextLine();

                if (currentKingdom.inventoryAmount(itemValues.get(request), "resources") <= 0) {
                    return "\nThis kingdom has no " + request + " to trade.";
                } else if (!itemValues.containsKey(request)) {
                    return "\nThere in not any " + request + " in this kingdom.";
                }

                int availableAmount = currentKingdom.inventoryAmount(itemValues.get(request), "resources");

                if (currentKingdom.isConquered().equals("conquered")) {
                    // Hráč získává všechny suroviny zdarma (dobyto)
                    currentKingdom.collectItems(request, availableAmount, "resources");

                    switch (request) {
                        case "resources":
                            inventory.addItem(1, availableAmount);
                            break;
                        case "scrolls":
                            // Ošetření kapacity svitků
                            int scrollAmount;
                            if (currentKingdom.getSrcollsSize() + availableAmount > 21) {
                                scrollAmount = 21;
                            } else if (currentKingdom.getSrcollsSize() + availableAmount <= 0) {
                                scrollAmount = currentKingdom.getSrcollsSize();
                            } else {
                                scrollAmount = currentKingdom.getSrcollsSize() + availableAmount;
                            }
                            int index = currentKingdom.getSrcollsSize() == 0 ? 1 : currentKingdom.getSrcollsSize();
                            for (int i = index; i <= scrollAmount; i++) {
                                currentKingdom.setScrolls(i, true);
                            }
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

                } else if (currentKingdom.isConquered().equals("not conquered") &&
                        currentKingdom.getBattle().equals("Not Battling")) {
                    // Hráč obchoduje – nabízí vlastní surovinu na výměnu
                    System.out.print("\nEnter what you offer (resources, scrolls, metals, krystals): ");
                    String offeredItem = scanner.nextLine();

                    if (!itemValues.containsKey(offeredItem)) {
                        return "\nYou can't offer that item.";
                    }

                    int offerValue = itemValues.get(offeredItem);

                    if (inventory.getResourceAmount(offerValue) > 0) {
                        System.out.println("\nEnter the amount you would like to offer: ");
                        int amount;
                        try {
                            amount = scanner.nextInt();
                        } catch (Exception e) {
                            return "\nInvalid amount.";
                        }

                        if (amount <= 0) {
                            return "\nYou can't offer that amount.";
                        } else if (amount > inventory.getResourceAmount(offerValue)) {
                            return "\nYou don't have that amount.";
                        }

                        int requiredValue = getRequiredValue(request);

                        if (offerValue * amount >= requiredValue) {
                            // Výmena je přijata – provedou se úpravy obou inventářů
                            currentKingdom.collectItems(request, availableAmount, "resources");
                            currentKingdom.addItems(offeredItem, amount, "items");

                            switch (offeredItem) {
                                case "resources":
                                    inventory.removeItem(1, amount);
                                    break;
                                case "scrolls":
                                    if (currentKingdom.getSrcollsSize() - amount <= 0) {
                                        int index = currentKingdom.getSrcollsSize() > 0 ? currentKingdom.getSrcollsSize() : 1;
                                        for (int i = index; i >= 1; i--) {
                                            currentKingdom.setScrolls(i, false);
                                        }
                                    } else {
                                        int scrollAmount = currentKingdom.getSrcollsSize() - amount;
                                        for (int i = currentKingdom.getSrcollsSize(); i >= scrollAmount; i--) {
                                            currentKingdom.setScrolls(i, false);
                                        }
                                    }
                                    inventory.removeItem(2, amount);
                                    break;
                                case "metals":
                                    inventory.removeItem(3, amount);
                                    break;
                                case "krystals":
                                    inventory.removeItem(4, amount);
                                    break;
                            }

                            switch (request) {
                                case "resources":
                                    inventory.addItem(1, availableAmount);
                                    break;
                                case "scrolls":
                                    int scrollAmount;
                                    if (currentKingdom.getSrcollsSize() + availableAmount > 21) {
                                        scrollAmount = 21;
                                    } else if (currentKingdom.getSrcollsSize() + availableAmount <= 0) {
                                        scrollAmount = currentKingdom.getSrcollsSize();
                                    } else {
                                        scrollAmount = currentKingdom.getSrcollsSize() + availableAmount;
                                    }
                                    int index = currentKingdom.getSrcollsSize() > 0 ? currentKingdom.getSrcollsSize() : 1;
                                    for (int i = index; i <= scrollAmount; i++) {
                                        currentKingdom.setScrolls(i, true);
                                    }
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
     * Určuje, zda tento příkaz ukončuje běh hry.
     *
     * @return vždy vrací false (příkaz Get hru neukončuje).
     */
    @Override
    public boolean exit() {
        return false;
    }

}
