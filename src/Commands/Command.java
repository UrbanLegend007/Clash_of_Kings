package Commands;

/**
 * Abstraktní třída Command, která definuje základní strukturu příkazů ve hře.
 * Každý konkrétní příkaz musí implementovat metody execute() a exit().
 */
public abstract class Command {

    /**
     * Spustí příkaz a vrátí jeho výstup jako text.
     * @return Výsledek provedeného příkazu.
     */
    public abstract String execute();

    /**
     * Určuje, zda má příkaz ukončit běh programu nebo dané operace.
     * @return True, pokud má být operace ukončena, jinak false.
     */
    public abstract boolean exit();

}
