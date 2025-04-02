import World.CommandManager;

 /**
 * Hlavní třída, která slouží k spuštění hry.
 * Tato třída inicializuje třídu `CommandManager`, která se stará o správu příkazů ve hře.
 * Pokud dojde k jakékoliv výjimce při načítání třídy `CommandManager`, zobrazí se chybová zpráva.
 */
public class Main {
    public static void main(String[] args) {

        /**
         * Hlavní metoda, která je vstupním bodem aplikace.
         * Snaží se inicializovat třídu `CommandManager`. Pokud dojde k výjimce, vypíše chybovou zprávu.
         *
         * @param args argumenty příkazového řádku (nevyužívá se v této implementaci)
         */
        try {
            new CommandManager();
        } catch (Exception e) {
            System.out.println("Error loading the game.");
        }
    }
}