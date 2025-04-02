package Commands;

/**
 * Příkaz Help poskytuje hráči základní informace o cílech hry a dostupných možnostech.
 */
public class Help extends Command {

    /**
     * Konstruktor třídy Help.
     */
    public Help() {

    }

    /**
     * Vrací text s nápovědou o hře.
     * @return Textový řetězec obsahující herní instrukce.
     */
    @Override
    public String execute() {
        return "\nYou have to conquer all the kingdoms.\n" +
                "You can conquer them by defeating them in a battle, or get their loyalty and make an alliance.\n" +
                "You can also trade with them, negotiate, and talk to them.\n" +
                "Your army and inventory will be rebuilt when you return to My Kingdom.\n" +
                "Your army will increase by defeating enemies.\n" +
                "You can also upgrade your army.";
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
