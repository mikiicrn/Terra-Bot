package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.air.Air;
import entities.animal.Animal;
import entities.plant.Plant;
import entities.soil.Soil;
import entities.water.Water;
import fileio.CommandInput;
import model.Cell;
import model.PlayGame;
import model.TerraBot;

public final class PrintEnvironmentConditions {

    private static final String CMD_KEY = "command";
    private static final String CMD_NAME = "printEnvConditions";
    private static final String OUT_KEY = "output";
    private static final String TIME_KEY = "timestamp";

    private static final String KEY_SOIL = "soil";
    private static final String KEY_WATER = "water";
    private static final String KEY_AIR = "air";
    private static final String KEY_PLANTS = "plants";
    private static final String KEY_ANIMALS = "animals";

    // private constructor to prevent instantiation of utility class
    private PrintEnvironmentConditions() {
    }

    /**
     * executes the print environment conditions command
     *
     * @param command  the input command details
     * @param playGame the current game instance
     * @param terrabot the robot instance
     * @param output   the JSON output node
     */
    public static void command(final CommandInput command,
                               final PlayGame playGame,
                               final TerraBot terrabot,
                               final ArrayNode output) {

        // initialize root object
        ObjectNode root = output.addObject();
        root.put(CMD_KEY, CMD_NAME);
        ObjectNode out = root.putObject(OUT_KEY);

        Cell cell = playGame.getCell(terrabot.getX(), terrabot.getY());

        // check if the entity exists; if so, create the JSON node and populate it immediately
        Soil soil = cell.getSoil();
        if (soil != null) {
            soil.toJson(out.putObject(KEY_SOIL));
        }

        Water water = cell.getWater();
        if (water != null) {
            water.toJson(out.putObject(KEY_WATER));
        }

        Air air = cell.getAir();
        if (air != null) {
            air.toJson(out.putObject(KEY_AIR));
        }

        Plant plant = cell.getPlant();
        if (plant != null) {
            plant.toJson(out.putObject(KEY_PLANTS));
        }

        Animal animal = cell.getAnimal();
        if (animal != null) {
            animal.toJson(out.putObject(KEY_ANIMALS));
        }

        root.put(TIME_KEY, command.getTimestamp());
    }
}
