package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import model.PlayGame;
import model.TerraBot;
import entities.Entity;

import java.util.Objects;

public class LearnFact {
    public static void command (CommandInput command,
                                PlayGame playGame,
                                TerraBot terrabot,
                                ArrayNode output) {
        if (terrabot.getBattery() < 2) {
            String errorMsg = "ERROR: Not enough battery left. Cannot perform action";
            JSONOutput.stateSimulation(errorMsg, command, output);
            return;
        }
        System.out.println("DEBUG LearnFact: Looking for " + command.getComponents() + " in inventory");
        for (Entity entity : terrabot.getInventory()) {
            System.out.println("DEBUG LearnFact: Found entity " + entity.getName());
            if (Objects.equals(entity.getName(), command.getComponents())) {
                terrabot.addFact(command.getComponents(), command.getSubject());
                terrabot.recharge(-2);
                String message = "The fact has been successfully saved in the database.";
                JSONOutput.stateSimulation(message, command, output);
                return;
            }
        }
        System.out.println("DEBUG LearnFact: Entity not found in inventory");
        String message = "ERROR: Subject not yet saved. Cannot perform action";
        JSONOutput.stateSimulation(message, command, output);
        return;
    }
}
