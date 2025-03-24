package Commands;

import World.CommandManager;
import World.Kingdom;

public class Talk extends Command {

    private CommandManager worldCommandManager;

    public Talk(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);
        String result = "";

        try {
            currentKingdom.setLoyalty(1);
            result = currentKingdom.getDialog(worldCommandManager.currentPosition);
        } catch (Exception e) {
            result = "An error occurred while attempting to talk to the kingdom.";
        }

        return result;
    }

    @Override
    public boolean exit() {
        return false;
    }
}
