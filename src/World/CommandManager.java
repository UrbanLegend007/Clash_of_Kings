package World;

import Commands.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Třída pro správu příkazů a interakci se světem.
 * Obsahuje metody pro načítání světa, pohyb mezi královstvími, správu příkazů a resetování světa.
 */
public class CommandManager {

    private Scanner s = new Scanner(System.in);
    private boolean exit = false;

    private boolean won = false;

    private HashMap<String, Command> command;
    public HashMap<Integer, Kingdom> world = new HashMap<>();
    private HashMap<String, Integer> nameToId = new HashMap<>();

    public int start = 1;
    public int currentPosition = start;

    /**
     * Konstruktor pro inicializaci CommandManageru a načítání hry.
     * Tento konstruktor inicializuje objekty a pokusí se načíst mapu a lokaci.
     */
    public CommandManager(){
        try{
            loadingGame();
        } catch (Exception e){
            System.out.println("Error while loading this game.");
        }
    }

    /**
     * Pokusí se načíst svět a lokaci z uložených souborů a následně spustí hru.
     */
    public void loadingGame(){
        try{
            if (loadWorld()) {
                System.out.println("\nMap successfully loaded.");
            } else {
                System.out.println("\nError loading map.");
            }
        } catch (Exception e) {
            System.out.println("\nError loading the map.");
        }
        try{
            if(loadLocation()){
                System.out.println("\nLocation successfully loaded.");
            } else {
                System.out.println("\nError loading location.");
            }
        } catch (Exception e) {
            System.out.println("\nError loading the location.");
        }
        try{
            start();
        } catch (Exception e) {
            System.out.println("Error starting the game.");
        }
    }

    /**
     * Ukládá aktuální lokaci do souboru.
     * @param location Nová pozice hráče.
     */
    public void setLocation(int location){
        try (BufferedReader br = new BufferedReader(new FileReader("res/location"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(location);
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("res/location"));
            bw.write(sb.toString());
            bw.close();
            loadLocation();
        } catch (Exception e){
            System.out.println("Error setting the location.");
        }
    }

    /**
     * Načítá aktuální lokaci hráče z disku.
     * @return true pokud byla lokace načtena úspěšně, jinak false.
     */
    public boolean loadLocation(){
        currentPosition = start;

        try (BufferedReader br = new BufferedReader(new FileReader("res/location"))) {
            String text;

            while ((text = br.readLine()) != null) {
                currentPosition = Integer.parseInt(text);
                break;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error loading location from the world.");
            return false;
        }
    }

    /**
     * Inicializuje příkazy pro hru.
     */
    private void inicializace(){
        command = new HashMap<>();
        command.put("exit", new Exit());
        command.put("inventory", new Inventory());
        command.put("help", new Help());
        command.put("talk", new Talk(this));
        command.put("army", new Army(this,false,false));
        command.put("negotiation", new Negotiation(this));
        command.put("maintain", new Maintain(this));
        command.put("trade", new Trade(this));
        command.put("get", new Get(this));
        command.put("travel", new Travel(this));
    }

    /**
     * Spustí hru a zpracovává příkazy uživatele.
     */
    public void start(){
        try{
            inicializace();
        }catch (Exception e) {
            System.out.println("Error inicializating the game.");
        }
        do{
            runCommand();
        }while(!exit);
    }

    /**
     * Tato metoda kontroluje příkazy, provádí příslušné akce a ukončuje hru, pokud jsou splněny podmínky.
     */
    private void runCommand(){
        int count = 0;
        for (int i = 1; i <= 8; i++) {
            Kingdom kingdom = world.get(i);
            if(kingdom.isConquered().equals("conquered")){
                count++;
            }
        }
        if(count == 8){
            won = true;
        }
        if(!won){
            System.out.println("\nCurrent location: " + world.get(currentPosition).toString());
            System.out.println("\nCommands: \n-> reset, scrolls, characters, inventory, travel, help, get, trade, talk, army, negotiation, maintain, exit");
            System.out.println("\nEnter command: ");
            System.out.print(" -> ");
            String prikaz = "";

            try {
                prikaz = s.nextLine().toLowerCase();
                System.out.println("\n===========================================================================================================");
            } catch (Exception e){
                System.out.println("Invalid input. Please enter a valid command.");
                System.out.println("\n===========================================================================================================\n");
                s.next();
                return;
            }
            if(prikaz.equals("characters")){
                showCharacters();
                System.out.println("\n===========================================================================================================\n");
            } else if(prikaz.equals("reset")){
                System.out.println("\nDo you really want to reset the map? Y/N");
                System.out.print(" -> ");
                try {
                    prikaz = s.nextLine().toLowerCase();
                } catch (Exception e){
                    System.out.println("Invalid input. Please enter a valid command.");
                }
                if(prikaz.equals("Y") || prikaz.equals("yes") || prikaz.equals("y")){
                    resetWorld();
                    System.out.println("\n===========================================================================================================\n");
                }else if(prikaz.equals("N") || prikaz.equals("no") || prikaz.equals("n")){
                    System.out.println("World has not been reset.");
                    System.out.println("\n===========================================================================================================\n");
                }else{
                    System.out.println("Invalid input. Please enter a valid command.");
                    System.out.println("\n===========================================================================================================\n");
                }
            } else if(prikaz.equals("scrolls")){
                System.out.println(world.get(currentPosition).getScrolls());
                System.out.println("\n===========================================================================================================\n");
            } else if (command.containsKey(prikaz)) {
                System.out.println(command.get(prikaz).execute());
                exit = command.get(prikaz).exit();
                System.out.println("\n===========================================================================================================\n");
            } else {
                System.out.println("Invalid command.");
                System.out.println("\n===========================================================================================================\n");
            }
        }else{
            System.out.println("\n\n===========================================================================================================" +
                    "\n\n                        Congratulations. \n                    You have WON this game. \n\n" +
                    "===========================================================================================================");
            exit = true;
        }
    }
    /**
     * Zobrazuje seznam dostupných království, kam může hráč cestovat.
     *
     */
    public void showBorders() {
        Kingdom currentKingdom = world.get(currentPosition);

        if (currentKingdom == null) {
            System.out.println("Error: Unknown location.");
            return;
        }

        System.out.println("\nYou can travel to:");
        for (int neighborID : currentKingdom.getBorders()) {
            Kingdom neighbor = world.get(neighborID);
            if (neighbor != null) {
                System.out.println("- " + neighbor.getName());
            }
        }
    }

    /**
     * Zobrazuje postavy všech království ve světě.
     */
    public void showCharacters() {
        Kingdom currentKingdom = world.get(currentPosition);

        if (currentKingdom == null) {
            System.out.println("Error: Unknown location.");
            return;
        }
        System.out.println();
        for (int kingdomID = 1; kingdomID <= 8; kingdomID++) {
            Kingdom neighbor = world.get(kingdomID);
            if (neighbor != null) {
                System.out.println("--------------------------------------------------" + neighbor.character());
            }
        }
        System.out.println("--------------------------------------------------");
    }

    /**
     * Načítá svět z mapy do herního stavu.
     * @return true pokud svět byl úspěšně načten, jinak false.
     */
    public boolean loadWorld() {
        try (BufferedReader br = new BufferedReader(new FileReader("res/Map"))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] line = text.split(",");
                String[] borders = Arrays.copyOfRange(line, 2, line.length - 4);
                String conquered = line[line.length - 4];
                String characterName = line[line.length - 3];
                String loyalty = line[line.length - 2];
                String battle = line[line.length - 1];

                Kingdom kingdom = new Kingdom(
                        line[1],
                        Integer.parseInt(line[0]),
                        borders,
                        conquered,
                        characterName,
                        loyalty,
                        battle
                );
                world.put(Integer.valueOf(line[0]), kingdom);
                nameToId.put(line[1].toLowerCase(), kingdom.getID());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error loading the world.");
            return false;
        }
    }

    /**
     * Cestuje na požadovanou destinaci v rámci království.
     * @param destination Název cílového království.
     * @return Zpráva o úspěchu nebo chybě při cestování.
     */
    public String travelTo(String destination) {
        if (!nameToId.containsKey(destination)) {
            return "\nMisspelled kingdom.";
        }

        int targetId = nameToId.get(destination);
        Kingdom current = world.get(currentPosition);
        Kingdom myKingdom = world.get(1);

        if (current.getBorders().contains(targetId)) {
            currentPosition = targetId;
            setLocation(targetId);
            if(currentPosition == 1){
                myKingdom.setArmy(myKingdom.getMyArmy());
                myKingdom.setInventory(0,1);
                System.out.println("\nYour army has been rebuilt.");
            }
            System.out.println("\nYou traveled to " + world.get(currentPosition).getName() + ".");
            new Army(this,true,true).execute();
            return "";
        } else {
            return "\nYou cannot travel to " + world.get(currentPosition).getName() + ".";
        }
    }

    /**
     * Resetuje svět na původní hodnoty.
     */
    public void resetWorld() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/Map"))) {
            bw.write("1,My kingdom,2,3,4,conquered,My character,10,Not Battling\n");
            bw.write("2,Shadow kingdom,1,3,5,not conquered,Dark king,5,Not Battling\n");
            bw.write("3,Copper kingdom,1,2,4,6,not conquered,Arthur king,5,Not Battling\n");
            bw.write("4,Forest kingdom,1,3,7,not conquered,Eddard king,5,Not Battling\n");
            bw.write("5,Northern kingdom,2,3,6,8,not conquered,Rob king,5,Not Battling\n");
            bw.write("6,Sea kingdom,3,5,7,8,not conquered,Jon king,5,Not Battling\n");
            bw.write("7,Sun-Desert kingdom,4,6,8,not conquered,Yellow king,5,Not Battling\n");
            bw.write("8,East kingdom,5,6,7,not conquered,Healthy king,5,Not Battling\n");
            System.out.println("\nThe world has been reset to its default state.");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/reset"))){
            bw.write("2000");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/resources"))){
            bw.write("1,1,1,1,1\n" +
                    "2,3,3,3,3\n" +
                    "3,3,3,3,3\n" +
                    "4,3,3,3,3\n" +
                    "5,3,3,3,3\n" +
                    "6,3,3,3,3\n" +
                    "7,3,3,3,3\n" +
                    "8,3,3,3,3");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/items"))){
            bw.write("1,1,1,1,1\n" +
                    "2,3,3,3,3\n" +
                    "3,3,3,3,3\n" +
                    "4,3,3,3,3\n" +
                    "5,3,3,3,3\n" +
                    "6,3,3,3,3\n" +
                    "7,3,3,3,3\n" +
                    "8,3,3,3,3");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/inventory"))){
            bw.write("1,1\n" +
                    "2,0\n" +
                    "3,1\n" +
                    "4,1");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/fortress"))){
            bw.write("1,true,true,true,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "2,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "3,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "4,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "5,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "6,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "7,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold\n" +
                    "8,false,false,false,3,3,3,Main Castle,Iron Keep,Armyhold");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/army"))){
            bw.write("1,2000,2000,2000\n" +
                    "2,5000,3000,2500\n" +
                    "3,8500,4000,4000\n" +
                    "4,11000,5000,6000\n" +
                    "5,14000,7000,9000\n" +
                    "6,23000,20000,15000\n" +
                    "7,17000,15000,12000\n" +
                    "8,20000,18000,13000");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/location"))){
            bw.write("1");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/scrolls"))){
            bw.write("1;Shadow kingdom has in Main castle 5000 soldiers.;false\n" +
                    "2;Shadow kingdom has in Iron Keep 3000 soldiers.;false\n" +
                    "3;Shadow kingdom has in Armyhold 2500 soldiers.;false\n" +
                    "4;Copper kingdom has in Main castle 8500 soldiers.;false\n" +
                    "5;Copper kingdom has in Iron Keep 4000 soldiers.;false\n" +
                    "6;Copper kingdom has in Armyhold 4000 soldiers.;false\n" +
                    "7;Forest kingdom has in Main castle 11000 soldiers.;false\n" +
                    "8;Forest kingdom has in Iron Keep 5000 soldiers.;false\n" +
                    "9;Forest kingdom has in Armyhold 6000 soldiers.;false\n" +
                    "10;Northern kingdom has in Main castle 14000 soldiers.;false\n" +
                    "11;Northern kingdom has in Iron Keep 7000 soldiers.;false\n" +
                    "12;Northern kingdom has in Armyhold 9000 soldiers.;false\n" +
                    "13;Sea kingdom has in Main castle 23000 soldiers.;false\n" +
                    "14;Sea kingdom has in Iron Keep 20000 soldiers.;false\n" +
                    "15;Sea kingdom has in Armyhold 15000 soldiers.;false\n" +
                    "16;Sun-Desert kingdom has in Main castle 17000 soldiers.;false\n" +
                    "17;Sun-Desert kingdom has in Iron Keep 15000 soldiers.;false\n" +
                    "18;Sun-Desert kingdom has in Armyhold 12000 soldiers.;false\n" +
                    "19;East kingdom has in Main castle 20000 soldiers.;false\n" +
                    "20;East kingdom has in Iron Keep 18000 soldiers.;false\n" +
                    "21;East kingdom has in Armyhold 13000 soldiers.;false");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/strength"))){
            bw.write("0");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/itemValues"))){
            bw.write("resources,1\n" +
                    "scrolls,2\n" +
                    "metals,3\n" +
                    "krystals,4");
        } catch (Exception e){
            System.out.println("Error resetting world.");
        }

        try{
            loadingGame();
        } catch (Exception e){
            System.out.println("Error while resetting and loading game.");
        }
    }
}
