package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Třída {@code Negotiation} umožňuje hráči vyjednávat s královstvím.
 * Hráč si může vybrat mezi válkou, mírovými podmínkami nebo vytvořením aliance s jiným královstvím.
 */
public class Negotiation extends Command {

    private CommandManager worldCommandManager;

    /**
     * Konstruktor pro vytvoření příkazu vyjednávání.
     * Inicializuje správce příkazů {@code worldCommandManager}.
     *
     * @param worldCommandManager Správce příkazů pro hru.
     */
    public Negotiation(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    /**
     * Provede vyjednávání s aktuálním královstvím.
     * Umožňuje hráči zahájit válku, dohodnout mírové podmínky nebo vytvořit alianci s královstvím.
     *
     * @return Textová zpráva s výsledkem vyjednávání, včetně výsledku války, mírové dohody nebo aliance.
     */
    @Override
    public String execute() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);
            Kingdom myKingdom = worldCommandManager.world.get(worldCommandManager.start);

            // Kontrola, zda bylo království již dobyto
            if(currentKingdom.isConquered().equals("conquered")){
                return "\nYou have already conquered this kingdom.";
            } else {
                // Umožňuje hráči vybrat akci
                System.out.print("\nEnter what you want to do: \n1) start war \n2) peace terms \n3) alliance \n");
                String request = scanner.nextLine().toLowerCase();

                // Zpracování požadavku na válku
                if (request.equals("1") || request.equals("war") || request.equals("start war") || request.equals("start")) {
                    currentKingdom.setLoyalty(0);  // Nastavení loajality na 0, když začíná válka
                    currentKingdom.setBattle("Battling");  // Nastavení stavu bitvy na "Battling"
                    return "\nYou are attacking " + currentKingdom.getName() + ". All trading and alliances have been broken.";

                    // Zpracování mírových podmínek
                } else if (request.equals("2") || request.equals("peace terms")) {

                    if (currentKingdom.getBattle().equals("Battling") || currentKingdom.isConquered().equals("Not Conquered")) {
                        // Požadavky na mírové podmínky
                        System.out.println("Peace terms: \n - 1) You will leave this kingdom and all of its fortresses.\n - 2) You will give this kingdom all items.\nDo you agree? (yes/no)");

                        String terms = scanner.nextLine().toLowerCase();
                        if (terms.equals("yes") || terms.equals("y")) {
                            // Údržba království po přijetí mírových podmínek
                            for (int i = 0; i < 3; i++) {
                                currentKingdom.setFortressOccupied(i, false);
                            }

                            // Přenos předmětů mezi královstvími
                            currentKingdom.addItems("resources", myKingdom.getInventory(1), "items");
                            currentKingdom.addItems("scrolls", myKingdom.getInventory(2), "items");
                            currentKingdom.addItems("metals", myKingdom.getInventory(3), "items");
                            currentKingdom.addItems("krystals", myKingdom.getInventory(4), "items");

                            // Vymazání svitků z hráčova království
                            for (int i = myKingdom.getSrcollsSize(); i >= 1; i--) {
                                myKingdom.setScrolls(i, false);
                            }
                            myKingdom.setInventory(0,0);  // Vymazání inventáře hráče
                            currentKingdom.setBattle("Not Battling");  // Nastavení stavu bitvy na "Not Battling"
                            currentKingdom.setLoyalty(3);  // Nastavení loajality na 3

                            return "\nYou gave " + currentKingdom.getName() + " all items\nYou are now in peace.";
                        } else if (terms.equals("no") || terms.equals("n")) {
                            return "\nYou declined the peace terms of " + currentKingdom.getName() + ".";
                        } else {
                            return "\nInvalid input for peace terms.";
                        }
                    } else {
                        return "\nYou are not at war.";  // Pokud není hráč ve válce
                    }
                    // Zpracování žádosti o alianci
                } else if (request.equals("3") || request.equals("alliance")) {

                    if (currentKingdom.getBattle().equals("Battling")) {
                        return "\nYou are at war.\nYou cannot currently make an alliance.";  // Pokud je hráč ve válce
                    } else if(currentKingdom.getLoyalty() == 10){
                        currentKingdom.setConquered("conquered");  // Pokud loajalita je 10, království je dobyto
                        return "\nYou have made an alliance with " + currentKingdom.getName() + ".\nThis kingdom is conquered.";
                    } else if (currentKingdom.getLoyalty() < 10) {
                        return "\nThis kingdom needs to have loyalty 10.";  // Pokud loajalita není dostatečná pro alianci
                    } else {
                        return "\nYou currently cannot make an alliance.";  // Pokud není možné uzavřít alianci
                    }
                } else {
                    return "Invalid input for action.";  // Neplatný vstup
                }
            }

            // Zachytávání chyby při nesprávném vstupu
        } catch (InputMismatchException e) {
            return "\nInvalid input type. Please enter a valid option.";
        } catch (Exception e) {
            return "\nAn error occurred while taking negotiation.";  // Chyba během vyjednávání
        }
    }

    /**
     * Určuje, zda tento příkaz ukončí běh programu.
     * Tento příkaz {@code Negotiation} nikdy neukončí program, takže vždy vrací {@code false}.
     *
     * @return Vždy vrací {@code false}.
     */
    @Override
    public boolean exit() {
        return false;
    }
}
