package Entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import Model.Weather;

public class TropicalAir extends Air {
    protected double co2Level;

    public TropicalAir(fileio.AirInput airInput) {
        super(airInput, 82.0);
        this.co2Level = airInput.getCo2Level();
    }

    @Override
    public double getQuality(){
        double normal_air_quality = (oxygenLevel * 2) + (humidity * 0.5) - (co2Level * 0.01);
        if (weatherAffected) {
            normal_air_quality = normal_air_quality + (Weather.rainfall * 0.3);
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
        node.put("co2Level", co2Level);
    }
}
