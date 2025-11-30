package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import model.Cell;
import model.PlayGame;
import model.TerraBot;

public final class PrintMap {

    private static final String CMD_KEY = "command";
    private static final String CMD_NAME = "printMap";
    private static final String OUT_KEY = "output";
    private static final String SECTION_KEY = "section";
    private static final String TOTAL_KEY = "totalNrOfObjects";
    private static final String SOIL_KEY = "soilQuality";
    private static final String AIR_KEY = "airQuality";
    private static final String TIME_KEY = "timestamp";

    // private constructor to prevent instantiation of utility class
    private PrintMap() {
    }

    /**
     * executes the print map command
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
        ArrayNode outArray = root.putArray(OUT_KEY);

        int width = playGame.getWidth();
        int height = playGame.getHeight();

        // iterate through the grid
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = playGame.getCell(x, y);
                // Null check added here to satisfy the warning
                if (cell != null) {
                    processCellNode(cell, x, y, outArray);
                }
            }
        }

        // add timestamp
        root.put(TIME_KEY, command.getTimestamp());
    }

    private static void processCellNode(final Cell cell,
                                        final int x,
                                        final int y,
                                        final ArrayNode outArray) {
        ObjectNode cellNode = outArray.addObject();

        // add coordinates
        cellNode.putArray(SECTION_KEY).add(x).add(y);

        // calculate total objects (Plant + Animal + Water)
        int total = (cell.getPlant() != null ? 1 : 0)
                + (cell.getAnimal() != null ? 1 : 0)
                + (cell.getWater() != null ? 1 : 0);

        cellNode.put(TOTAL_KEY, total);

        // add quality level
        // assume Air and Soil are never null if the Cell exists
        if (cell.getAir() != null) {
            cellNode.put(AIR_KEY, cell.getAir().getQualityLevel());
        }
        if (cell.getSoil() != null) {
            cellNode.put(SOIL_KEY, cell.getSoil().getQualityLevel());
        }
    }
}
