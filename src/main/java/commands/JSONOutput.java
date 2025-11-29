    package commands;

    import com.fasterxml.jackson.databind.node.ArrayNode;
    import com.fasterxml.jackson.databind.node.ObjectNode;
    import fileio.CommandInput;

    public final class JSONOutput {
        // private constructor to prevent instantiation of utility class
        private JSONOutput() {
        }

        /**
         * appends a simulation state message to the output array
         *
         * @param message The message to display (success or error).
         * @param command The command that triggered this output.
         * @param output  The JSON output array to append to.
         */
        public static void stateSimulation(final String message,
                                           final CommandInput command,
                                           final ArrayNode output) {
            ObjectNode node = output.addObject();
            node.put("command", command.getCommand());
            node.put("message", message);
            node.put("timestamp", command.getTimestamp());
        }
    }
