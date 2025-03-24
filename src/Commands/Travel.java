package Commands;
import World.CommandManager;

import java.util.Scanner;

public class Travel extends Command {
    private CommandManager worldCommandManager;

    public Travel(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        worldCommandManager.showBorders();

        String result = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            System.out.print("Enter name of the kingdom: ");
            String destination = scanner.nextLine().toLowerCase();

            result = worldCommandManager.travelTo(destination);
        } catch (Exception e) {
            result = "Invalid input! Please try again.";
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return result;
    }

    @Override
    public boolean exit() {
        return false;
    }
}