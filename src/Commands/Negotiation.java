package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Třída Negotiation umožňuje hráči vyjednávat s královstvím.
 * Hráč si může vybrat mezi válkou, mírovými podmínkami nebo vytvořením aliance.
 */
public class Negotiation extends Command {

    private CommandManager worldCommandManager;

    /**
     * Konstruktor pro vytvoření příkazu vyjednávání.
     * @param worldCommandManager Správce příkazů pro hru.
     */
    public Negotiation(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    /**
     * Provede vyjednávání s aktuálním královstvím.
     * @return Textová zpráva s výsledkem vyjednávání.
     * Zpracuje mírové podmínky.
     * Zpracuje vytvoření aliance s královstvím.
     */
    @Override
    public String execute() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);
            Kingdom myKingdom = worldCommandManager.world.get(worldCommandManager.start);

            if(currentKingdom.isConquered().equals("conquered")){
                return "\nYou have already conquered this kingdom.";
            }else {
                System.out.print("\nEnter what you want to do: \n1) start war \n2) peace terms \n3) alliance \n");
                String request = scanner.nextLine().toLowerCase();

                if (request.equals("1") || request.equals("war") || request.equals("start war") || request.equals("start")) {
                    currentKingdom.setLoyalty(0);
                    currentKingdom.setBattle("Battling");
                    return "\nYou are attacking " + currentKingdom.getName() + ". All trading and alliances have been broken.";
                } else if (request.equals("2") || request.equals("peace terms")) {

                    if (currentKingdom.getBattle().equals("Battling") || currentKingdom.isConquered().equals("Not Conquered")) {
                        System.out.println("Peace terms: \n - 1) You will leave this kingdom and all of its fortresses.\n - 2) You will give this kingdom all items.\nDo you agree? (yes/no)");

                        String terms = scanner.nextLine().toLowerCase();
                        if (terms.equals("yes") || terms.equals("y")) {
                            for (int i = 0; i < 3; i++) {
                                currentKingdom.setFortressOccupied(i, false);
                            }

                            currentKingdom.addItems("resources", myKingdom.getInventory(1), "items");
                            currentKingdom.addItems("scrolls", myKingdom.getInventory(2), "items");
                            currentKingdom.addItems("metals", myKingdom.getInventory(3), "items");
                            currentKingdom.addItems("krystals", myKingdom.getInventory(4), "items");

                            for (int i = myKingdom.getSrcollsSize(); i >= 1; i--) {
                                myKingdom.setScrolls(i, false);
                            }
                            myKingdom.setInventory(0,0);
                            currentKingdom.setBattle("Not Battling");
                            currentKingdom.setLoyalty(3);

                            return "\nYou gave " + currentKingdom.getName() + " all items\nYou are now in peace.";
                        } else if (terms.equals("no") || terms.equals("n")) {
                            return "\nYou declined the peace terms of " + currentKingdom.getName() + ".";
                        } else {
                            return "\nInvalid input for peace terms.";
                        }
                    } else {
                        return "\nYou are not at war.";
                    }
                } else if (request.equals("3") || request.equals("alliance")) {

                    if (currentKingdom.getBattle().equals("Battling")) {
                        return "\nYou are at war.\nYou cannot currently make an alliance.";
                    } else if(currentKingdom.getLoyalty() == 10){
                        currentKingdom.setConquered("conquered");
                        return "\nYou have made an alliance with " + currentKingdom.getName() + ".\nThis kingdom is conquered.";
                    } else if (currentKingdom.getLoyalty() < 10) {
                        return "\nThis kingdom needs to have loyalty 10.";
                    } else {
                        return "\nYou currently cannot make an allience.";
                    }

                } else {
                    return "Invalid input for action.";
                }
            }


        } catch (InputMismatchException e) {
            return "\nInvalid input type. Please enter a valid option.";
        } catch (Exception e) {
            return "\nAn error occurred while taking negotiation.";
        }
    }

    /**
     * Určuje, zda tento příkaz ukončí běh programu.
     * @return Vždy vrací false.
     */
    @Override
    public boolean exit() {
        return false;
    }
}
