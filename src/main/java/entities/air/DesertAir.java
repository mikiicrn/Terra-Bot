package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import model.Weather;

public final class DesertAir extends Air {
    private static final double MAX_SCORE = 65.0;
    private static final double DUST_WEIGHT = 0.2;
    private static final double TEMP_WEIGHT = 0.3;
    private static final double STORM_PENALTY = 30.0;
    private static final int OXYGEN_MULTIPLIER = 2;

    protected double dustParticles;

    public DesertAir(final AirInput airInput) {
        super(airInput, MAX_SCORE);
        this.dustParticles = airInput.getDustParticles();
    }

    /**
     * calculates the quality of the desert air based on dust and temperature
     * applies a penalty if a desert storm is active
     *
     * @return the calculated air quality score
     */
    @Override
    public double getQuality() {
        double normalAirQuality = (oxygenLevel * OXYGEN_MULTIPLIER)
                - (dustParticles * DUST_WEIGHT)
                - (temperature * TEMP_WEIGHT);

        if (weatherAffected && Weather.isDesertStorm()) {
            normalAirQuality = normalAirQuality - STORM_PENALTY;
        }
        return round(normalize(normalAirQuality));
    }

    /**
     * exports the desert air data to a JSON object
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
        node.put("desertStorm", Weather.isDesertStorm());
    }
}
