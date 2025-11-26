package Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import Model.Weather;

public class Temperate extends Air {
    protected double pollenLevel;

    public Temperate(fileio.AirInput airInput) {
        super(airInput, 84.0);
        this.pollenLevel = airInput.getPollenLevel();
    }

    @Override
    public double getQuality(){
        double normal_air_quality = (oxygenLevel * 2) + (humidity * 0.7) - (pollenLevel * 0.1);
        double seasonPenalty = 0.0;
        if (Weather.newSeason.equalsIgnoreCase("spring")) {
            seasonPenalty = 15.0;
        }
        if (weatherAffected) {
            normal_air_quality = normal_air_quality - seasonPenalty;
        }
        return round(normalize(normal_air_quality));
    }

    @Override
    public void toJson(ObjectNode node) {
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
