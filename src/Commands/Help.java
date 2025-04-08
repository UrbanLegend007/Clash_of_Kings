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
                "You can conquer them by declaring them a war in negotiation, and then occupy all of the 3 fortresses, but they can attack you as well.\n" +
                "You get their loyalty by talking to them, trading with them or getting items from them.\n" +
                "Scrolls hold the information about the enemies army.\n" +
                "You can also trade with them, negotiate, and talk to them.\n" +
                "Your army, fortresses strength and inventory will be rebuilt when you return to My Kingdom.\n" +
                "Your army will increase by defeating enemies.\n" +
                "You can also upgrade your army in maintain.";
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
