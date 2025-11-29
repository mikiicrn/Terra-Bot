package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import entities.air.Air;
import entities.soil.Soil;
import fileio.CommandInput;
import model.Cell;
import model.PlayGame;
import model.TerraBot;

public final class ImproveEnvironment {

    private static final int ENERGY_COST = 10;
    private static final double LOW_UPDATE = 0.2;
    private static final double HIGH_UPDATE = 0.3;

    private static final String ERR_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final String ERR_SUBJECT =
            "ERROR: Subject not yet saved. Cannot perform action";
    private static final String ERR_FACT =
            "ERROR: Fact not yet saved. Cannot perform action";

    // private constructor to prevent instantiation of utility class
    private ImproveEnvironment() {
    }

    /**
     * executes the improve environment command
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

        String name = command.getName();
        String improvementType = command.getImprovementType();
        String requiredFact = getFact(improvementType, name);

        if (!terrabot.findSubject(name)) {
            JSONOutput.stateSimulation(ERR_SUBJECT, command, output);
            return;
        }

        if (!terrabot.findFact(name, requiredFact)) {
            JSONOutput.stateSimulation(ERR_FACT, command, output);
            return;
        }

        terrabot.recharge(-ENERGY_COST);

        Cell currentCell = playGame.getCell(terrabot.getX(), terrabot.getY());
        Air air = currentCell.getAir();
        Soil soil = currentCell.getSoil();

        String message = applyEffect(improvementType, name, air, soil);
        JSONOutput.stateSimulation(message, command, output);
    }

    private static String getFact(final String improvementType, final String name) {
        return switch (improvementType) {
            case "increaseHumidity" -> "Method to increase humidity";
            case "increaseMoisture" -> "Method to increaseMoisture";
            case "plantVegetation" -> "Method to plant " + name;
            case "fertilizeSoil" -> "Method to fertilize soil with " + name;
            default -> "";
        };
    }

    private static String applyEffect(final String improvementType,
                                      final String name,
                                      final Air air,
                                      final Soil soil) {
        return switch (improvementType) {
            case "increaseHumidity" -> {
                air.updateHumidity(LOW_UPDATE);
                yield "The humidity was successfully increased using " + name;
            }
            case "increaseMoisture" -> {
                soil.updateWaterRetention(LOW_UPDATE);
                yield "The moisture was successfully increased using " + name;
            }
            case "plantVegetation" -> {
                air.updateOxygenLevel(HIGH_UPDATE);
                yield "The " + name + " was planted successfully.";
            }
            case "fertilizeSoil" -> {
                soil.updateOrganicMatter(HIGH_UPDATE);
                yield "The soil was successfully fertilized using " + name;
            }
            default -> "ERROR: Unknown improvement type.";
        };
    }
}
