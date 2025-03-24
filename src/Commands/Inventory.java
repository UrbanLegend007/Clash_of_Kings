package Commands;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Inventory extends Command {
    private static final String INVENTORY_FILE = "src/inventory";

    private static final Map<Integer, String> resourceTypes = new HashMap<>();
    static {
        resourceTypes.put(1, "Resources");
        resourceTypes.put(2, "Scrolls");
        resourceTypes.put(3, "Metal");
        resourceTypes.put(4, "Krystals");
    }

    @Override
    public String execute() {
        return "\n" + showInventory();
    }

    @Override
    public boolean exit() {
        return false;
    }

    private HashMap<Integer, Integer> loadInventory() {
        HashMap<Integer, Integer> inventory = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int resourceType = Integer.parseInt(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    inventory.put(resourceType, amount);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory.");
        }
        return inventory;
    }

    public String showInventory() {
        HashMap<Integer, Integer> inventory = loadInventory();
        if (inventory.isEmpty()) {
            return "Your inventory is empty.";
        }

        StringBuilder sb = new StringBuilder("Your inventory:\n");
        for (Integer key : inventory.keySet()) {
            sb.append("- ").append(resourceTypes.get(key)).append(": ").append(inventory.get(key)).append("\n");
        }
        return sb.toString();
    }

    public int getResourceAmount(int resourceType) {
        HashMap<Integer, Integer> inventory = loadInventory();
        return inventory.getOrDefault(resourceType, 0);
    }

    public void addItem(int resourceType, int amount) {
        HashMap<Integer, Integer> inventory = loadInventory();
        inventory.put(resourceType, inventory.getOrDefault(resourceType, 0) + amount);
        saveInventory(inventory);
    }

    public boolean removeItem(int resourceType, int amount) {
        HashMap<Integer, Integer> inventory = loadInventory();
        int currentAmount = inventory.getOrDefault(resourceType, 0);
        if (currentAmount >= amount) {
            inventory.put(resourceType, currentAmount - amount);
            if (inventory.get(resourceType) == 0) {
                inventory.remove(resourceType);
            }
            saveInventory(inventory);
            return true;
        }
        return false;
    }

    private void saveInventory(HashMap<Integer, Integer> inventory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Integer key : inventory.keySet()) {
                bw.write(key + "," + inventory.get(key) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }
}
