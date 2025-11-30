package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import entities.Entity;
import fileio.CommandInput;
import model.Cell;
import model.PlayGame;
import model.TerraBot;

public final class Scanning {

    private static final int ENERGY_COST = 7;
    private static final String NONE = "none";

    private static final String MSG_WATER = "The scanned object is water.";
    private static final String MSG_PLANT = "The scanned object is a plant.";
    private static final String MSG_ANIMAL = "The scanned object is an animal.";

    private static final String ERR_ENERGY =
            "ERROR: Not enough energy to perform action";
    private static final String ERR_NOT_FOUND =
            "ERROR: Object not found. Cannot perform action";

    // private constructor to prevent instantiation of utility class
    private Scanning() {
    }

    /**
     * executes the scanning command
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

        // check battery
        if (terrabot.getBattery() < ENERGY_COST) {
            JSONOutput.stateSimulation(ERR_ENERGY, command, output);
            return;
        }

        // sensor data into booleans for readability
        boolean hasColor = !NONE.equals(command.getColor());
        boolean hasSmell = !NONE.equals(command.getSmell());
        boolean hasSound = !NONE.equals(command.getSound());

        Cell cell = playGame.getCell(terrabot.getX(), terrabot.getY());
        String message;

        // determine object type based on sensor logic
        if (!hasColor && !hasSmell && !hasSound) {
            // case: Water (no sensory input)
            if (scanSuccess(cell.getWater(), command, terrabot)) {
                message = MSG_WATER;
            } else {
                message = ERR_NOT_FOUND;
            }
        } else if (hasColor && hasSmell && !hasSound) {
            // case: Plant (sight + smell, no sound)
            if (scanSuccess(cell.getPlant(), command, terrabot)) {
                message = MSG_PLANT;
            } else {
                message = ERR_NOT_FOUND;
            }
        } else if (hasColor && hasSmell) {
            // case: Animal (sight + smell + sound)
            // hasSound must be true here because !hasSound was checked previously
            if (scanSuccess(cell.getAnimal(), command, terrabot)) {
                message = MSG_ANIMAL;
            } else {
                message = ERR_NOT_FOUND;
            }
        } else {
            // case: unknown combination
            message = ERR_NOT_FOUND;
        }

        JSONOutput.stateSimulation(message, command, output);
    }

    /**
     * helper method to handle the common logic for a successful scan
     *
     * @param entity   the entity found in the cell
     * @param command  the input command
     * @param terrabot the robot instance
     * @return true if the entity exists and was processed, false otherwise
     */
    private static boolean scanSuccess(final Entity entity,
                                       final CommandInput command,
                                       final TerraBot terrabot) {
        if (entity == null) {
            return false;
        }

        // update entity state
        entity.setScanTime(command.getTimestamp());
        entity.setScanned(true);

        // update bot state
        terrabot.addInventory(entity);
        terrabot.recharge(-ENERGY_COST);

        return true;
    }
}
