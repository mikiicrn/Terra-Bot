package Entities.Soil;

import  com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public class GrasslandSoil extends Soil {
    private final double rootDensity;

    public GrasslandSoil(fileio.SoilInput soilInput) {
        super(soilInput);
        this.rootDensity = soilInput.getRootDensity();
    }

    @Override
    public double getQuality() {
        double score = (nitrogen * 1.3) + (organicMatter * 1.5) + (rootDensity * 0.8);
        double normalizeScore, finalScore;
        normalizeScore = Math.max(0, Math.min(100, score));
        finalScore = Math.round(normalizeScore * 100.0) / 100.0;
        return finalScore;
    }

    public double calculateProbability() {
        return ((50 - rootDensity) + waterRetention * 0.5) / 75 * 100;
    }

    @Override
    public void toJson(com.fasterxml.jackson.databind.node.ObjectNode node) {
        double soilQuality = getQuality();
        node.put("type", type);
        node.put("name", name);
        node.put("mass", mass);
        node.put("nitrogen", nitrogen);
        node.put("waterRetention", waterRetention);
        node.put("soilpH", soilpH);
        node.put("organicMatter", organicMatter);
        node.put("soilQuality", soilQuality);
        node.put("rootDensity", rootDensity);
    }
}
