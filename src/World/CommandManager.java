package World;

import Commands.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CommandManager {

    private Scanner s = new Scanner(System.in);
    private boolean exit = false;

    private boolean won = false;

    private HashMap<String, Command> command;
    public HashMap<Integer, Kingdom> world = new HashMap<>();
    private HashMap<String, Integer> nameToId = new HashMap<>();

    public int start = 1;
    public int currentPosition = start;

    public CommandManager(){
        if (loadWorld()) {
            System.out.println("\nMap successfully loaded.");
        } else {
            System.out.println("\nError loading map.");
        }
        start();
    }

    private void inicializace(){
        command = new HashMap<>();
        command.put("exit", new Exit());
        command.put("inventory", new Inventory());
        command.put("help", new Help());
        command.put("talk", new Talk(this));
        command.put("use", new Use(this));
        command.put("army", new Army(this));
        command.put("negotiation", new Negotiation(this));
        command.put("maintain", new Maintain(this));
        command.put("trade", new Trade(this));
        command.put("get", new Get(this));
        command.put("travel", new Travel(this));
    }

    public void start(){
        inicializace();
        do{
            runCommand();
        }while(!exit);
    }

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
            System.out.println("\nCommands: \n-> characters, inventory, travel, help, get, trade, talk, use, army, negotiation, maintain, exit");
            System.out.println("\nEnter command: ");
            System.out.print(" -> ");
            String prikaz = "";

            try {
                prikaz = s.nextLine().toLowerCase();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid command.");
                s.next();
                return;
            }

            if(prikaz.equals("characters")){
                showCharacters();
            }else if (command.containsKey(prikaz)) {
                System.out.println(command.get(prikaz).execute());
                exit = command.get(prikaz).exit();
            }else {
                System.out.println("Invalid command.");
            }
        }else{
            System.out.println("\n\n-------------------------------------------------------------------" +
                    "\n\n                        Congratulations. \n                    You have WON this game. \n\n" +
                    "-----------------------------------------------------------------------------------------------------------");
            exit = true;
        }
    }

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

    public boolean atWar(){
        Kingdom currentKingdom = world.get(currentPosition);

        if (currentKingdom == null) {
            System.out.println("Error: Unknown location.");
        }

        if(currentKingdom.getBattle().equals("Not Battling")){
            return false;
        }else if(currentKingdom.getBattle().equals("Battling")){
            return true;
        }else{
            return false;
        }
    }

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

    public boolean loadWorld() {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\Map"))) {
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
        } catch (IOException e) {
            System.out.println("Error reading world data: " + e.getMessage());
            return false;
        }
    }

    public String travelTo(String destination) {
        if (!nameToId.containsKey(destination)) {
            return "\nMisspelled kingdom.";
        }

        int targetId = nameToId.get(destination);
        Kingdom current = world.get(currentPosition);

        if (current.getBorders().contains(targetId)) {
            currentPosition = targetId;
            return "\nYou traveled to " + world.get(targetId).getName() + ".";
        } else {
            return "\nYou cannot travel to " + world.get(targetId).getName() + ".";
        }
    }
}
