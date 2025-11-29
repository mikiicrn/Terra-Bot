package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public final class GrasslandSoil extends Soil {

    private final double rootDensity;

    private static final double NITROGEN_WEIGHT = 1.3;
    private static final double ORGANIC_WEIGHT = 1.5;
    private static final double ROOT_WEIGHT = 0.8;
    private static final double MAX_SCORE = 100.0;
    private static final double ROUNDING_FACTOR = 100.0;

    private static final double PROB_BASE = 50.0;
    private static final double PROB_WATER_WEIGHT = 0.5;
    private static final double PROB_DIVISOR = 75.0;
    private static final double PERCENTAGE_SCALE = 100.0;

    public GrasslandSoil(final SoilInput soilInput) {
        super(soilInput);
        this.rootDensity = soilInput.getRootDensity();
    }

    /**
     * calculates the quality of grassland soil based on nutrients and root density
     *
     * @return the calculated quality score
     */
    @Override
    public double getQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_WEIGHT)
                + (rootDensity * ROOT_WEIGHT);

        double normalizeScore = Math.max(0, Math.min(MAX_SCORE, score));
        return Math.round(normalizeScore * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * calculates the probability of interaction based on root density and water retention
     *
     * @return the probability value
     */
    public double calculateProbability() {
        return ((PROB_BASE - rootDensity) + waterRetention * PROB_WATER_WEIGHT)
                / PROB_DIVISOR * PERCENTAGE_SCALE;
    }

    /**
     * exports the grassland soil data to a JSON object
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
        node.put("rootDensity", rootDensity);
    }
}
