package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import model.PlayGame;
import model.TerraBot;

public final class PrintKnowledgeBase {


    // private constructor to prevent instantiation of utility class
    private PrintKnowledgeBase() {
    }

    /**
     * executes the print knowledge base command
     *
     * @param command  The input command.
     * @param playGame The game instance.
     * @param terrabot The robot instance.
     * @param output   The output array.
     */
    public static void command(final CommandInput command,
                               final PlayGame playGame,
                               final TerraBot terrabot,
                               final ArrayNode output) {
        ObjectNode root = output.addObject();
        root.put("command", "printKnowledgeBase");
        ArrayNode knowledgeOutput = root.putArray("output");
        terrabot.getKnowledgeBase().forEach((topic, facts) -> {
            ObjectNode factNode = knowledgeOutput.addObject();
            factNode.put("topic", topic);
            ArrayNode factArray = factNode.putArray("facts");
            for (String fact : facts) {
                factArray.add(fact);
            }
        });
        root.put("timestamp", command.getTimestamp());
    }
}
