package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

public class Negotiation extends Command{

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);

    public Negotiation(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.print("Enter what you want to do: \n1) attack \n2) peace terms \n3) alience \n");
        String request = scanner.nextLine();

        if(request.equals("1") || request.equals("attack")){
            return "You are attacking " + currentKingdom.getName() + ". All trading and aliences have been broken.";
        }else if(request.equals("2") || request.equals("peace terms")){
            System.out.println("Enter your peace terms.");
            String terms = scanner.nextLine();

            return "Your peace terms are accepted by " + currentKingdom.getName() + ". Your peace terms are " + terms + ".";
        }else if(request.equals("3") || request.equals("alience")){
            return "You have made alience with " + currentKingdom.getName() + ".";
        }else{
            return "Invalid input";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
