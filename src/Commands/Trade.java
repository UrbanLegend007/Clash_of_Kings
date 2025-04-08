package Commands;

import World.CommandManager;
import World.Kingdom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Třída {@code Trade} umožňuje obchodování s královstvím. Hráč může nabídnout
 * určité zboží a získat za něj jiné. Obchodování je možné pouze, pokud
 * království není v konfliktu nebo již dobyté.
 */
public class Trade extends Command {

    private CommandManager worldCommandManager;
    private Inventory inventory = new Inventory();
    private Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Integer> itemValues = new HashMap<>();

    /**
     * Konstruktor pro vytvoření příkazu pro obchodování.
     * Tento příkaz inicializuje správce příkazů {@code worldCommandManager},
     * který umožňuje správu světa a pozic hráče, a také načte hodnoty zboží
     * pro obchodování.
     *
     * @param worldCommandManager Instance CommandManager pro správu světa a pozic.
     */
    public Trade(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
        loadItemValues();
    }

    /**
     * Provede obchodování s aktuálním královstvím. Hráč může nabídnout
     * určité zboží a získat za něj jiné. Vrací zprávu o probíhajícím obchodu
     * a jeho výsledku.
     *
     * Obchodování je možné pouze, pokud království není v konfliktu a
     * není již dobyté. Hráč zadává zboží, které chce získat a nabídnout
     * na výměnu. Pokud je nabídka splněna, dojde k výměně zboží a zvýšení
     * loajality království.
     *
     * @return Výstupní zpráva o výsledku obchodu.
     */
    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        try {
            // Kontrola, zda je království v konfliktu nebo již dobyté
            if(currentKingdom.isConquered().equals("not conquered") && currentKingdom.getBattle().equals("Battling")){
                return "\nYou can't trade with this kingdom while you are at war.";
            } else if(currentKingdom.isConquered().equals("conquered")){
                return "\nThis is already your kingdom.";
            } else if(currentKingdom.isConquered().equals("not conquered") && currentKingdom.getBattle().equals("Not Battling")){

                // Žádost o zboží k získání
                System.out.print("\nEnter item to get from trade (resources, scrolls, metals, krystals): ");
                String request = scanner.nextLine();

                // Kontrola dostupnosti zboží pro obchod
                if(currentKingdom.inventoryAmount(itemValues.get(request), "items") <= 0){
                    return "\nThis kingdom has no " + request + " to trade.";
                } else if(!itemValues.containsKey(request)){
                    return "\nThere is no " + request + " in this kingdom.";
                }

                int availableAmount = currentKingdom.inventoryAmount(itemValues.get(request), "items");

                if(availableAmount <= 0){
                    return "\nNo " + request + " available in this kingdom.";
                }

                // Žádost o zboží k nabídce
                System.out.print("\nEnter what you offer (resources, scrolls, metals, krystals): ");
                String offeredItem = scanner.nextLine();

                // Kontrola, zda je zboží nabídnuté k obchodu možné vyměnit
                if (!itemValues.containsKey(offeredItem)) {
                    return "You can't trade with that item.";
                }

                int offerValue = itemValues.get(offeredItem);

                // Ověření, zda má hráč dostatečné množství nabídnutého zboží
                if (inventory.getResourceAmount(offerValue) > 0){

                    System.out.println("\nEnter the amount you would like to offer: ");
                    int amount;
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

                    // Ověření, zda nabídka splňuje požadavky
                    if (offerValue * amount >= requiredValue) {
                        // Provádí výměnu zboží
                        currentKingdom.collectItems(request, 1, "items");
                        currentKingdom.addItems(offeredItem, amount, "items");

                        // Odstraňuje položky z inventáře na základě nabídky
                        switch(offeredItem){
                            case "resources":
                                inventory.removeItem(1, amount);
                                break;
                            case "scrolls":
                                int scrollAmount;
                                if(currentKingdom.getSrcollsSize() - amount <= 0){
                                    int index;
                                    if(currentKingdom.getSrcollsSize() > 0){
                                        index = currentKingdom.getSrcollsSize();
                                    } else {
                                        index = 1;
                                    }
                                    for (int i = index; i >= 1; i--) {
                                        currentKingdom.setScrolls(i,false);
                                    }
                                } else if(currentKingdom.getSrcollsSize() - amount > 0 && currentKingdom.getSrcollsSize() - amount <= 21){
                                    scrollAmount = currentKingdom.getSrcollsSize() - amount;
                                    int index;
                                    if(currentKingdom.getSrcollsSize() > 0){
                                        index = currentKingdom.getSrcollsSize();
                                    } else {
                                        index = 1;
                                    }
                                    for (int i = index; i > scrollAmount; i--) {
                                        currentKingdom.setScrolls(i,false);
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

                        // Přidává nové položky na základě požadavku
                        switch(request){
                            case "resources":
                                inventory.addItem(1, 1);
                                break;
                            case "scrolls":
                                int scrollAmount;

                                if(currentKingdom.getSrcollsSize() + 1 > 21){
                                    scrollAmount = 21;
                                } else if(currentKingdom.getSrcollsSize() + 1 <= 0){
                                    scrollAmount = currentKingdom.getSrcollsSize();
                                } else {
                                    scrollAmount = currentKingdom.getSrcollsSize() + 1;
                                }
                                int index;
                                if(currentKingdom.getSrcollsSize() > 0){
                                    index = currentKingdom.getSrcollsSize();
                                } else {
                                    index = 1;
                                }
                                for (int i = index; i <= scrollAmount; i++) {
                                    currentKingdom.setScrolls(i, true);
                                }
                                inventory.addItem(2, 1);
                                break;
                            case "metals":
                                inventory.addItem(3, 1);
                                break;
                            case "krystals":
                                inventory.addItem(4, 1);
                                break;
                        }

                        // Zvýšení loajality království
                        currentKingdom.setLoyalty(1);
                        return "\nOffer accepted. You traded 1 " + offeredItem + ".\nYou have collected 1 " + request + ".";

                    } else {
                        return "\nTrade rejected. Your offer was too low.";
                    }

                } else {
                    return "\nYou don't have any " + offeredItem + ".";  // Pokud hráč nemá dostatek nabídnutého zboží
                }

            } else {
                return "\nError while trading.";  // V případě, že obchodování není možné
            }
        } catch (Exception e) {
            return "\nAn error occurred while trading.";  // Zachycení výjimek během obchodování
        }
    }

    /**
     * Získá požadovanou hodnotu pro konkrétní druh zboží.
     * Tato metoda vrací hodnotu podle typu zboží, jako jsou "resources",
     * "scrolls", "metals" nebo "krystals".
     *
     * @param resource Název zboží, pro které se získává požadovaná hodnota.
     * @return Požadovaná hodnota pro obchod.
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
     * Načte hodnoty zboží pro obchodování ze souboru.
     * Tento soubor musí obsahovat hodnoty pro jednotlivé druhy zboží,
     * které jsou načteny do statického HashMap.
     */
    public void loadItemValues() {
        try (BufferedReader br = new BufferedReader(new FileReader("res/itemValues"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                itemValues.put(parts[0], Integer.parseInt(parts[1]));  // Načítání zboží do mapy
            }
        } catch (IOException e) {
            System.out.println("Error loading item values from file.");  // Chyba při načítání souboru
        } catch (Exception e) {
            System.out.println("Error loading item values.");  // Obecná chyba
        }
    }

    /**
     * Určuje, zda tento příkaz způsobí ukončení hry nebo operace.
     * Tento příkaz {@code Trade} nikdy neukončí program, vždy vrací {@code false}.
     *
     * @return Vždy vrací {@code false}, protože tento příkaz neukončuje hru.
     */
    @Override
    public boolean exit() {
        return false;
    }
}
