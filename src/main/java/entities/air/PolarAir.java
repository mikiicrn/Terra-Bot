package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import model.Weather;

public final class PolarAir extends Air {

    private static final double MAX_SCORE = 142.0;
    private static final double TEMP_OFFSET = 100.0;
    private static final double ICE_WEIGHT = 0.05;
    private static final double WIND_WEIGHT = 0.2;
    private static final int OXYGEN_MULTIPLIER = 2;

    protected double iceCrystalConcentration;

    public PolarAir(final AirInput airInput) {
        super(airInput, MAX_SCORE);
        this.iceCrystalConcentration = airInput.getIceCrystalConcentration();
    }

    /**
     * calculates the quality of polar air based on temperature and ice crystals
     * applies a penalty if wind speed is high due to weather
     *
     * @return the calculated air quality score
     */
    @Override
    public double getQuality() {
        double normalAirQuality = (oxygenLevel * OXYGEN_MULTIPLIER)
                + (TEMP_OFFSET - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_WEIGHT);

        if (weatherAffected) {
            normalAirQuality = normalAirQuality - (Weather.getWindSpeed() * WIND_WEIGHT);
        }
        return round(normalize(normalAirQuality));
    }

    /**
     * exports the polar air data to a JSON object
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
        node.put("iceCrystalConcentration", iceCrystalConcentration);
    }
}
