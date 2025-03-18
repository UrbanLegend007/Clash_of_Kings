package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.Scanner;

public class Maintain extends Command {

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);

    public Maintain(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

        System.out.print("Enter, if you want to maintain: \n1) gear \n2) properties\n");
        String request = scanner.nextLine();

        if(request.equals("1") || request.equals("gear")){
            System.out.print("Enter metal you want to use to maintain: \n1) gold \n2) silver \n3) bronze\n");
            String metal = scanner.nextLine();

            if(metal.equals("1") || metal.equals("gold")){
                return "You maintained gear with gold.";
            }else if(metal.equals("2") || metal.equals("silver")){
                return "You maintained gear with silver.";
            }else if(metal.equals("3") || metal.equals("bronze")){
                return "You maintained gear with bronze.";
            }else{
                return "Invalid input.";
            }
        }else if(request.equals("2") || request.equals("properties")){
            System.out.print("Enter resource you want to use to maintain: \n1) resource \n2) potion \n3) krystal\n");
            String resource = scanner.nextLine();

            if(resource.equals("1") || resource.equals("resource")){
                return "You maintained properties with resource.";
            }else if(resource.equals("2") || resource.equals("potion")){
                return "You maintained properties with potion.";
            }else if(resource.equals("3") || resource.equals("krystal")){
                return "You maintained properties with krystal.";
            }else{
                return "Invalid input.";
            }
        }else{
            return "Invalid input.";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
