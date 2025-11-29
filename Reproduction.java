import main.Main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;

public class Reproduction {
    public static void main(String[] args) throws Exception {
        String inputPath = "input/test20_complex_errors.json";
        String outputPath = "out/reproduction_test20.json";
        String refPath = "ref/ref_test20_complex_errors.json";

        Main.action(inputPath, outputPath);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode output = mapper.readTree(new File(outputPath));
        JsonNode ref = mapper.readTree(new File(refPath));

        if (output.equals(ref)) {
            System.out.println("Test 20 PASSED");
        } else {
            System.out.println("Test 20 FAILED");
            System.out.println("Expected: " + ref.toPrettyString());
            System.out.println("Actual: " + output.toPrettyString());
        }
    }
}
