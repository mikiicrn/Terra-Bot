package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public final class DesertSoil extends Soil {
    private final double salinity;

    private static final double NITROGEN_WEIGHT = 0.5;
    private static final double WATER_WEIGHT = 0.3;
    private static final double SALINITY_WEIGHT = 2.0;
    private static final double MAX_SCORE = 100.0;
    private static final double ROUNDING_FACTOR = 100.0;
    private static final double PERCENTAGE_BASE = 100.0;

    public DesertSoil(final SoilInput soilInput) {
        super(soilInput);
        this.salinity = soilInput.getSalinity();
    }

    /**
     * calculates the quality of the desert soil based on nitrogen, water and salinity
     *
     * @return the calculated quality score
     */
    @Override
    public double getQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (waterRetention * WATER_WEIGHT)
                - (salinity * SALINITY_WEIGHT);

        double normalizeScore = Math.max(0, Math.min(MAX_SCORE, score));
        return Math.round(normalizeScore * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * calculates the probability of interaction based on water retention and salinity
     *
     * @return the probability value
     */
    public double calculateProbability() {
        return (PERCENTAGE_BASE - waterRetention + salinity) / PERCENTAGE_BASE * PERCENTAGE_BASE;
    }

    /**
     * exports the desert soil data to a JSON object
     *
     * @param node the JSON node to populate
     */
    @Override
    public void toJson(final ObjectNode node) {
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
