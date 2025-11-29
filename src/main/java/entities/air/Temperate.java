package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import model.Weather;

public final class Temperate extends Air {

    private static final double MAX_SCORE = 84.0;
    private static final double HUMIDITY_WEIGHT = 0.7;
    private static final double POLLEN_WEIGHT = 0.1;
    private static final double SPRING_PENALTY = 15.0;
    private static final int OXYGEN_MULTIPLIER = 2;

    protected double pollenLevel;

    public Temperate(final AirInput airInput) {
        super(airInput, MAX_SCORE);
        this.pollenLevel = airInput.getPollenLevel();
    }

    /**
     * calculates the quality of temperate air based on humidity and pollen
     * applies a penalty during spring if weather is affected
     *
     * @return the calculated air quality score
     */
    @Override
    public double getQuality() {
        double normalAirQuality = (oxygenLevel * OXYGEN_MULTIPLIER)
                + (humidity * HUMIDITY_WEIGHT)
                - (pollenLevel * POLLEN_WEIGHT);

        double seasonPenalty = 0.0;
        if ("spring".equalsIgnoreCase(Weather.getNewSeason())) {
            seasonPenalty = SPRING_PENALTY;
        }
        if (weatherAffected) {
            normalAirQuality = normalAirQuality - seasonPenalty;
        }
        return round(normalize(normalAirQuality));
    }

    /**
     * exports the temperate air data to a JSON object
     *
     * @param node the JSON node to populate
     */
    @Override
    public void toJson(final ObjectNode node) {
        double airQuality = getQuality();
        node.put("type", type);
        node.put("name", name);
        node.put("mass", mass);
        node.put("humidity", humidity);
        node.put("temperature", temperature);
        node.put("oxygenLevel", oxygenLevel);
        node.put("airQuality", airQuality);
        node.put("pollenLevel", pollenLevel);
    }
}
