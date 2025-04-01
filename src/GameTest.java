import Commands.Army;
import World.CommandManager;
import World.Kingdom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @org.junit.jupiter.api.Test
    void reset() {
        String input1 = "reset\n";
        String input2 = "y\n";
        String combinedInput = input1 + input2;

        InputStream vstup = new ByteArrayInputStream(combinedInput.getBytes());
        System.setIn(vstup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        CommandManager commandManager = new CommandManager();

        String output = outputStream.toString();

        assertTrue(output.contains("Map successfully loaded."));
        assertTrue(output.contains("Location successfully loaded."));
        assertTrue(output.contains("The world has been reset to its default state."));
        assertTrue(output.contains("Map successfully loaded."));
        assertTrue(output.contains("Location successfully loaded."));
    }

    @org.junit.jupiter.api.Test
    void inventory() {
        String input1 = "reset\n";
        String input2 = "y\n";
        String input3 = "inventory\n";
        String combinedInput1 = input1 + input2 + input3;

        InputStream vstup1 = new ByteArrayInputStream(combinedInput1.getBytes());
        System.setIn(vstup1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        CommandManager commandManager = new CommandManager();

        String output = outputStream.toString();

        assertTrue(output.contains("Your inventory:"));
    }

    @org.junit.jupiter.api.Test
    void talk() {
        String input1 = "reset\n";
        String input2 = "y\n";
        String input3 = "talk\n";
        String combinedInput1 = input1 + input2 + input3;

        InputStream vstup1 = new ByteArrayInputStream(combinedInput1.getBytes());
        System.setIn(vstup1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        CommandManager commandManager = new CommandManager();

        String output = outputStream.toString();

        assertTrue(output.contains("Greetings, traveler. Who are you, and what brings you here?"));
    }



    @org.junit.jupiter.api.Test
    void help() {
        String input1 = "reset\n";
        String input2 = "y\n";
        String input3 = "help\n";
        String combinedInput1 = input1 + input2 + input3;

        InputStream vstup1 = new ByteArrayInputStream(combinedInput1.getBytes());
        System.setIn(vstup1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        CommandManager commandManager = new CommandManager();

        String output = outputStream.toString();


        assertTrue(output.contains("You can conquere them by defeating them in a battle, or get their loyalty and make an alliance."));
        assertTrue(output.contains("Your army will increase by defeating enemies."));
    }

    @org.junit.jupiter.api.Test
    void army() {
        String input1 = "reset\n";
        String input2 = "y\n";
        String input3 = "army\n";
        String combinedInput1 = input1 + input2 + input3;

        InputStream vstup1 = new ByteArrayInputStream(combinedInput1.getBytes());
        System.setIn(vstup1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        CommandManager commandManager = new CommandManager();

        String output = outputStream.toString();

        assertTrue(output.contains("You have already conquered this kingdom."));
    }

    @org.junit.jupiter.api.Test
    void characters() {
        String input1 = "reset\n";
        String input2 = "y\n";
        String input3 = "characters\n";
        String combinedInput1 = input1 + input2 + input3;

        InputStream vstup1 = new ByteArrayInputStream(combinedInput1.getBytes());
        System.setIn(vstup1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        CommandManager commandManager = new CommandManager();

        String output = outputStream.toString();

        assertTrue(output.contains("King: Dark king"));
        assertTrue(output.contains("Loyalty: 5"));
        assertTrue(output.contains("Kingdom: Forest kingdom"));
        assertTrue(output.contains("Not Battling"));
    }
}