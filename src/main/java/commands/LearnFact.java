package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import model.PlayGame;
import model.TerraBot;
import entities.Entity;

import java.util.Objects;

public final class LearnFact {
    private static final int ENERGY_COST = 2;
    private static final String MSG_SUCCESS =
            "The fact has been successfully saved in the database.";
    private static final String ERR_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final String ERR_SUBJECT =
            "ERROR: Subject not yet saved. Cannot perform action";

    // private constructor to prevent instantiation of utility class
    private LearnFact() {
    }

    /**
     * executes the learn fact command
     *
     * @param command  The input command details.
     * @param playGame The current game instance.
     * @param terrabot The robot instance.
     * @param output   The JSON output node.
     */
    public static void command(final CommandInput command,
                                final PlayGame playGame,
                                final TerraBot terrabot,
                                final ArrayNode output) {
        if (terrabot.getBattery() < ENERGY_COST) {
            JSONOutput.stateSimulation(ERR_BATTERY, command, output);
            return;
        }

        String targetEntity = command.getComponents();
        boolean found = false;

        // check if the entity exists in inventory
        for (Entity entity : terrabot.getInventory()) {
            if (Objects.equals(entity.getName(), targetEntity)) {
                found = true;
                break;
            }
        }

        if (found) {
            terrabot.addFact(targetEntity, command.getSubject());
            terrabot.recharge(-ENERGY_COST);
            JSONOutput.stateSimulation(MSG_SUCCESS, command, output);
        } else {
            JSONOutput.stateSimulation(ERR_SUBJECT, command, output);
        }
    }
}
