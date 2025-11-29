package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import model.Cell;
import model.PlayGame;
import model.TerraBot;

/**
 * Command to move the robot to the adjacent cell with the lowest risk.
 */
public final class MoveRobot {

    private static final String ERR_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final int DIRECTIONS = 4;

    // private constructor to prevent instantiation of utility class
    private MoveRobot() {
    }

    /**
     * executes the move robot command
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

        // direction vectors: down, right, up, left
        // dx corresponds to columns, dy corresponds to lines
        final int[] dx = {0, 1, 0, -1};
        final int[] dy = {1, 0, -1, 0};

        int currentX = terrabot.getX();
        int currentY = terrabot.getY();

        double minRisk = Double.MAX_VALUE;
        int bestX = -1;
        int bestY = -1;

        // iterate through all neighbors
        for (int i = 0; i < DIRECTIONS; i++) {
            int nextX = currentX + dx[i];
            int nextY = currentY + dy[i];

            if (playGame.isValidPosition(nextX, nextY)) {
                Cell nextCell = playGame.getCell(nextX, nextY);
                double currentRisk = calculateCellRisk(nextCell);

                if (currentRisk < minRisk) {
                    minRisk = currentRisk;
                    bestX = nextX;
                    bestY = nextY;
                }
            }
        }

        // execute move if a valid spot was found
        if (bestX != -1 && bestY != -1) {
            if (terrabot.getBattery() >= minRisk) {
                // perform move
                terrabot.setX(bestX);
                terrabot.setY(bestY);
                terrabot.setBattery(terrabot.getBattery() - minRisk);

                String message = String.format(
                        "The robot has successfully moved to position (%d, %d).",
                        bestX, bestY
                );
                JSONOutput.stateSimulation(message, command, output);
            } else {
                JSONOutput.stateSimulation(ERR_BATTERY, command, output);
            }
        }
    }

    /**
     * helper method to calculate the risk factor of a specific cell
     *
     * @param cell The cell to evaluate.
     * @return The calculated risk score.
     */
    private static double calculateCellRisk(final Cell cell) {
        double score = 0;
        int riskFactors = 2; // starts with Soil and Air

        score += cell.getSoil().calculateProbability();
        score += cell.getAir().getToxicity();

        if (cell.getAnimal() != null) {
            score += cell.getAnimal().getAttackRisk();
            riskFactors++;
        }

        if (cell.getPlant() != null) {
            score += cell.getPlant().getStuckRisk();
            riskFactors++;
        }

        return Math.round(score / riskFactors);
    }
}
