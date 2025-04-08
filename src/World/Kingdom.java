package World;

import java.io.*;
import java.util.*;

/**
 * Třída reprezentuje království s různými vlastnostmi, jako jsou jméno, ID, armáda, pevnosti, zdroje, a další.
 * Poskytuje metody pro manipulaci s těmito vlastnostmi, včetně načítání a ukládání dat z/do souborů.
 */
public class Kingdom {

    private String name;
    private String character;
    private String conquered;
    private int loyalty;

    private String battle;
    private int ID;
    private ArrayList<Integer> borders;
    private HashMap<String, Integer> items = new HashMap<>();
    private HashMap<String, Integer> resources = new HashMap<>();
    private static HashMap<Integer, String> dialogMap = new HashMap<>();
    private int[] fortressArmy = new int[3];
    private boolean[] fortressesOccupied = new boolean[3];
    private HashMap<Integer, String> scrolls = new HashMap<>();
    private int[] fortressesStrength = new int[3];

    public int getSrcollsSize(){
        return scrolls.size();
    }

    /**
     * Konstruktor pro vytvoření nového království s danými parametry.
     *
     * @param name Název království.
     * @param ID ID království.
     * @param borders Pole hranic království.
     * @param conquered Status dobytí království.
     * @param character Charakter království.
     * @param loyalty Loajalita království.
     * @param battle Stav bitvy království.
     */
    public Kingdom(String name, int ID, String[] borders, String conquered, String character, String loyalty, String battle) {
        this.name = name;
        this.ID = ID;
        this.borders = new ArrayList<>();
        this.conquered = conquered;
        this.character = character;
        this.loyalty = Integer.parseInt(loyalty);
        for (String loc : borders) {
            this.borders.add(Integer.parseInt(loc));
        }
        this.battle = battle;
        loadItems("items");
        loadItems("resources");
        loadDialogs();
        loadFortressStatus();
        saveFortressStatus();
        loadArmy();
        loadScrolls();
    }

    /**
     * Načte stav pevností pro toto království.
     */
    private void loadFortressStatus() {
        try (BufferedReader br = new BufferedReader(new FileReader("res/fortress"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);
                if (kingdomID == this.ID) {
                    for (int i = 0; i < 3; i++) {
                        fortressesOccupied[i] = Boolean.parseBoolean(parts[i + 1]);
                    }
                    for (int i = 0; i < 3; i++) {
                        fortressesStrength[i] = Integer.parseInt(parts[i + 4]);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading fortress status for kingdom: " + name + ".");
        } catch (Exception e) {
            System.out.println("Error loading fortress status.");
        }
    }

    public void loadScrolls(){
        try (BufferedReader br = new BufferedReader(new FileReader("res/scrolls"))){
            String line;
            boolean get;

            while ((line = br.readLine()) != null){
                String[] parts = line.split(";", 3);
                get = Boolean.parseBoolean(parts[2]);
                if(get){
                    scrolls.put(Integer.parseInt(parts[0]), parts[1]);
                } else {
                    scrolls.remove(Integer.parseInt(parts[0]));
                }
            }
        } catch (Exception e){
            System.out.println("Error while getting srolls.");
        }
    }

    public void setScrolls(int id, boolean get) {
        if(id <= 0 || id > 21){
            System.out.println("Scroll ID out of bounds: " + id);
            return;
        }
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("res/scrolls"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 3);
                if (Integer.parseInt(parts[0]) == id) {
                    line = parts[0] + ";" + parts[1] + ";" + get;
                }
                updatedLines.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error while setting scrolls.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/scrolls"))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error while writing updated scrolls.");
        }
        try {
            loadScrolls();
        } catch (Exception e) {
            System.out.println("Error while loading scrolls.");
        }
        this.setInventory(id, scrolls.size());
    }

    public String getScrolls(){
        if(scrolls.isEmpty()){
            return "\nYou have no scrolls.";
        } else {
            System.out.println();
            for (int i = 1; i <= 21; i++) {
                if(scrolls.containsKey(i)){
                    System.out.println(scrolls.get(i));
                }
            }
            return "\nYou have " + scrolls.size() + " scrolls.";
        }
    }

    /**
     * Uloží stav pevností pro toto království do souboru.
     */
    private void saveFortressStatus() {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("res/fortress"))) {
            String line;
            boolean updated = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    StringBuilder newLine = new StringBuilder();
                    newLine.append(this.ID);
                    for (int i = 0; i < 3; i++) {
                        newLine.append(",").append(fortressesOccupied[i]);
                    }
                    for (int i = 0; i < 3; i++) {
                        newLine.append(",").append(fortressesStrength[i]);
                    }
                    lines.add(newLine.toString());
                    updated = true;
                } else {
                    lines.add(line);
                }
            }

            if (!updated) {
                StringBuilder newLine = new StringBuilder();
                newLine.append(this.ID);
                for (int i = 0; i < 3; i++) {
                    newLine.append(",").append(fortressesOccupied[i]);
                }
                for (int i = 0; i < 3; i++) {
                    newLine.append(",").append(fortressesStrength[i]);
                }
                lines.add(newLine.toString());
            }
        } catch (Exception e) {
            System.out.println("Error reading fortress file.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/fortress"))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (Exception e){
            System.out.println("Error writing fortress file.");
        }
    }

    /**
     * Nastaví sílu pevnosti pro daný index.
     *
     * @param fortressIndex Index pevnosti (0, 1, 2).
     * @param strength Síla pevnosti.
     */
    public void setFortressesStrength(int fortressIndex, int strength) {
        if( fortressIndex < 0 || fortressIndex >= 3 ) {
            System.out.println("Invalid fortress index: " + fortressIndex);
        }
        fortressesStrength[fortressIndex] = strength;
        saveFortressStatus();
    }

    /**
     * Získá sílu pevnosti pro daný index.
     *
     * @param fortressIndex Index pevnosti (0, 1, 2).
     * @return Síla pevnosti.
     */
    public int getFortressStrength(int fortressIndex) {
        return fortressesStrength[fortressIndex];
    }

    /**
     * Zkontroluje, zda je pevnost obsazena pro daný index.
     *
     * @param fortressIndex Index pevnosti (0, 1, 2).
     * @return True pokud je pevnost obsazena, jinak false.
     */
    public boolean isFortressOccupied(int fortressIndex) {
        if (fortressIndex < 0 || fortressIndex >= fortressesOccupied.length) {
            return false;
        }
        return fortressesOccupied[fortressIndex];
    }

    /**
     * Nastaví stav obsazení pevnosti pro daný index.
     *
     * @param fortressIndex Index pevnosti (0, 1, 2).
     * @param occupied Stav obsazení (true/false).
     */
    public void setFortressOccupied(int fortressIndex, boolean occupied) {
        if (fortressIndex < 0 || fortressIndex >= fortressesOccupied.length) {
            return;
        }
        fortressesOccupied[fortressIndex] = occupied;
        saveFortressStatus();
    }

    /**
     * Načte dialogy pro toto království z externího souboru.
     */
    private static void loadDialogs() {
        if (!dialogMap.isEmpty()) return;

        try (BufferedReader br = new BufferedReader(new FileReader("res/dialog"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    int dialogID = Integer.parseInt(parts[0]);
                    dialogMap.put(dialogID, parts[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading dialogs.");
        }
    }

    /**
     * Načte armádu pro toto království z externího souboru.
     */
    private void loadArmy() {
        try (BufferedReader br = new BufferedReader(new FileReader("res/Army"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    for (int i = 0; i < 3; i++) {
                        fortressArmy[i] = Integer.parseInt(parts[i + 1]);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading army for kingdom: " + name + ".");
        } catch (Exception e) {
            System.out.println("Error loading army.");
        }
    }

    /**
     * Získá počet vojáků v pevnosti pro daný index.
     *
     * @param fortressIndex Index pevnosti (0, 1, 2).
     * @return Počet vojáků v pevnosti.
     */
    public int getArmyInFortress(int fortressIndex) {
        if (fortressIndex < 0 || fortressIndex >= fortressArmy.length) {
            return 0;
        }
        return fortressArmy[fortressIndex];
    }

    /**
     * Nastaví počet vojáků v pevnosti pro daný index.
     *
     * @param fortressIndex Index pevnosti (0, 1, 2).
     * @param newSize Nový počet vojáků.
     */
    public void setArmyInFortress(int fortressIndex, int newSize) {
        if (fortressIndex < 0 || fortressIndex >= fortressArmy.length) {
            return;
        }
        fortressArmy[fortressIndex] = newSize;
        saveArmy();
    }


    /**
     * Nastaví inventář.
     */
    public void setInventory(int id, int amount){
        if(id < 0 || id > 21){
            System.out.println("\nInvalid inventory index: " + id);
            return;
        } else if(id == 0){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/inventory"))){
                bw.write("1," + amount + "\n" +
                        "2," + amount + "\n" +
                        "3," + amount + "\n" +
                        "4," + amount);
            } catch (Exception e){
                System.out.println("\nError reseting inventory world.");
            }
            return;
        }
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("res/inventory"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (Integer.parseInt(parts[0]) == id) {
                    line = parts[0] + "," + amount;
                }
                updatedLines.add(line);
            }
        } catch (Exception e) {
            System.out.println("\nError while setting inventory.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/inventory"))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("\nError while writing updated inventory.");
        }
    }

    /**
     * Vrátí inventář.
     */
    public int getInventory(int itemType) {
        int totalAmount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("res/inventory"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int kingdomID = Integer.parseInt(parts[0]);
                if (kingdomID == this.ID) {
                    totalAmount = Integer.parseInt(parts[itemType]);
                }
            }
            return totalAmount;
        } catch (Exception e) {
            System.out.println("Error loading items from inventory.");
            return 0;
        }
    }

    /**
     * Nastaví velikost armády pro všechny pevnosti.
     *
     * @param newSize Nová velikost armády pro všechny pevnosti.
     */
    public void setArmy(int newSize) {
        setArmyInFortress(0, newSize);
        setArmyInFortress(1, newSize);
        setArmyInFortress(2, newSize);
    }

    /**
     * Získá celkový počet vojáků v armádě (suma pro všechny pevnosti).
     *
     * @return Celkový počet vojáků v armádě.
     */
    public int getArmySize() {
        return (getArmyInFortress(0) + getArmyInFortress(1) + getArmyInFortress(2));
    }

    /**
     * Uloží velikost armády.
     */
    private void saveArmy() {
        try (BufferedReader br = new BufferedReader(new FileReader("res/army"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    sb.append(kingdomID);
                    for (int soldiers : fortressArmy) {
                        sb.append(",").append(soldiers);
                    }
                    sb.append("\n");
                } else {
                    sb.append(line).append("\n");
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("res/army"));
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("Error saving army for kingdom: " + name + ".");
        } catch (Exception e) {
            System.out.println("Error saving army.");
        }
    }

    /**
     * Získá dialog pro daný ID dialogu.
     *
     * @param dialogID ID dialogu.
     * @return Text dialogu.
     */
    public String getDialog(int dialogID) {
        return dialogMap.getOrDefault(dialogID, "No dialog available.");
    }

    /**
     * Načte položky (např. zdroje, předměty) pro toto království z externího souboru.
     *
     * @param item Název souboru s položkami.
     */
    private void loadItems(String item) {
        try (BufferedReader br = new BufferedReader(new FileReader("res/"+item))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    items.put("resources", Integer.parseInt(parts[1]));
                    items.put("scrolls", Integer.parseInt(parts[2]));
                    items.put("metals", Integer.parseInt(parts[3]));
                    items.put("krystals", Integer.parseInt(parts[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading items from kingdom: " + name + ".");
        } catch (Exception e) {
            System.out.println("Error loading items.");
        }
    }

    /**
     * Sebere všechny položky (např. zdroje, předměty) pro toto království z externího souboru.
     *
     * @param item Název souboru pro uložení položek.
     */
    public boolean collectItems(String resource, int amount, String item) {

        int currentAmount = items.getOrDefault(resource, 0);
        if (currentAmount >= amount) {
            items.put(resource, currentAmount - amount);
            saveItems(item);
            return true;
        }else {
            items.put(resource, 0);
            saveItems(item);
            return false;
        }
    }

    /**
     * Přidá položky (např. zdroje, předměty) pro toto království do externího souboru.
     *
     * @param item Název souboru pro uložení položek.
     */
    public boolean addItems(String resource, int amount, String item) {
        int currentAmount = items.getOrDefault(resource, 0);
        items.put(resource, currentAmount + amount);
        saveItems(item);
        return true;
    }


    /**
     * Uloží položky (např. zdroje, předměty) pro toto království do externího souboru.
     *
     * @param item Název souboru pro uložení položek.
     */
    private void saveItems(String item) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("res/"+item));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    sb.append(ID).append(",")
                            .append(items.get("resources")).append(",")
                            .append(items.get("scrolls")).append(",")
                            .append(items.get("metals")).append(",")
                            .append(items.get("krystals")).append("\n");
                }else {
                    sb.append(line).append("\n");
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("res/"+item));
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("Error loading items from kingdom: " + name + ".");
        } catch (Exception e) {
            System.out.println("Error loading items.");
        }
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    /**
     * Vrátí velikost inventáře.
     */
    public int inventoryAmount(int itemType, String item) {

        int totalAmount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("res/" + item))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int kingdomID = Integer.parseInt(parts[0]);
                if (kingdomID == this.ID) {
                    totalAmount = Integer.parseInt(parts[itemType]);
                }
            }
            return totalAmount;
        } catch (Exception e) {
            System.out.println("Error loading items amount from kingdom: " + name + ".");
            return 0;
        }
    }

    /**
     * Získá seznam všech zdrojů pro toto království.
     *
     * @return Mapa zdrojů (název -> množství).
     */
    public HashMap<String, Integer> getResources() {
        return resources;
    }

    /**
     * Získá seznam hranic pro toto království.
     *
     * @return Seznam hranic (ID sousedních království).
     */
    public ArrayList<Integer> getBorders() {
        return borders;
    }

    /**
     * Vytvoření toString.
     */
    @Override
    public String toString() {
        return "\n - Kingdom: " + name +
                "\n - Borders = " + borders.size() +
                "\n - " + conquered +
                "\n - " + battle;
    }

    public String character(){
        return "\nKingdom: " + name +
                "\nKing: " + character +
                "\nLoyalty: " + loyalty +
                "\n" + battle;
    }

    /**
     * Getters a setters.
     */
    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * Získá stav dobytí království.
     *
     * @return Stav dobytí (např. "not conquered").
     */
    public String isConquered() {
        String conqueredStatus = "not conquered";

        try (BufferedReader reader = new BufferedReader(new FileReader("res/Map"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    conqueredStatus = parts[parts.length-4].trim();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading the 'Map' file.");
        }

        return conqueredStatus;
    }

    /**
     * Nastaví stav dobytí království.
     *
     * @param conquered Nový stav dobytí.
     */
    public void setConquered(String conquered) {
        this.conquered = conquered;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("res/Map"));
            StringBuilder updatedMap = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    parts[parts.length - 4] = conquered;
                }

                updatedMap.append(String.join(",", parts)).append("\n");
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("res/Map"));
            writer.write(updatedMap.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Error updating the 'conquered' status in the Map file.");
        }
    }

    public int getLoyalty() {
        return loyalty;
    }

    /**
     * Nastaví loajalitu království.
     *
     * @param loy Nová hodnota loajality.
     */
    public void setLoyalty(int loy) {
        if(loy > 0){
            if((loyalty += loy) < 10 && (loyalty+= loy) > 0){
                this.loyalty += loy;

            }else if((loyalty += loy) >= 10){
                this.loyalty = 10;

            }else if((loyalty += loy) <= 0){
                this.loyalty = 0;
            }

        }else if(loy == 0){
            this.loyalty = 0;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("res/Map"));
            StringBuilder updatedMap = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    parts[parts.length-2] = Integer.toString(this.loyalty);
                }

                updatedMap.append(String.join(",", parts)).append("\n");
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("res/Map"));
            writer.write(updatedMap.toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Error updating the 'loyalty' status in the Map file.");
        }
        System.out.println(this.checkLoyalty());
    }

    public String checkLoyalty() {
        int loyalty = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("res/Map"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    loyalty = Integer.parseInt(parts[parts.length-2]);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking the 'loyalty' status in the Map file.");
        }
        if(loyalty >= 10){
            this.setConquered("conquered");
            this.setBattle("Not Battling");
            this.setMyArmy(1000);
            return "\n" + this.getName() + " has loyalty " + loyalty + ".\nYou have conquered " + this.getName() + ".\nYou have conquered the entire kingdom of " + this.getName() + ".\nYour army has increased by 1000 soldiers.";
        } else {
            return "";
        }
    }

    /**
     * Vrátí bojování království.
     */
    public String getBattle() {
        String battleStatus = "Not Battling";

        try (BufferedReader reader = new BufferedReader(new FileReader("res/Map"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    battleStatus = parts[parts.length - 1].trim();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading the 'Map' file.");
        }

        return battleStatus;
    }

    /**
     * Nastaví bojování království.
     */
    public void setBattle(String battle) {
        this.battle = battle;

        try (BufferedReader reader = new BufferedReader(new FileReader("res/Map"))) {
            StringBuilder updatedMap = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int kingdomID = Integer.parseInt(parts[0]);

                if (kingdomID == this.ID) {
                    parts[parts.length-1] = battle.trim();
                }

                updatedMap.append(String.join(",", parts)).append("\n");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("res/Map"))) {
                writer.write(updatedMap.toString());
            }

        } catch (Exception e) {
            System.out.println("Error updating the 'Map' file.");
        }
    }

    /**
     * Nastaví moji armádu.
     */
    public void setMyArmy(int army){

        try (BufferedReader br = new BufferedReader(new FileReader("res/reset"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(army+getMyArmy());
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("res/reset"));
            bw.write(sb.toString());
            bw.close();
        } catch (Exception e){
            System.out.println("Error setting army for My kingdom.");
        }
    }

    /**
     * Nastaví sílu mojí armády.
     */
    public void setStrength(double strength){
        try (BufferedReader br = new BufferedReader(new FileReader("res/strength"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(strength+getStrength());
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("res/strength"));
            bw.write(sb.toString());
            bw.close();
        } catch (Exception e){
            System.out.println("Error setting strength for My kingdom.");
        }
    }

    /**
     * Vrátí sílu mojí armády.
     */
    public double getStrength(){
        double strength = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("res/strength"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                strength = Double.parseDouble(line);
                break;
            }
        } catch (Exception e){
            System.out.println("Error getting strength for My kingdom.");
        }

        return strength;
    }
    /**
     * Vrátí moji armádu.
     */
    public int getMyArmy(){
        int army = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("res/reset"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                army = Integer.parseInt(line);
                break;
            }
        } catch (Exception e){
            System.out.println("Error getting army for My kingdom.");
        }
        return army;
    }
}
