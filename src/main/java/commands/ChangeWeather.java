package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import model.PlayGame;
import model.TerraBot;
import model.Weather;

import static java.lang.IO.println;

public final class ChangeWeather {
    private static final String MSG_SUCCESS =
            "The weather has changed.";
    private static final String MSG_ERROR =
            "ERROR: The weather change does not affect the environment. Cannot perform action";

    // private constructor to prevent instantiation of utility class
    private ChangeWeather() {
    }

    /**
     * executes the change weather command
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
        String type = command.getType();

        Weather.setType(type);

        switch (type) {
            case "rainfall" -> Weather.setRainfall(command.getRainfall());
            case "polarStorm" -> Weather.setWindSpeed(command.getWindSpeed());
            case "newSeason" -> Weather.setNewSeason(command.getSeason());
            case "desertStorm" -> Weather.setDesertStorm(command.isDesertStorm());
            case "peopleHiking" -> Weather.setNumberOfHikers(command.getNumberOfHikers());
            default -> println("Unknown weather event type: " + type);
        }

        boolean affected = playGame.applyWeather(true);
        String message;

        if (affected) {
            message = MSG_SUCCESS;
        } else {
            message = MSG_ERROR;
        }

        JSONOutput.stateSimulation(message, command, output);
    }
}
