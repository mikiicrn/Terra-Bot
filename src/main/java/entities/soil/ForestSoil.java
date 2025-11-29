package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public final class ForestSoil extends Soil {

    private final double leafLitter;

    private static final double NITROGEN_WEIGHT = 1.2;
    private static final double ORGANIC_WEIGHT = 2.0;
    private static final double WATER_WEIGHT = 1.5;
    private static final double LITTER_WEIGHT = 0.3;
    private static final double MAX_SCORE = 100.0;
    private static final double ROUNDING_FACTOR = 100.0;

    private static final double PROB_WATER_WEIGHT = 0.6;
    private static final double PROB_LITTER_WEIGHT = 0.4;
    private static final double PROB_DIVISOR = 80.0;
    private static final double PERCENTAGE_SCALE = 100.0;

    public ForestSoil(final SoilInput soilInput) {
        super(soilInput);
        this.leafLitter = soilInput.getLeafLitter();
    }

    /**
     * calculates the quality of forest soil based on nutrients and leaf litter
     *
     * @return the calculated quality score
     */
    @Override
    public double getQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_WEIGHT)
                + (waterRetention * WATER_WEIGHT)
                + (leafLitter * LITTER_WEIGHT);

        double normalizeScore = Math.max(0, Math.min(MAX_SCORE, score));
        return Math.round(normalizeScore * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * calculates the probability of interaction based on water retention and leaf litter
     *
     * @return the probability value
     */
    public double calculateProbability() {
        return (waterRetention * PROB_WATER_WEIGHT + leafLitter * PROB_LITTER_WEIGHT)
                / PROB_DIVISOR * PERCENTAGE_SCALE;
    }

    /**
     * exports the forest soil data to a JSON object
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
        node.put("leafLitter", leafLitter);
    }
}
