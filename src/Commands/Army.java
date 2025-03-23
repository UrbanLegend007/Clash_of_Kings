package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Army extends Command {

    private CommandManager worldCommandManager;
    private Scanner scanner = new Scanner(System.in);
    private Map<String, Boolean> fortresses = new HashMap<>();
    private String command;

    public Army(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
        fortresses.put("main castle", false);
        fortresses.put("iron keep", false);
        fortresses.put("armyhold", false);
    }

    @Override
    public String execute() {
        do{
            System.out.print("\nEnter command: \n1) attack \n2) defense \n3) reinforcements \n4) occupy \n5) exit\n-> ");
            command = scanner.nextLine();

            if (command.equals("1") || command.equals("attack")) {
                System.out.println("Your army has attacked.");
            } else if (command.equals("2") || command.equals("defense")) {
                System.out.println("Your army has defended.");
            } else if (command.equals("3") || command.equals("reinforcements")) {
                System.out.println("Your army has reinforcements.");
            } else if (command.equals("4") || command.equals("occupy")) {
                System.out.println(occupyFortress());
            } else {
                System.out.println("Invalid command");
            }
        }while(!(command.equals("5") || command.equals("exit")));

        return "Exiting army.";
    }

    private String occupyFortress() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        if (currentKingdom == null) {
            return "Error: Unknown kingdom.";
        }

        System.out.print("Enter fortress number (1, 2, or 3): ");
        int fortressNumber = scanner.nextInt();

        if (fortressNumber < 1 || fortressNumber > 3) {
            return "Invalid fortress number.";
        }

        fortressNumber--;  // Adjust for 0-based index

        if (currentKingdom.isFortressOccupied(fortressNumber)) {
            return "This fortress has already been occupied.";
        }

        // Obsazení hradu
        currentKingdom.setFortressOccupied(fortressNumber, true);

        return "You have successfully occupied fortress " + fortressNumber + " in " + currentKingdom.getName() + ".";
    }

//    private String occupyFortress() {
//        System.out.println("\nFortresses: ");
//        System.out.println(" - Main Castle");
//        System.out.println(" - Iron Keep");
//        System.out.println(" - Armyhold");
//        System.out.print("\nEnter fortress name to occupy: ");
//        String fortressName = scanner.nextLine().toLowerCase();
//
//        if (!fortresses.containsKey(fortressName)) {
//            return "Fortress not found.";
//        }
//        if (fortresses.get(fortressName)) {
//            return "This fortress is already occupied.";
//        }
//        fortresses.put(fortressName, true);
//        return "You have successfully occupied " + fortressName + ".";
//    }

    @Override
    public boolean exit() {
        return false;
    }
}
