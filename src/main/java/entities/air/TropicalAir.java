package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import model.Weather;

public final class TropicalAir extends Air {

    private static final double MAX_SCORE = 82.0;
    private static final double HUMIDITY_WEIGHT = 0.5;
    private static final double CO2_WEIGHT = 0.01;
    private static final double RAINFALL_BONUS = 0.3;
    private static final int OXYGEN_MULTIPLIER = 2;

    protected double co2Level;

    public TropicalAir(final AirInput airInput) {
        super(airInput, MAX_SCORE);
        this.co2Level = airInput.getCo2Level();
    }

    /**
     * calculates the quality of tropical air based on humidity and co2 levels
     * applies a bonus if rainfall is present due to weather
     *
     * @return the calculated air quality score
     */
    @Override
    public double getQuality() {
        double normalAirQuality = (oxygenLevel * OXYGEN_MULTIPLIER)
                + (humidity * HUMIDITY_WEIGHT)
                - (co2Level * CO2_WEIGHT);

        if (weatherAffected) {
            normalAirQuality = normalAirQuality + (Weather.getRainfall() * RAINFALL_BONUS);
        }
        return round(normalize(normalAirQuality));
    }

    /**
     * exports the tropical air data to a JSON object
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
        node.put("co2Level", round(co2Level));
    }
}
