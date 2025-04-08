package Commands;

import World.CommandManager;
import World.Kingdom;

/**
 * Třída {@code Talk} umožňuje hráči provádět rozhovor s královstvím
 * a zvyšovat jeho loajalitu. Pokud království nebylo dobyto, loajalita se
 * zvýší, a hráč dostane odpovídající dialog.
 */
public class Talk extends Command {

    private CommandManager worldCommandManager;

    /**
     * Konstruktor pro vytvoření příkazu pro rozhovor s královstvím.
     * Tento příkaz inicializuje správce příkazů {@code worldCommandManager},
     * který umožňuje správu světa a pozic hráče.
     *
     * @param worldCommandManager Instance CommandManager pro správu světa a pozic.
     */
    public Talk(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    /**
     * Provede rozhovor s aktuálním královstvím a zvyšuje jeho loajalitu, pokud
     * království není dobyto. Vrací zprávu podle aktuálního stavu království.
     *
     * Pokud království není dobyto, loajalita se zvýší o 1. Pokud je dobyto,
     * vrátí zprávu, že království již bylo dobyto. Pokud došlo k chybě při
     * získávání dialogu, vrátí chybovou zprávu.
     *
     * @return Textová zpráva o probíhajícím rozhovoru a změně loajality království.
     */
    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        try {
            // Kontrola stavu království a zvýšení loajality, pokud není dobyto
            if(currentKingdom.isConquered().equals("not conquered")){
                currentKingdom.setLoyalty(1);  // Zvyšuje loajalitu na 1
                return currentKingdom.getDialog(worldCommandManager.currentPosition);  // Získá dialog pro dané království
            } else if(currentKingdom.isConquered().equals("conquered")){
                return currentKingdom.getDialog(worldCommandManager.currentPosition) + "\nBut you have already conquered me.";  // Pokud je dobyto
            } else {
                return "";  // Pokud není definovaný stav, vrátí prázdnou zprávu
            }

        } catch (Exception e) {
            return "\nDošlo k chybě při pokusu o rozhovor s královstvím.";  // Zachycení obecné chyby
        }
    }

    /**
     * Určuje, zda tento příkaz způsobí ukončení hry nebo operace.
     * Tento příkaz {@code Talk} nikdy neukončí program, vždy vrací {@code false}.
     *
     * @return Vždy vrací {@code false}, protože tento příkaz neukončuje hru.
     */
    @Override
    public boolean exit() {
        return false;
    }
}
