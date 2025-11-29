package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import model.Weather;

public final class MountainAir extends Air {

    private static final double MAX_SCORE = 78.0;
    private static final double ALTITUDE_SCALE = 1000.0;
    private static final double ALTITUDE_PENALTY_WEIGHT = 0.5;
    private static final double HUMIDITY_WEIGHT = 0.6;
    private static final double HIKER_PENALTY = 0.1;
    private static final int OXYGEN_MULTIPLIER = 2;

    protected double altitude;

    public MountainAir(final AirInput airInput) {
        super(airInput, MAX_SCORE);
        this.altitude = airInput.getAltitude();
    }

    /**
     * calculates the quality of mountain air based on altitude and humidity
     * applies a penalty if hikers are present
     *
     * @return the calculated air quality score
     */
    @Override
    public double getQuality() {
        double oxygenFactor = oxygenLevel - (altitude / ALTITUDE_SCALE * ALTITUDE_PENALTY_WEIGHT);
        double normalAirQuality = (oxygenFactor * OXYGEN_MULTIPLIER)
                + (humidity * HUMIDITY_WEIGHT);

        if (weatherAffected) {
            normalAirQuality = normalAirQuality - (Weather.getNumberOfHikers() * HIKER_PENALTY);
        }
        return round(normalize(normalAirQuality));
    }

    /**
     * exports the mountain air data to a JSON object
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
        node.put("altitude", altitude);
    }
}
