package Commands;

import World.CommandManager;
import World.Kingdom;

/**
 * Třída pro rozhovor s královstvím a zvyšování loajality.
 * Umožňuje hráči navázat kontakt s královstvím a ovlivnit jeho loajalitu.
 */
public class Talk extends Command {

    private CommandManager worldCommandManager;

    /**
     * Konstruktor pro vytvoření příkazu pro rozhovor s královstvím.
     * @param worldCommandManager Instance CommandManager pro správu světa a pozic.
     */
    public Talk(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    /**
     * Provede rozhovor s aktuálním královstvím a zvyšuje jeho loajalitu.
     * @return Výstupní zpráva o probíhajícím rozhovoru a změně loajality.
     */
    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        try {
            if(currentKingdom.isConquered().equals("not conquered")){
                currentKingdom.setLoyalty(1);
                return currentKingdom.getDialog(worldCommandManager.currentPosition);
            } else if(currentKingdom.isConquered().equals("conquered")){
                return currentKingdom.getDialog(worldCommandManager.currentPosition) + "\nBut you have already conquered me.";
            } else {
                return "";
            }

        } catch (Exception e) {
            return "\nDošlo k chybě při pokusu o rozhovor s královstvím: " + e.getMessage();
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
