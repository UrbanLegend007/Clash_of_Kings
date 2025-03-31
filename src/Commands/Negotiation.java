package Commands;

import World.CommandManager;
import World.Kingdom;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Negotiation extends Command {

    private CommandManager worldCommandManager;
    private Inventory inventory = new Inventory();

    public Negotiation(CommandManager worldCommandManager) {
        this.worldCommandManager = worldCommandManager;
    }

    @Override
    public String execute() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            Kingdom currentKingdom = worldCommandManager.world.get(worldCommandManager.currentPosition);

            System.out.print("Enter what you want to do: \n1) start war \n2) peace terms \n3) alliance \n");
            String request = scanner.nextLine().toLowerCase();

            if (request.equals("1") || request.equals("war") || request.equals("start war") || request.equals("start")) {
                currentKingdom.setLoyalty(0);
                currentKingdom.setBattle("Battling");
                return "You are attacking " + currentKingdom.getName() + ". All trading and alliances have been broken.";
            } else if (request.equals("2") || request.equals("peace terms")) {

                if (worldCommandManager.atWar()) {
                    System.out.println("Enter your peace terms:");
                    System.out.println("Peace terms: \n - 1) You will leave this kingdom.\n - 2) You will give this kingdom 30 items (if you have just less, then everything of that item).\nDo you agree? (yes/no)");

                    String terms = scanner.nextLine().toLowerCase();
                    if (terms.equals("yes") || terms.equals("y")) {
                        System.out.print("Enter what you offer (krystals, resources, scrolls, metals): ");
                        String offeredItem = scanner.nextLine().toLowerCase();

                        if (offeredItem.equals("resources")) {
                            if (inventory.getResourceAmount(1) >= 30) {
                                inventory.removeItem(1, 30);
                                currentKingdom.setLoyalty(5);
                                currentKingdom.setBattle("Not Battling");
                                return "You made peace with " + currentKingdom.getName() + ".\nYou gave them 30 " + offeredItem + ".";
                            } else {
                                return "You don't have enough " + offeredItem + " left";
                            }
                        } else if (offeredItem.equals("scrolls")) {
                            if (inventory.getResourceAmount(2) >= 30) {
                                inventory.removeItem(2, 30);
                                currentKingdom.setLoyalty(5);
                                currentKingdom.setBattle("Not Battling");
                                return "You made peace with " + currentKingdom.getName() + ".\nYou gave them 30 " + offeredItem + ".";
                            } else {
                                return "You don't have enough " + offeredItem + " left";
                            }
                        } else if (offeredItem.equals("metals")) {
                            if (inventory.getResourceAmount(3) >= 30) {
                                inventory.removeItem(3, 30);
                                currentKingdom.setLoyalty(5);
                                currentKingdom.setBattle("Not Battling");
                                return "You made peace with " + currentKingdom.getName() + ".\nYou gave them 30 " + offeredItem + ".";
                            } else {
                                return "You don't have enough " + offeredItem + " left";
                            }
                        } else if (offeredItem.equals("krystals")) {
                            if (inventory.getResourceAmount(4) >= 30) {
                                inventory.removeItem(4, 30);
                                currentKingdom.setLoyalty(5);
                                currentKingdom.setBattle("Not Battling");
                                return "You made peace with " + currentKingdom.getName() + ".\nYou gave them 30 " + offeredItem + ".";
                            } else {
                                return "You don't have enough " + offeredItem + " left";
                            }
                        } else {
                            return "Invalid input for offered item.";
                        }
                    } else if (terms.equals("no") || terms.equals("n")) {
                        return "You declined the peace terms of " + currentKingdom.getName() + ".";
                    } else {
                        return "Invalid input for peace terms.";
                    }
                } else {
                    return "\nYou are not at war.";
                }
            } else if (request.equals("3") || request.equals("alliance")) {

                if (worldCommandManager.atWar()) {
                    return "\nYou are at war.\nYou cannot currently make an alliance.";
                } else {
                    currentKingdom.setLoyalty(3);
                    return "You have made an alliance with " + currentKingdom.getName() + ".";
                }

            } else {
                return "Invalid input for action.";
            }

        } catch (InputMismatchException e) {
            return "Invalid input type. Please enter a valid option.";
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

    @Override
    public boolean exit() {
        return false;
    }
}
