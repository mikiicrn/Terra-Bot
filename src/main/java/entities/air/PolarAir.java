package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Weather;

public class PolarAir extends Air {
    protected double iceCrystalConcentration;

    public PolarAir(fileio.AirInput airInput) {
        super(airInput, 142.0);
        this.iceCrystalConcentration = airInput.getIceCrystalConcentration();
    }

    @Override
    public double getQuality(){
        double normal_air_quality = (oxygenLevel * 2) + (100-Math.abs(temperature)) - (iceCrystalConcentration * 0.05);
        if (weatherAffected) {
            normal_air_quality = normal_air_quality - (Weather.getWindSpeed() * 0.2);
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
        node.put("iceCrystalConcentration", iceCrystalConcentration);
    }
}
