package entities.soil;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Entity;
import fileio.SoilInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Soil extends Entity {
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;

    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;

    public Soil(final SoilInput soilInput) {
        super(); // Call to the superclass constructor
        this.type = soilInput.getType();
        this.name = soilInput.getName();
        this.mass = soilInput.getMass();
        this.nitrogen = soilInput.getNitrogen();
        this.waterRetention = soilInput.getWaterRetention();
        this.soilpH = soilInput.getSoilpH();
        this.organicMatter = soilInput.getOrganicMatter();
    }

    /**
     * updates the water retention level by adding the specified amount
     *
     * @param water the amount of water to add
     */
    public final void updateWaterRetention(final double water) {
        this.waterRetention += water;
        this.waterRetention = round(this.waterRetention);
    }

    /**
     * updates the organic matter level by adding the specified amount
     *
     * @param amount the amount of organic matter to add
     */
    public final void updateOrganicMatter(final double amount) {
        this.organicMatter += amount;
        this.organicMatter = round(this.organicMatter);
    }

    /**
     * calculates the specific quality score of the soil type
     *
     * @return the calculated quality score
     */
    public abstract double getQuality();

    /**
     * calculates the probability of interaction or growth on this soil
     *
     * @return the probability value
     */
    public abstract double calculateProbability();

    /**
     * returns a string description of the soil quality level
     *
     * @return "good", "moderate", or "poor"
     */
    public final String getQualityLevel() {
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
