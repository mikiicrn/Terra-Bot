package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;

public final class SwampSoil extends Soil {

    private final double waterLogging;

    private static final double NITROGEN_WEIGHT = 1.1;
    private static final double ORGANIC_WEIGHT = 2.2;
    private static final double LOGGING_PENALTY = 5.0;
    private static final double PROB_MULTIPLIER = 10.0;

    public SwampSoil(final SoilInput soilInput) {
        super(soilInput);
        this.waterLogging = soilInput.getWaterLogging();
    }

    /**
     * exports the swamp soil data to a JSON object
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
        node.put("waterLogging", waterLogging);
    }

    /**
     * calculates the quality of swamp soil based on nutrients and water logging
     *
     * @return the calculated quality score
     */
    @Override
    public double getQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_WEIGHT)
                - (waterLogging * LOGGING_PENALTY);
        score = normalize(score);
        score = round(score);
        return score;
    }

    /**
     * calculates the probability of interaction based on water logging
     *
     * @return the probability value
     */
    public double calculateProbability() {
        return waterLogging * PROB_MULTIPLIER;
    }
}
