package Commands;

import World.CommandManager;

import java.util.Scanner;

public class Army extends Command{

    private CommandManager worldCommandManager;
    Scanner scanner = new Scanner(System.in);

    public Army(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        System.out.print("Enter command: \n1) attack \n2) defense \n3) reinforcements \n4) occupy\n");
        String command = scanner.nextLine();

        if(command.equals("1") || command.equals("attack")){
            return "Your army has attacked.";
        }else if(command.equals("2") || command.equals("defense")){
            return "Your army has defensed.";
        }else if(command.equals("3") || command.equals("reinforcements")){
            return "Your army has reinforcements.";
        }else if(command.equals("4") || command.equals("occupy")){
            return "Your army has occupied.";
        }else{
            return "Invalid command";
        }
    }

    @Override
    public boolean exit() {
        return false;
    }

}
