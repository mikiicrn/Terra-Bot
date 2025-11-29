package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import model.PlayGame;
import model.TerraBot;

public class PrintKnowledgeBase {
    public static void command(CommandInput command,
                               PlayGame playGame,
                               TerraBot terrabot,
                               ArrayNode output) {
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
