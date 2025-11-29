package entities.air;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Entity;
import fileio.AirInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Air extends Entity {

    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double maxScore;
    protected boolean weatherAffected;

    private static final double PERCENTAGE_SCALE = 100.0;
    private static final double TOXIC_THRESHOLD = 0.8;
    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;
    private static final double TOXICITY_MULTIPLIER = 100.0;

    public Air(final AirInput airInput, final double maxScore) {
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

    /**
     * updates the humidity level by adding the specified amount
     * rounds the result to two decimal places
     *
     * @param addedHumidity the amount of humidity to add
     */
    public void updateHumidity(final double addedHumidity) {
        this.humidity += addedHumidity;
        this.humidity = Math.round(this.humidity * PERCENTAGE_SCALE) / PERCENTAGE_SCALE;
    }

    /**
     * updates the oxygen level by adding the specified amount
     * rounds the result to two decimal places
     *
     * @param addedOxygen the amount of oxygen to add
     */
    public void updateOxygenLevel(final double addedOxygen) {
        this.oxygenLevel += addedOxygen;
        this.oxygenLevel = Math.round(this.oxygenLevel * PERCENTAGE_SCALE) / PERCENTAGE_SCALE;
    }

    /**
     * calculates the specific quality score of the air type
     *
     * @return the calculated quality score
     */
    public abstract double getQuality();

    /**
     * calculates the toxicity level of the air based on its quality score
     *
     * @return the calculated toxicity value
     */
    public double getToxicity() {
        double airQualityScore = getQuality();
        double toxicityAQ = TOXICITY_MULTIPLIER * (1 - airQualityScore / maxScore);
        double finalResult = round(toxicityAQ);
        finalResult = normalize(finalResult);
        return round(finalResult);
    }

    /**
     * determines if the air is considered toxic based on a threshold
     *
     * @return true if toxic, false otherwise
     */
    public boolean isToxic() {
        double airQualityScore = getQuality();
        double toxicityAQ = TOXICITY_MULTIPLIER * (1 - airQualityScore / maxScore);
        return toxicityAQ > (TOXIC_THRESHOLD * maxScore); // if true => toxic air
    }

    /**
     * returns a string description of the air quality level
     *
     * @return "good", "moderate", or "poor"
     */
    public String getQualityLevel() {
        double quality = getQuality();
        if (quality >= GOOD_QUALITY_THRESHOLD) {
            return "good";
        }
        if (quality >= MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        }
        return "poor";
    }

    /**
     * exports the entity data to a JSON object
     *
     * @param node the JSON node to populate
     */
    public abstract void toJson(ObjectNode node);
}
