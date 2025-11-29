package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;


public class DesertSoil extends Soil {
    private final double salinity;

    public DesertSoil(SoilInput soilInput) {
        super(soilInput);
        this.salinity = soilInput.getSalinity();
    }

    @Override
    public double getQuality() {
        double score = (nitrogen * 0.5) + (waterRetention * 0.3) - (salinity * 2);
        double normalizeScore, finalScore;
        normalizeScore = Math.max(0, Math.min(100, score));
        finalScore = Math.round(normalizeScore * 100.0) / 100.0;
        return finalScore;
    }

    public double calculateProbability() {
        return (100 - waterRetention + salinity) / 100 * 100;
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
        node.put("salinity", salinity);
    }
}
