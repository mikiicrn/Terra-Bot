package Entities.Air;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import lombok.EqualsAndHashCode;
import Entities.Entity;

import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)


abstract public class Air extends Entity {
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double maxScore;
    protected boolean weatherAffected;

    public Air(AirInput airInput, double maxScore) {
        super();
        this.maxScore = maxScore;
        this.type = airInput.getType();
        this.name = airInput.getName();
        this.mass = airInput.getMass();
        this.humidity = airInput.getHumidity();
        this.temperature = airInput.getTemperature();
        this.oxygenLevel = airInput.getOxygenLevel();
        this.weatherAffected = false; // it s not affected initially
    }

    public void updateHumidity(double humidity) {
        this.humidity += humidity;
        this.humidity = Math.round(this.humidity * 100.0) / 100.0;
    }

    public void updateOxygenLevel(double oxygenLevel) {
        this.oxygenLevel += oxygenLevel;
        this.oxygenLevel = Math.round(this.oxygenLevel * 100.0) / 100.0;
    }

    public abstract double getQuality();

    public double getToxicity() {
        double airQualityScore = getQuality();
        double toxicityAQ = 100 * (1 - airQualityScore / maxScore);
        double finalResult = round(toxicityAQ);
        finalResult = normalize(finalResult);
        return round(finalResult);
    }

    public boolean isToxic() {
        double airQualityScore = getQuality();
        double toxicityAQ = 100 * (1 - airQualityScore / maxScore);
        return toxicityAQ > (0.8 * maxScore); // if true => toxic air
    }

    public String getQualityLevel() {
        double quality = getQuality();
        if (quality >= 70)
            return "good";
        if (quality >= 40)
            return "moderate";
        return "poor";
    }

    public abstract void toJson(ObjectNode node);
}
