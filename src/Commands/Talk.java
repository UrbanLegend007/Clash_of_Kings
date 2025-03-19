package Commands;

import World.CommandManager;
import World.Kingdom;

public class Talk extends Command{

    private CommandManager worldCommandManager;

    public Talk(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);
        return currentKingdom.getDialog(worldCommandManager.currentPosition);
    }

    @Override
    public boolean exit() {
        return false;
    }
}