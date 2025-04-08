package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

/**
 * Třída {@code Maintain} umožňuje hráči udržovat svůj majetek a vybavení, čímž posiluje jeho armádu.
 * Hráč může provádět údržbu majetku nebo vybavení za použití určitých zdrojů.
 */
public class Maintain extends Command {

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);
    private Inventory inventory = new Inventory();

    /**
     * Konstruktor pro třídu {@code Maintain}.
     * Inicializuje správce příkazů {@code worldCommandManager}.
     *
     * @param worldCommandManager Správce příkazů pro svět hry.
     */
    public Maintain(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    /**
     * Provede příkaz pro údržbu majetku nebo vybavení, což posiluje armádu.
     * Hráč může provádět údržbu za použití zdrojů z inventáře.
     * Pokud jsou zdroje dostupné, údržba zlepší sílu armády.
     *
     * @return Textová zpráva informující o výsledku akce, například zda byla údržba provedena nebo zda jsou zdroje nedostatečné.
     */
    @Override
    public String execute() {
        try {
            System.out.print("\nEnter, if you want to maintain: \n1) properties \n2) gear\n");
            String request = scanner.nextLine();

            if (request.equals("1") || request.equals("properties")) {  // Kontrola, zda uživatel chce udržovat majetek
                if(inventory.getResourceAmount(1) > 0){  // Kontrola, zda jsou dostupné zdroje
                    getMyKingdom().setStrength(0.1);  // Posílení síly armády
                    inventory.removeItem(1, 1);  // Odebrání 1 zdroje z inventáře
                    return "\nYou used 1 resource. \nYou maintained your properties and now your army is stronger by 0,1.";
                } else {
                    return "\nNot enough resources to maintain.";  // Pokud nejsou dostatečné zdroje
                }

            } else if (request.equals("2") || request.equals("gear")) {  // Kontrola, zda uživatel chce udržovat vybavení
                if(inventory.getResourceAmount(3) > 0){  // Kontrola, zda jsou dostupné kovy
                    getMyKingdom().setStrength(0.2);  // Posílení síly armády
                    inventory.removeItem(3, 1);  // Odebrání 1 kovu z inventáře
                    return "\nYou used 1 metal. \nYou maintained your gear and now your army is stronger by 0,2.";
                } else {
                    return "\nNot enough metals to maintain.";  // Pokud nejsou dostatečné kovy
                }
            } else {
                return "\nInvalid input.";  // Pokud uživatel zadal neplatnou volbu
            }
        } catch (Exception e) {
            return "\nAn error occurred while attempting to maintain.";  // Chyba během provádění údržby
        }
    }

    /**
     * Získá hráčovo vlastní království.
     * Používá správce příkazů, aby získal aktuální království hráče.
     *
     * @return Instance třídy {@code Kingdom} představující hráčovo hlavní království.
     */
    private Kingdom getMyKingdom() {
        return worldCommandManager.world.get(worldCommandManager.start);  // Získání království podle indexu
    }

    /**
     * Určuje, zda tento příkaz ukončí běh programu.
     * Tento příkaz {@code Maintain} nikdy neukončí program, takže vždy vrací {@code false}.
     *
     * @return Vždy vrací {@code false}.
     */
    @Override
    public boolean exit() {
        return false;
    }
}
