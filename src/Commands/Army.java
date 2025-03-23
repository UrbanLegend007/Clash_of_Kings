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
    private String fortressName;

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
        Kingdom myKingdom = worldCommandManager.world.get(0);

        if (currentKingdom == null) {
            return "\nError: Unknown kingdom.";
        }

        System.out.println("\n1) Main Castle");
        System.out.println("2) Iron Keep");
        System.out.println("3) Armyhold");
        System.out.print("Enter fortress number: ");
        int fortressNumber = scanner.nextInt();
        scanner.nextLine();

        if (fortressNumber < 1 || fortressNumber > 3) {
            return "\nInvalid fortress number.";
        }
        fortressNumber--;

        if (currentKingdom.isFortressOccupied(fortressNumber)) {
            return "\nThis fortress has already been occupied.";
        }
        currentKingdom.setFortressOccupied(fortressNumber, true);

        switch (fortressNumber) {
            case 0:
                fortressName = "Main Castle";
                break;
            case 1:
                fortressName = "Iron Keep";
                break;
            case 2:
                fortressName = "Armyhold";
                break;
        }

        if(currentKingdom.getArmySize()*3 > myKingdom.getArmySize()) {
            currentKingdom.updateArmySize(((currentKingdom.getArmySize()*3)-myKingdom.getArmySize())/3);
            myKingdom.updateArmySize(0);
            return "\nYou have been defeated by " + currentKingdom.getName() + ".\n" + currentKingdom.getName() + " army has now " + currentKingdom.getArmySize() + " and your army has " + myKingdom.getArmySize();
        }else if(currentKingdom.getArmySize()*3 < myKingdom.getArmySize()) {
            currentKingdom.setFortressOccupied(fortressNumber, true);
            currentKingdom.updateArmySize((0));
            myKingdom.updateArmySize(myKingdom.getArmySize() - currentKingdom.getArmySize()*3);
            return "\nYou occupied " + fortressName + " in " + currentKingdom.getName() + ".\n" + currentKingdom.getName() + " army has now " + currentKingdom.getArmySize() + " and your army has " + myKingdom.getArmySize();
        }else{
            currentKingdom.updateArmySize((0));
            myKingdom.updateArmySize(0);
            return "\nBoth armies have been defeated, but " + fortressName + " is still occupied by " + currentKingdom.getName() + ".";
        }
    }
    @Override
    public boolean exit() {
        return false;
    }
}
