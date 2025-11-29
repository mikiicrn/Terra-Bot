package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public final class TundraSoil extends Soil {

    private final double permafrostDepth;

    private static final double NITROGEN_WEIGHT = 0.7;
    private static final double ORGANIC_WEIGHT = 0.5;
    private static final double FROST_PENALTY = 1.5;
    private static final double MAX_SCORE = 100.0;
    private static final double ROUNDING_FACTOR = 100.0;

    private static final double PROB_BASE = 50.0;
    private static final double PROB_DIVISOR = 50.0;
    private static final double PERCENTAGE_SCALE = 100.0;

    public TundraSoil(final SoilInput soilInput) {
        super(soilInput);
        this.permafrostDepth = soilInput.getPermafrostDepth();
    }

    /**
     * calculates the quality of tundra soil based on nutrients and permafrost
     *
     * @return the calculated quality score
     */
    @Override
    public double getQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_WEIGHT)
                - (permafrostDepth * FROST_PENALTY);

        double normalizeScore = Math.max(0, Math.min(MAX_SCORE, score));
        return Math.round(normalizeScore * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * calculates the probability of interaction based on permafrost depth
     *
     * @return the probability value
     */
    public double calculateProbability() {
        return (PROB_BASE - permafrostDepth) / PROB_DIVISOR * PERCENTAGE_SCALE;
    }

    /**
     * exports the tundra soil data to a JSON object
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
        node.put("permafrostDepth", permafrostDepth);
    }
}
