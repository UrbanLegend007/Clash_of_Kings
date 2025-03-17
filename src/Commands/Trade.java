package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.HashMap;
import java.util.Scanner;

public class Trade extends Command {

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);
    private static final HashMap<String, Integer> itemValues = new HashMap<>();

    public Trade(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.print("Enter item to trade (krystals, resources, scrolls, metals): ");
        String request = scanner.nextLine();

        if (!currentKingdom.getResources().containsKey(request) || currentKingdom.getResources().get(request) == 0) {
            return "This kingdom has no " + request + " to trade.";
        }

        return "";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
