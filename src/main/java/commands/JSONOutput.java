package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

public class JSONOutput {
    public static void stateSimulation(String message, CommandInput command, ArrayNode output) {
        ObjectNode node = output.addObject();
        node.put("command", command.getCommand());
        node.put("message", message);
        node.put("timestamp", command.getTimestamp());
    }
}
