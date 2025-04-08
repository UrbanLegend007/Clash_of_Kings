package Commands;

import World.CommandManager;

import java.util.Scanner;

/**
 * Třída pro cestování mezi královstvími.
 * Umožňuje hráči cestovat do jiných království zadáním jejich názvu.
 */
public class Travel extends Command {
    private CommandManager worldCommandManager;

    /**
     * Konstruktor pro vytvoření příkazu pro cestování.
     * @param worldCommandManager Instance CommandManager pro správu světa a pozic.
     */
    public Travel(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    /**
     * Provede cestování do zadaného království.
     * Hráč zadá název království a bude přesměrován na příslušnou lokaci.
     * @return Výstupní zpráva o úspěchu nebo chybě při cestování.
     */
    @Override
    public String execute() {
        // Zobrazení hranic království
        worldCommandManager.showBorders();

        Scanner scanner;
        try {
            scanner = new Scanner(System.in);
            System.out.print("\nEnter name of the kingdom: ");
            String destination = scanner.nextLine().toLowerCase(); // Uživatelský vstup pro název království

            // Pokusí se cestovat na požadovanou destinaci
            return worldCommandManager.travelTo(destination); // Zavolání metody pro cestování
        } catch (Exception e) {
            return "\nInvalid input! Please try again."; // Zachycení chyby při neplatném vstupu
        }
    }

    /**
     * Určuje, zda příkaz způsobí ukončení hry nebo operace.
     * @return false, protože tento příkaz neukončuje hru.
     */
    @Override
    public boolean exit() {
        return false;
    }
}
