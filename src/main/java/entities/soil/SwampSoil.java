package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public class SwampSoil extends Soil{
    private final double waterLogging;
    public SwampSoil(SoilInput soilInput) {
        super(soilInput);
        this.waterLogging = soilInput.getWaterLogging();
    }

    @Override
    public void toJson(ObjectNode node) {
        double soilQuality = getQuality();
        node.put("type", type);
        node.put("name", name);
        node.put("mass", mass);
        node.put("nitrogen", nitrogen);
        node.put("waterRetention", waterRetention);
        node.put("soilpH", soilpH);
        node.put("organicMatter", organicMatter);
        node.put("soilQuality", soilQuality);
        node.put("waterLogging", waterLogging);
    }

    @Override
    public double getQuality() {
        double score = 	(nitrogen * 1.1) + (organicMatter * 2.2) - (waterLogging * 5);
        score = normalize(score);
        score = round(score);
        return score;
    }

    public double calculateProbability() {
        return waterLogging * 10;
    }
}
