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
    private Inventory inventory = new Inventory();
    private String command;
    private int count = 0;
    private int occupiedIndex = -1;
    private boolean defense;
    private boolean travel;

    /**
     * Konstruktor třídy Army, inicializuje pevnosti a připojí správce příkazů.
     * @param worldCommandManager Správce příkazů ve světě hry.
     * @param defense Příznak, zda se má provést obrana pevnosti.
     * @param travel Příznak, zda došlo k přesunu mezi královstvími.
     */
    public Army(CommandManager worldCommandManager, boolean defense, boolean travel) {
        this.worldCommandManager = worldCommandManager;

        for (int i = 0; i < 3; i++) {
            fortresses.put(getCurrentKingdom().getFortressesNames(i), getCurrentKingdom().isFortressOccupied(i));
        }
        this.defense = defense;
        this.travel = travel;
    }

    /**
     * Hlavní metoda pro spuštění armádních akcí, jako je útok nebo obrana.
     * @return Výsledek příkazu jako text.
     */
    @Override
    public String execute() {
        if(travel){
            int occupied = 0;
            for (int i = 0; i < 3; i++) {
                if(getCurrentKingdom().isFortressOccupied(i)){
                    occupied++;
                    getCurrentKingdom().setFortressesStrength(i,3);
                    getCurrentKingdom().setArmyInFortress(i, (getMyKingdom().getMyArmy() / 3));
                }
            }
            if(occupied > 0){
                System.out.println("\nYour army and strength in your fortresses in " + getCurrentKingdom().getName() +" has been rebuilt.");
            }
        }
        if(defense){
            System.out.println(defenseFortress());
            return "defense";
        }else {
            if (getCurrentKingdom().getBattle().equals("Battling") && getCurrentKingdom().isConquered().equals("not conquered")) {
                try {
                    do {
                        System.out.print("\nEnter command: \n1) attack \n2) use krystal \n3) exit army \n-> ");
                        command = scanner.nextLine();

                        if (command.equals("1") || command.equals("attack")) {
                            System.out.println(attackFortress());
                            checkIfKingdomIsConquered();
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
                    System.out.println("\n                    " + getCurrentKingdom().getName() + " has ATTACKED your fortress " + getCurrentKingdom().getFortressesNames(fortressNumber) + ".");
                    System.out.println("                    You have to defend it.");

                    // Spotřeba krystalů k posílení obrany pevnosti
                    int use = 0;
                    for (int i = 0; i < 2; i++) {
                        if(getCurrentKingdom().inventoryAmount(4,"items") > 0){
                            if(getCurrentKingdom().getFortressStrength(fortressNumber) > 1){
                                getCurrentKingdom().setFortressesStrength(fortressNumber,getCurrentKingdom().getFortressStrength(fortressNumber)-1);
                                getCurrentKingdom().addItems("krystals",-1,"items");
                                use++;
                            }
                        }
                    }
                    if(use > 0){
                        System.out.println("\n" + getCurrentKingdom().getName() + " has used " + use + " krystals and now your fortress has " + getCurrentKingdom().getFortressStrength(fortressNumber) + " strength.");
                    }

                    // Výsledky bitvy - obránci vyhráli
                    if((getCurrentKingdom().getArmyInFortress(fortressNumber) + getMyKingdom().getStrength()) * getCurrentKingdom().getFortressStrength(fortressNumber) > getCurrentKingdom().getArmySize()){
                        getCurrentKingdom().setArmyInFortress(fortressNumber,
                                (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) - getCurrentKingdom().getArmySize()) / getCurrentKingdom().getFortressStrength(fortressNumber));
                        return "\n                    Your army has defended.\n" +
                                "                    You have now " + getCurrentKingdom().getArmyInFortress(fortressNumber) + " in " + getCurrentKingdom().getFortressesNames(fortressNumber) + ".";

                        // Výsledek - obránci prohráli
                    }else if((getCurrentKingdom().getArmyInFortress(fortressNumber) + getMyKingdom().getStrength()) * getCurrentKingdom().getFortressStrength(fortressNumber) < getCurrentKingdom().getArmySize()){

                        getCurrentKingdom().setFortressOccupied(fortressNumber, false);
                        getCurrentKingdom().setFortressesStrength(fortressNumber,3);
                        getCurrentKingdom().setArmyInFortress(fortressNumber,
                                getCurrentKingdom().getArmySize() / 3);

                        return "\n                    Your army has been defeated.\n" +
                                "                    You have lost " + getCurrentKingdom().getFortressesNames(fortressNumber) + " in " + getCurrentKingdom().getName() + ".";

                        // Remíza - obě armády zničeny
                    }else{
                        getCurrentKingdom().setArmyInFortress(fortressNumber,0);
                        return "\n                    Both armies have been defeated.\n" +
                                "                    You still have the fortress " + getCurrentKingdom().getFortressesNames(fortressNumber) + ".\n" +
                                "                    Your army in this fortress is " + getCurrentKingdom().getArmyInFortress(fortressNumber) + ".";
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

            // Výpočet výsledku útoku
            if (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) > getMyKingdom().getArmySize() + getMyKingdom().getStrength()) {
                getCurrentKingdom().setFortressOccupied(fortressNumber, false);

                getCurrentKingdom().setArmyInFortress(fortressNumber, (((getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber)) - getMyKingdom().getArmySize())) / getCurrentKingdom().getFortressStrength(fortressNumber));
                getMyKingdom().setArmy(0);

                return "\nYou have been defeated by " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";

            } else if (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber) < getMyKingdom().getArmySize() + getMyKingdom().getStrength()) {
                getCurrentKingdom().setFortressOccupied(fortressNumber, true);

                getMyKingdom().setArmy((getMyKingdom().getArmySize() - (getCurrentKingdom().getArmyInFortress(fortressNumber) * getCurrentKingdom().getFortressStrength(fortressNumber))) / 3);
                getCurrentKingdom().setArmyInFortress(fortressNumber, (getCurrentKingdom().getMyArmy() / 3));
                getCurrentKingdom().setFortressesStrength(fortressNumber,3);

                return "\nYou occupied " + getCurrentKingdom().getFortressesNames(fortressNumber) + " in " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.\nStrength of this fortress has been reset.\nThis fortress now has " + getCurrentKingdom().getArmyInFortress(fortressNumber) + ".";

            } else {
                getCurrentKingdom().setFortressOccupied(fortressNumber, false);

                getCurrentKingdom().setArmyInFortress(fortressNumber, 0);
                getMyKingdom().setArmy(0);

                return "\nBoth armies have been defeated, but " + getCurrentKingdom().getFortressesNames(fortressNumber) + " is still occupied by " + getCurrentKingdom().getName() + ".\n"
                        + "Your army has " + getMyKingdom().getArmySize() + " soldiers.";
            }
        } catch (Exception e) {
            return "\nError while attacking fortress.";
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
                } else {
                    if(inventory.getResourceAmount(4) > 0){
                        getCurrentKingdom().setFortressesStrength(fortressNumber, getCurrentKingdom().getFortressStrength(fortressNumber)-1);
                        inventory.removeItem(4, 1);
                        return "\nYou used krystal on fortress " + getCurrentKingdom().getFortressesNames(fortressNumber) + ".\nThis fortresses strength is now " + getCurrentKingdom().getFortressStrength(fortressNumber) + ".";
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
     * Kontroluje, zda byly všechny pevnosti v království dobyty.
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
            getMyKingdom().setMyArmy(1200);
            System.out.println("\nYou have conquered the entire kingdom of " + getCurrentKingdom().getName() + ".\nYour army has increased by 1200 soldiers.");
        }
    }

    /**
     * Zkontroluje pevnosti, které nejsou obsazeny.
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
                System.out.println((i+1) + ") " + getCurrentKingdom().getFortressesNames(i));
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
     * Získá hráčovo výchozí království.
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
