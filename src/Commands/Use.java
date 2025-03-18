package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

public class Use extends Command {

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);

    public Use(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.print("Enter what you want to use: \n1) krystal 1 \n2) krystal 2 \n3) krystal 3 \n");
        String krystal = scanner.nextLine();

        if(krystal.equals("1") || krystal.equals("krystal 1")){
            return "You have used krystal 1";
        }else if(krystal.equals("2") || krystal.equals("krystal 2")){
            return "You have used krystal 2";
        }else if(krystal.equals("3") || krystal.equals("krystal 3")){
            return "You have used krystal 3";
        }else{
            return "Invalid krystal";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
