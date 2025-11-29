package model;

import commands.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;

public final class Simulation {
    private boolean running = false; // initialized inline
    private static final String NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ALREADY_STARTED =
            "ERROR: Simulation already started. Cannot perform action";
    private static final String STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";

    public boolean isRunning() {
        return running;
    }

    /**
     * handles the commands for the simulation
     *
     * @param command  The input command.
     * @param playGame The game instance.
     * @param terrabot The robot instance.
     * @param output   The output array.
     */
    public void handleCommand(final CommandInput command,
                              final PlayGame playGame,
                              final TerraBot terrabot,
                              final ArrayNode output) {
        String type = command.getCommand();

        // check if Robot is busy charging (global check before any command)
        if (terrabot != null && command.getTimestamp() < terrabot.getTimeUntilCharged()) {
            JSONOutput.stateSimulation(STILL_CHARGING, command, output);
            return;
        }

        // handle State-Changing Commands (Start/End) separately
        if (type.equals("startSimulation")) {
            handleStart(command, output);
            return;
        }

        if (type.equals("endSimulation")) {
            handleEnd(command, output);
            return;
        }

        // global guard: if simulation isn't running, block all other commands here
        if (!running) {
            JSONOutput.stateSimulation(NOT_STARTED, command, output);
            return;
        }

        // handle Action commands (knowing that the simulation is running now)
        switch (type) {
            case "moveRobot":
                if (terrabot != null) {
                    commands.MoveRobot.command(command, playGame, terrabot, output);
                }
                break;

            case "rechargeBattery":
                if (terrabot != null) {
                    handleRecharge(command, terrabot, output);
                }
                break;

            case "changeWeatherConditions":
                if (terrabot != null) {
                    commands.ChangeWeather.command(command, playGame, terrabot, output);
                }
                break;

            case "scanObject":
                if (terrabot != null) {
                    Scanning.command(command, playGame, terrabot, output);
                }
                break;

            case "learnFact":
                if (terrabot != null) {
                    commands.LearnFact.command(command, playGame, terrabot, output);
                }
                break;

            case "printKnowledgeBase":
                if (terrabot != null) {
                    PrintKnowledgeBase.command(command, playGame, terrabot, output);
                }
                break;

            case "improveEnvironment":
                if (terrabot != null) {
                    commands.ImproveEnvironment.command(command, playGame, terrabot, output);
                }
                break;

            case "getEnergyStatus":
                if (terrabot != null) {
                    handleEnergyStatus(command, terrabot, output);
                }
                break;

            case "printEnvConditions":
                if (terrabot != null) {
                    PrintEnvironmentConditions.command(command, playGame, terrabot, output);
                }
                break;

            case "printMap":
                if (terrabot != null) {
                    PrintMap.command(command, playGame, terrabot, output);
                }
                break;

            default:
                // handle unknown commands if necessary (but it won't be with valid input)
                break;
        }
    }

    // helper methods

    private void handleStart(final CommandInput command, final ArrayNode output) {
        if (running) {
            JSONOutput.stateSimulation(ALREADY_STARTED, command, output);
        } else {
            JSONOutput.stateSimulation("Simulation has started.", command, output);
            this.running = true;
        }
    }

    private void handleEnd(final CommandInput command, final ArrayNode output) {
        if (!running) {
            JSONOutput.stateSimulation(NOT_STARTED, command, output);
        } else {
            JSONOutput.stateSimulation("Simulation has ended.", command, output);
            this.running = false;
        }
    }

    private void handleRecharge(final CommandInput command,
                                final TerraBot terrabot,
                                final ArrayNode output) {
        System.out.println(terrabot.getBattery());

        int timeToCharge = command.getTimeToCharge();
        terrabot.recharge(timeToCharge);
        terrabot.setTimeUntilCharged(command.getTimestamp() + timeToCharge);

        JSONOutput.stateSimulation("Robot battery is charging.", command, output);
    }

    private void handleEnergyStatus(final CommandInput command,
                                    final TerraBot terrabot,
                                    final ArrayNode output) {
        String msg = "TerraBot has "
                + Math.round(terrabot.getBattery())
                + " energy points left.";

        JSONOutput.stateSimulation(msg, command, output);
    }
}