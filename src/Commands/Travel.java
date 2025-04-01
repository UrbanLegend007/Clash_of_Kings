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

        Scanner scanner;
        try {
            scanner = new Scanner(System.in);
            System.out.print("Enter name of the kingdom: ");
            String destination = scanner.nextLine().toLowerCase();

            return worldCommandManager.travelTo(destination);
        } catch (Exception e) {
            return "Invalid input! Please try again.";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }
}