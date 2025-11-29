package entities.soil;

public class TundraSoil extends Soil {
    private final double permafrostDepth;

    public TundraSoil(fileio.SoilInput soilInput) {
        super(soilInput);
        this.permafrostDepth = soilInput.getPermafrostDepth();
    }

    @Override
    public double getQuality() {
        double score = (nitrogen * 0.7) + (organicMatter * 0.5) - (permafrostDepth * 1.5);
        double normalizeScore, finalScore;
        normalizeScore = Math.max(0, Math.min(100, score));
        finalScore = Math.round(normalizeScore * 100.0) / 100.0;
        return finalScore;
    }

    public double calculateProbability() {
        return 	(50 - permafrostDepth) / 50 * 100;
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
        node.put("permafrostDepth", permafrostDepth);
    }
}
