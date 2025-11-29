package entities.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Weather;

public class MountainAir extends Air {
    protected double altitude;

    public MountainAir (fileio.AirInput airInput) {
        super(airInput, 78.0);
        this.altitude = airInput.getAltitude();
    }

    @Override
    public double getQuality(){
        double oxygenFactor = oxygenLevel - (altitude/1000 * 0.5);
        double normal_air_quality = (oxygenFactor * 2) + (humidity * 0.6);
        if (weatherAffected) {
            normal_air_quality = normal_air_quality - (Weather.getNumberOfHikers() * 0.1);
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
        node.put("altitude", altitude);
    }
}
