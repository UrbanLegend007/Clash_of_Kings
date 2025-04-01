package Commands;

public class Help extends Command{

    public Help() {

    }

    @Override
    public String execute() {
        return "\nYou have to conquere all the kingdoms.\nYou can conquere them by defeating them in a battle, or get their loyalty and make an alliance." +
                "\nYou can also trade with them, make negotiation and talk to them.\nYour army and inventory will rebuilt when you return to My kingdom." +
                "\nYour army will increase by defeating enemies.\nYou can also upgrade your army.";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
