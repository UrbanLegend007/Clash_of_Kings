package Commands;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Třída Inventory spravuje inventář hráče, umožňuje zobrazování, přidávání a odebírání zdrojů.
 */
public class Inventory extends Command {

    private static final Map<Integer, String> resourceTypes = new HashMap<>();
    static {
        resourceTypes.put(1, "resources");
        resourceTypes.put(2, "scrolls");
        resourceTypes.put(3, "metal");
        resourceTypes.put(4, "krystals");
    }

    /**
     * Vrátí textovou reprezentaci aktuálního inventáře.
     * @return Textový řetězec obsahující obsah inventáře.
     */
    @Override
    public String execute() {
        return "\n" + showInventory();
    }

    /**
     * Určuje, zda tento příkaz ukončí běh programu.
     * @return Vždy vrací false.
     */
    @Override
    public boolean exit() {
        return false;
    }

    /**
     * Načte inventář ze souboru.
     * @return Mapa obsahující typy a množství zdrojů v inventáři.
     */
    private HashMap<Integer, Integer> loadInventory() {
        HashMap<Integer, Integer> inventory = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("res/inventory"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int resourceType = Integer.parseInt(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    inventory.put(resourceType, amount);
                }
            }
        } catch (Exception e) {
            System.out.println("\nError loading inventory.");
        }
        return inventory;
    }

    /**
     * Vrátí obsah inventáře jako textový řetězec.
     * @return Textová reprezentace inventáře.
     */
    public String showInventory() {
        try{
            HashMap<Integer, Integer> inventory = loadInventory();
            if (inventory.isEmpty()) {
                return "\nYour inventory is empty.";
            }

            StringBuilder sb = new StringBuilder("\nYour inventory:\n");
            for (Integer key : inventory.keySet()) {
                sb.append("- ").append(resourceTypes.get(key)).append(": ").append(inventory.get(key)).append("\n");
            }
            return sb.toString();
        } catch (Exception e){
            return "\nError getting inventory.";
        }
    }

    /**
     * Vrátí množství daného zdroje v inventáři.
     * @param resourceType Číselný identifikátor zdroje.
     * @return Počet jednotek daného zdroje.
     */
    public int getResourceAmount(int resourceType) {
        HashMap<Integer, Integer> inventory = loadInventory();
        return inventory.getOrDefault(resourceType, 0);
    }

    /**
     * Přidá určitý počet jednotek daného zdroje do inventáře.
     * @param resourceType Číselný identifikátor zdroje.
     * @param amount Počet jednotek k přidání.
     */
    public void addItem(int resourceType, int amount) {
        HashMap<Integer, Integer> inventory = loadInventory();
        inventory.put(resourceType, inventory.getOrDefault(resourceType, 0) + amount);
        saveInventory(inventory);
    }

    /**
     * Odebere určitý počet jednotek daného zdroje z inventáře.
     * Pokud je množství menší než požadované, nastaví hodnotu na 0.
     * @param resourceType Číselný identifikátor zdroje.
     * @param amount Počet jednotek k odebrání.
     * @return True, pokud byla operace úspěšná.
     */
    public boolean removeItem(int resourceType, int amount) {
        HashMap<Integer, Integer> inventory = loadInventory();
        int currentAmount = inventory.getOrDefault(resourceType, 0);
        if (currentAmount >= amount) {
            inventory.put(resourceType, currentAmount - amount);
            saveInventory(inventory);
            return true;
        } else {
            inventory.put(resourceType, 0);
            saveInventory(inventory);
            return true;
        }
    }

    /**
     * Uloží aktuální inventář do souboru.
     * @param inventory Mapa obsahující inventář.
     */
    private void saveInventory(HashMap<Integer, Integer> inventory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/inventory"))) {
            for (Integer key : inventory.keySet()) {
                bw.write(key + "," + inventory.get(key) + "\n");
            }
        } catch (Exception e) {
            System.out.println("\nError saving inventory.");
        }
    }
}
