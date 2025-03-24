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

    public Army(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;

        fortresses.put("main castle", false);
        fortresses.put("iron keep", false);
        fortresses.put("armyhold", false);
    }

    @Override
    public String execute() {
        if(worldCommandManager.atWar()){
            do{
                System.out.print("\nEnter command: \n1) attack \n2) defense \n3) exit\n-> ");
                command = scanner.nextLine();

                if (command.equals("1") || command.equals("attack")) {
                    System.out.println(attackFortress());
                } else if (command.equals("2") || command.equals("defense")) {
                    System.out.println(defenseFortress());
                }else if(command.equals("3") || command.equals("exit")){
                    System.out.println();
                }else {
                    System.out.println("Invalid command");
                }
            }while(!(command.equals("3") || command.equals("exit")));

            return "Exiting army.";
        }else{
            return "You are not at war.";
        }
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

            if (getCurrentKingdom().isFortressOccupied(fortressNumber)) {
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

            getCurrentKingdom().getArmyInFortress(1);
            return "\nYour army has defended.";
        }
    }

    private String attackFortress() {

        checkFortress();

        if(count == 0){
            return "\nYou have all the fortresses.";
        }else{
            System.out.print("\nEnter fortress number to attack: ");
            int fortressNumber = scanner.nextInt();
            scanner.nextLine();

            if (fortressNumber < 1 || fortressNumber > 3) {
                return "\nInvalid fortress number.";
            }
            fortressNumber--;

            if (getCurrentKingdom().isFortressOccupied(fortressNumber)) {
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

            if(getCurrentKingdom().getArmyInFortress(fortressNumber)*2 > getMyKingdom().getArmySize()) {
                getCurrentKingdom().setFortressOccupied(fortressNumber, false);

                getCurrentKingdom().setArmyInFortress(fortressNumber,((getCurrentKingdom().getArmySize()*2)-getMyKingdom().getArmySize())/3);
                getMyKingdom().setArmy(0);

                return "\nYou have been defeated by " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";

            }else if(getCurrentKingdom().getArmyInFortress(fortressNumber)*2 < getMyKingdom().getArmySize()) {
                getCurrentKingdom().setFortressOccupied(fortressNumber, true);

                getCurrentKingdom().setArmyInFortress(fortressNumber,0);
                getMyKingdom().setArmy(getMyKingdom().getArmySize() - getCurrentKingdom().getArmySize()*2);

                checkIfKingdomIsConquered();

                return "\nYou occupied " + fortressName + " in " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";

            }else{
                getCurrentKingdom().setFortressOccupied(fortressNumber, false);

                getCurrentKingdom().setArmyInFortress(fortressNumber,0);
                getMyKingdom().setArmy(0);

                return "\nBoth armies have been defeated, but " + fortressName + " is still occupied by " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";
            }
        }
    }

    private void checkIfKingdomIsConquered() {
        boolean conquered = true;
        for (int i = 0; i < 3; i++) {
            if (!getCurrentKingdom().isFortressOccupied(i)) {
                conquered = false;
            }
        }
        if(conquered){
            getCurrentKingdom().setConquered("conquered");
            System.out.println("\nYou have conquered the entire kingdom of " + getCurrentKingdom().getName() + "!");
        }
    }

    private void checkFortress() {
        count = 0;

        if (getCurrentKingdom() == null) {
            System.out.println("\nError: Unknown kingdom.");
        }

        System.out.println();
        for (int i = 0; i < 3; i++) {
            if(!getCurrentKingdom().isFortressOccupied(i)){
                count++;
                switch(i){
                    case 0:
                        System.out.println("1) Main Castle");
                        break;
                    case 1:
                        System.out.println("2) Iron Keep");
                        break;
                    case 2:
                        System.out.println("3) Armyhold");
                        break;
                }
            }
        }
    }

    private Kingdom getCurrentKingdom() {
        return worldCommandManager.world.get(worldCommandManager.currentPosition);
    }

    private Kingdom getMyKingdom() {
        return worldCommandManager.world.get(worldCommandManager.start);
    }

    @Override
    public boolean exit() {
        return false;
    }
}
