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
    private int count = 0;

    Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);
    Kingdom myKingdom = worldCommandManager.world.get(worldCommandManager.start);

    public Army(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
        fortresses.put("main castle", false);
        fortresses.put("iron keep", false);
        fortresses.put("armyhold", false);
    }

    @Override
    public String execute() {
        do{
            System.out.print("\nEnter command: \n1) attack \n2) defense \n3) reinforcements \n4) exit\n-> ");
            command = scanner.nextLine();

            if (command.equals("1") || command.equals("attack")) {
                System.out.println(attackFortress());
            } else if (command.equals("2") || command.equals("defense")) {
                System.out.println(defenseFortress());
            } else if (command.equals("3") || command.equals("reinforcements")) {
                System.out.println("Your army has reinforcements.");
            } else {
                System.out.println("Invalid command");
            }
        }while(!(command.equals("4") || command.equals("exit")));

        return "Exiting army.";
    }

    private String defenseFortress() {

        checkFortress();

        if(count == 0){
            return "\nYou have all the fortresses.";
        }else{
            System.out.println("\nEnter fortress to defend.");
            int fortressNumber = scanner.nextInt();
            scanner.nextLine();

            if (fortressNumber < 1 || fortressNumber > 3) {
                return "\nInvalid fortress number.";
            }
            fortressNumber--;

            if (currentKingdom.isFortressOccupied(fortressNumber)) {
                return "\nThis fortress is already yours.";
            }

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

            currentKingdom.getArmyInFortress(1);
            return "\nYour army has defended.";
        }
    }

    private String attackFortress() {

        checkFortress();

        if(count == 0){
            return "\nYou have all the fortresses.";
        }else{
            System.out.print("Enter fortress number to attack: ");
            int fortressNumber = scanner.nextInt();
            scanner.nextLine();

            if (fortressNumber < 1 || fortressNumber > 3) {
                return "\nInvalid fortress number.";
            }
            fortressNumber--;

            if (currentKingdom.isFortressOccupied(fortressNumber)) {
                return "\nThis fortress is already yours.";
            }

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

            if(currentKingdom.getArmyInFortress(fortressNumber)*2 > myKingdom.getArmySize()) {
                currentKingdom.setFortressOccupied(fortressNumber, false);

                currentKingdom.setArmyInFortress(fortressNumber,((currentKingdom.getArmySize()*2)-myKingdom.getArmySize())/3);
                myKingdom.setArmy(0);

                return "\nYou have been defeated by " + currentKingdom.getName() + ".\n" + currentKingdom.getName()
                        + "Your army has " + myKingdom.getArmySize() + " soldiers.";

            }else if(currentKingdom.getArmyInFortress(fortressNumber)*2 < myKingdom.getArmySize()) {
                currentKingdom.setFortressOccupied(fortressNumber, true);

                currentKingdom.setArmyInFortress(fortressNumber,0);
                myKingdom.setArmy(myKingdom.getArmySize() - currentKingdom.getArmySize()*2);

                return "\nYou occupied " + fortressName + " in " + currentKingdom.getName() + ".\n"
                        + "Your army has " + myKingdom.getArmySize() + " soldiers.";

            }else{
                currentKingdom.setFortressOccupied(fortressNumber, false);

                currentKingdom.setArmyInFortress(fortressNumber,0);
                myKingdom.setArmy(0);

                return "\nBoth armies have been defeated, but " + fortressName + " is still occupied by " + currentKingdom.getName() + ".\n"
                        + "Your army has " + myKingdom.getArmySize() + " soldiers.";
            }
        }
    }

    private void checkFortress() {
        count = 0;

        if (currentKingdom == null) {
            System.out.println("\nError: Unknown kingdom.");
        }

        for (int i = 0; i < 3; i++) {
            if(currentKingdom.isFortressOccupied(i)){
                count++;
                switch(i){
                    case 0:
                        System.out.println("\n1) Main Castle");
                        break;
                    case 1:
                        System.out.println("\n2) Iron Keep");
                        break;
                    case 2:
                        System.out.println("\n3) Armyhold");
                        break;
                }
            }
        }
    }

    @Override
    public boolean exit() {
        return false;
    }
}
