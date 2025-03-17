package World;

import Commands.Command;
import Commands.Exit;
import Commands.Help;
import Commands.Travel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class CommandManager {

    private Scanner s = new Scanner(System.in);
    private boolean exit = false;
    private HashMap<String, Command> command;
    private HashMap<Integer, Kingdom> world = new HashMap<>();
    private HashMap<String, Integer> nameToId = new HashMap<>();
    private int start = 1;
    private int currentPosition = start;

    public CommandManager(){
        if (loadWorld()) {
            System.out.println("Map successfully loaded");
        } else {
            System.out.println("Error loading map");
        }
        start();
    }

    private void inicializace(){
        command = new HashMap<>();
        command.put("exit", new Exit());
        command.put("help", new Help());
        command.put("travel", new Travel(this));

    }

    public void start(){
        inicializace();
        do{
            runCommand();
        }while(!exit);
    }

    private void runCommand(){
        System.out.println("\nCurrent location: " + world.get(currentPosition).toString());
        System.out.println("Commands: travel, help, exit");
        System.out.println("\nEnter command: ");
        System.out.print("-> ");
        String prikaz = s.next().toLowerCase();

        if (command.containsKey(prikaz)) {
            System.out.println(command.get(prikaz).execute());
            exit = command.get(prikaz).exit();
        } else {
            System.out.println("Invalid command");
        }
    }

    public void showBorders() {
        Kingdom currentKingdom = world.get(currentPosition);

        if (currentKingdom == null) {
            System.out.println("Error: Unknown location");
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

    public boolean loadWorld() {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\Map"))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] line = text.split(",");
                Kingdom kingdom = new Kingdom(
                    line[1],
                    Integer.parseInt(line[0]),
                    Arrays.copyOfRange(line, 2, line.length)
                );
                world.put(Integer.valueOf(line[0]), kingdom);
                nameToId.put(line[1].toLowerCase(), kingdom.getID());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String travelTo(String destination) {
        if (!nameToId.containsKey(destination)) {
            return "Misspelled kingdom";
        }

        int targetId = nameToId.get(destination);
        Kingdom current = world.get(currentPosition);

        if (current.getBorders().contains(targetId)) {
            currentPosition = targetId;
            return "You traveled to " + world.get(targetId).getName();
        } else {
            return "You cannot travel to " + world.get(targetId).getName();
        }
    }
}
