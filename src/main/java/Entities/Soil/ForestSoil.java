package Entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public class ForestSoil extends Soil{

    private final double leafLitter;

    public ForestSoil(SoilInput soilInput) {
        super(soilInput);
        this.leafLitter = soilInput.getLeafLitter();
    }

    @Override
    public double getQuality() {
        double score = 	(nitrogen * 1.2) + (organicMatter * 2) + (waterRetention * 1.5) + (leafLitter * 0.3);
        double normalizeScore, finalScore;
        normalizeScore = Math.max(0, Math.min(100, score));
        finalScore = Math.round(normalizeScore * 100.0) / 100.0;
        return finalScore;
    }

    public double calculateProbability() {
        return (waterRetention * 0.6 + leafLitter * 0.4) / 80 * 100;
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
        node.put("leafLitter", leafLitter);
    }
}
