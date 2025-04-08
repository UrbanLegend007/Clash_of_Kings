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

/*

lepe ohlidat army v fortress + velikost army, kdyz dobydu fortress (misto +1000 bude +1200 aby /3 a potom se to rozdeli podle fortress, tedy +400, +400, +400)
lepsi help

menit lepe loyalty
lepsi dialogy

dodelat java docs
 */