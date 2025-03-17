package Commands;

public class Exit extends Command {
    @Override
    public String execute() {
        return "Game over.";
    }

    @Override
    public boolean exit() {
        return true;
    }
}