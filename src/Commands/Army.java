package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Třída Army reprezentuje armádní mechanismus ve hře.
 * Umožňuje útoky na pevnosti, obranu a použití speciálních předmětů.
 */
public class Army extends Command {

    private CommandManager worldCommandManager;
    private Scanner scanner = new Scanner(System.in);
    private Map<String, Boolean> fortresses = new HashMap<>();
    private String[] fortressesNames = new String[3];
    private Inventory inventory = new Inventory();
    private String command;
    private int count = 0;
    private int occupiedIndex = -1;

    /**
     * Konstruktor třídy Army, inicializuje pevnosti a připojí správce příkazů.
     * @param worldCommandManager Správce příkazů ve světě hry.
     */
    public Army(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;

        fortresses.put("main castle", false);
        fortresses.put("iron keep", false);
        fortresses.put("armyhold", false);
        setFortressesNames();
    }

    /**
     * Nastaví názvy pevností.
     */
    private void setFortressesNames() {
        this.fortressesNames[0] = "Main Castle";
        this.fortressesNames[1] = "Iron Keep";
        this.fortressesNames[2] = "Armyhold";
    }

    /**
     * Získá název pevnosti podle indexu.
     * @param index Index pevnosti.
     * @return Název pevnosti nebo zpráva o špatném indexu.
     */
    private String getFortressesName(int index) {
        if(index < 0 || index >= fortressesNames.length){
            return "Wrong index for fortresses.";
        }else{
            return fortressesNames[index];
        }
    }//worldCommandManager.atWar()

    /**
     * Hlavní metoda pro spuštění armádních akcí, jako je útok nebo obrana.
     * @return Výsledek příkazu jako text.
     */
    @Override
    public String execute() {
        if (getCurrentKingdom().getBattle().equals("Battling") && getCurrentKingdom().isConquered().equals("not conquered")) {
            try {
                do {
                    System.out.print("\nEnter command: \n1) attack \n2) use krystal \n3) exit army \n-> ");
                    command = scanner.nextLine();

                    if (command.equals("1") || command.equals("attack")) {
                        System.out.println(attackFortress());
                        checkIfKingdomIsConquered();
                        System.out.println(defenseFortress());
                    } else if (command.equals("2") || command.equals("use") || command.equals("krystal") || command.equals("use krystal")) {
                        System.out.println(useKrystal());
                    } else if (command.equals("3") || command.equals("exit") || command.equals("exit army")) {
                        System.out.println();
                    } else {
                        System.out.println("Invalid command");
                    }
                } while (!(command.equals("4") || command.equals("exit") || command.equals("exit army")));
                return "\nExiting army.";
            } catch (Exception e) {
                return "\nError with army.";
            }
        } else if(getCurrentKingdom().getBattle().equals("Not Battling") && getCurrentKingdom().isConquered().equals("not conquered")){
            return "\nYou are not at war.";
        }else {
            return "\nYou have already conquered this kingdom.";
        }
    }

    /**
     * Brání pevnost proti útoku nepřítele.
     * @return Výsledek obrany jako text.
     */
    public String defenseFortress() {
        try {
            if(getCurrentKingdom().isConquered().equals("not conquered") && getCurrentKingdom().getBattle().equals("Battling")){

                int fortressNumber = -1;
                for (int i = 0; i < 3; i++) {
                    if(getCurrentKingdom().isFortressOccupied(i)){
                        fortressNumber = i;
                    }
                }

                if(fortressNumber == -1){
                    return "";
                } else if(occupiedIndex == fortressNumber){
                    occupiedIndex = -1;
                    return "";
                } else {
                    System.out.println("\n" + getCurrentKingdom().getName() + " has attacked fortress " + getFortressesName(fortressNumber) + ".");
                    System.out.println("You have to defend it.");

                    if(getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) > getCurrentKingdom().getArmySize()){

                        getCurrentKingdom().setArmyInFortress(fortressNumber,
                                (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) - getCurrentKingdom().getArmySize()) / getCurrentKingdom().getFortressStrength(fortressNumber));
                        return "\nYour army has defended.\n" +
                                "You have now " + getCurrentKingdom().getArmyInFortress(fortressNumber) + " in " + getFortressesName(fortressNumber) + ".";

                    }else if(getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) < getCurrentKingdom().getArmySize()){

                        getCurrentKingdom().setFortressOccupied(fortressNumber, false);
                        getCurrentKingdom().setArmyInFortress(fortressNumber,
                                getCurrentKingdom().getArmySize() - getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber));

                        return "\nYour army has been defeated.\n" +
                                "You have lost " + getFortressesName(fortressNumber) + " in " + getCurrentKingdom().getName() + ".";
                    }else{

                        getCurrentKingdom().setArmyInFortress(fortressNumber,0);
                        return "\nBoth armies have been defeated.\n" +
                                "You still have the fortress " + getFortressesName(fortressNumber) + ".\n" +
                                "Your army in this fortress is " + getCurrentKingdom().getArmyInFortress(fortressNumber) + ".";

                    }
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return "Error while defending fortress.";
        }
    }

    /**
     * Útočí na vybranou pevnost.
     * @return Výsledek útoku jako text.
     */
    public String attackFortress() {
        try {
            checkFortress();

            System.out.print("\nEnter fortress number to attack: ");
            int fortressNumber = scanner.nextInt();
            scanner.nextLine();
            occupiedIndex = fortressNumber-1;

            if (fortressNumber < 1 || fortressNumber > 3) {
                return "\nInvalid fortress number.";
            }
            fortressNumber--;

            if (getCurrentKingdom().isFortressOccupied(fortressNumber)) {
                return "\nThis fortress is already yours.";
            }

            if (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) > getMyKingdom().getArmySize() + getMyKingdom().getStrength()) {
                getCurrentKingdom().setFortressOccupied(fortressNumber, false);

                getCurrentKingdom().setArmyInFortress(fortressNumber, (((getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber)) - getMyKingdom().getArmySize())) / getCurrentKingdom().getFortressStrength(fortressNumber));
                getMyKingdom().setArmy(0);

                return "\nYou have been defeated by " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";

            } else if (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) < getMyKingdom().getArmySize() + getMyKingdom().getStrength()) {
                getCurrentKingdom().setFortressOccupied(fortressNumber, true);

                getMyKingdom().setArmy((getMyKingdom().getArmySize() - (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber))) / 3);
                getCurrentKingdom().setArmyInFortress(fortressNumber, 0);

                return "\nYou occupied " + getFortressesName(fortressNumber) + " in " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";

            } else {
                getCurrentKingdom().setFortressOccupied(fortressNumber, false);

                getCurrentKingdom().setArmyInFortress(fortressNumber, 0);
                getMyKingdom().setArmy(0);

                return "\nBoth armies have been defeated, but " + getFortressesName(fortressNumber) + " is still occupied by " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";
            }
        } catch (Exception e) {
            return "Error while attacking fortress.";
        }
    }

    /**
     * Použije krystal k oslabení pevnosti.
     * @return Výsledek použití krystalu jako text.
     */
    public String useKrystal() {
        try {
            checkFortress();

            if (count == 0) {
                return "\nYou have all the fortresses.";
            } else {
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

                if(getCurrentKingdom().getFortressStrength(fortressNumber) <= 1) {
                    return "\nYou cannot use more krystals on this fortress.";
                }else{
                    if(inventory.getResourceAmount(4) > 0){
                        getCurrentKingdom().setFortressesStrength(fortressNumber, getCurrentKingdom().getFortressStrength(fortressNumber)-1);
                        inventory.removeItem(4, 1);
                        return "\nYou used krystal on fortress " + getFortressesName(fortressNumber) + ".\nThis fortresses strength is now " + getCurrentKingdom().getFortressStrength(fortressNumber) + ".";
                    } else {
                        return "\nYou don't have enough krystals.";
                    }
                }
            }
        } catch (Exception e) {
            return "Error while attacking fortress.";
        }
    }

    /**
     * Kontroluje, zda bylo království dobyto.
     */
    private void checkIfKingdomIsConquered() {
        boolean conquered = true;
        for (int i = 0; i < 3; i++) {
            if (!getCurrentKingdom().isFortressOccupied(i)) {
                conquered = false;
            }
        }
        if (conquered) {
            getCurrentKingdom().setConquered("conquered");
            getCurrentKingdom().setBattle("Not Battling");
            getMyKingdom().setMyArmy(1000);
            System.out.println("\nYou have conquered the entire kingdom of " + getCurrentKingdom().getName() + ".\nYour army has increased by 1000 soldiers.");
        }
    }

    /**
     * Kontroluje, které pevnosti jsou obsazené.
     */
    private void checkFortress() {
        count = 0;

        if (getCurrentKingdom() == null) {
            System.out.println("\nError: Unknown kingdom.");
        }

        System.out.println();
        for (int i = 0; i < 3; i++) {
            if (!getCurrentKingdom().isFortressOccupied(i)) {
                count++;
                switch (i) {
                    case 0:
                        System.out.println("1) " + getFortressesName(i));
                        break;
                    case 1:
                        System.out.println("2) " + getFortressesName(i));
                        break;
                    case 2:
                        System.out.println("3) " + getFortressesName(i));
                        break;
                }
            }
        }
    }

    /**
     * Získá aktuální království, ve kterém se hráč nachází.
     * @return Aktuální království.
     */
    private Kingdom getCurrentKingdom() {
        return worldCommandManager.world.get(worldCommandManager.currentPosition);
    }

    /**
     * Získá hráčovo vlastní království.
     * @return Hráčovo království.
     */
    private Kingdom getMyKingdom() {
        return worldCommandManager.world.get(worldCommandManager.start);
    }

    /**
     * Ukončí armádní operace.
     * @return Vždy vrací false.
     */

    @Override
    public boolean exit() {
        return false;
    }
}
