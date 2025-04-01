package Commands;

/**
 * Třída Exit představuje příkaz pro ukončení hry.
 * Po zavolání metody execute() vrátí zprávu "Game over."
 * a metoda exit() signalizuje konec hry.
 */
public class Exit extends Command {

    /**
     * Vrátí zprávu oznamující konec hry.
     * @return Text "Game over."
     */
    @Override
    public String execute() {
        return "Game over.";
    }

    /**
     * Určuje, že tento příkaz má ukončit hru.
     * @return Vždy vrací true.
     */
    @Override
    public boolean exit() {
        return true;
    }
}
