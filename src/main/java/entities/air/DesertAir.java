package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Weather;

public class DesertAir extends Air {
    protected double dustParticles;

    public DesertAir(fileio.AirInput airInput) {
        super(airInput, 65.0);
        this.dustParticles = airInput.getDustParticles();
    }

    @Override
    public double getQuality(){
        double normal_air_quality = (oxygenLevel * 2) - (dustParticles * 0.2) - (temperature * 0.3);
        if (weatherAffected && Weather.isDesertStorm()) {
            normal_air_quality = normal_air_quality - 30.0;
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
        node.put("desertStorm", Weather.isDesertStorm());
    }
}
