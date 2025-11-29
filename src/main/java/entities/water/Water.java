package entities.water;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Entity;
import entities.air.Air;
import entities.soil.Soil;
import fileio.WaterInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Water extends Entity {
    protected double salinity;
    protected double pH;
    protected double purity;
    protected double turbidity;
    protected double contaminantIndex;
    protected boolean isFrozen;

    private static final double UPDATE_AMOUNT = 0.1;

    private static final double MAX_PURITY = 100.0;
    private static final double OPTIMAL_PH = 7.5;
    private static final double MAX_SALINITY = 350.0;
    private static final double MAX_TURBIDITY = 100.0;
    private static final double MAX_CONTAMINANT = 100.0;
    private static final double PERCENTAGE_SCALE = 100.0;

    private static final double WEIGHT_PURITY = 0.3;
    private static final double WEIGHT_PH = 0.2;
    private static final double WEIGHT_SALINITY = 0.15;
    private static final double WEIGHT_TURBIDITY = 0.1;
    private static final double WEIGHT_CONTAMINANT = 0.15;
    private static final double WEIGHT_FROZEN = 0.2;

    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;

    public Water(final WaterInput water) {
        super();
        this.name = water.getName();
        this.type = water.getType();
        this.mass = water.getMass();
        this.purity = water.getPurity();
        this.salinity = water.getSalinity();
        this.turbidity = water.getTurbidity();
        this.contaminantIndex = water.getContaminantIndex();
        this.pH = water.getPH();
        this.isFrozen = water.isFrozen();
    }

    /**
     * updates the water state and surrounding environment
     * increases soil water retention and air humidity
     *
     * @param soil the soil interacting with the water
     * @param air the air interacting with the water
     */
    public void updateWater(final Soil soil, final Air air) {
        if (!scanned) {
            return;
        }
        soil.updateWaterRetention(UPDATE_AMOUNT);
        air.updateHumidity(UPDATE_AMOUNT);
    }

    /**
     * exports the water entity data to a JSON object
     *
     * @param node the JSON node to populate
     */
    public void toJson(final ObjectNode node) {
        node.put("type", this.type);
        node.put("name", this.name);
        node.put("mass", this.mass);
    }

    /**
     * calculates the overall quality score of the water
     * based on purity, pH, salinity, turbidity, contaminants and frozen state
     *
     * @return the calculated quality score
     */
    public double getQuality() {
        double purityScore = purity / MAX_PURITY;
        double phScore = 1 - Math.abs(pH - OPTIMAL_PH) / OPTIMAL_PH;
        double salinityScore = 1 - (salinity / MAX_SALINITY);
        double turbidityScore = 1 - (turbidity / MAX_TURBIDITY);
        double contaminantScore = 1 - (contaminantIndex / MAX_CONTAMINANT);
        double frozenScore = isFrozen ? 0 : 1;

        return (WEIGHT_PURITY * purityScore
                + WEIGHT_PH * phScore
                + WEIGHT_SALINITY * salinityScore
                + WEIGHT_TURBIDITY * turbidityScore
                + WEIGHT_CONTAMINANT * contaminantScore
                + WEIGHT_FROZEN * frozenScore) * PERCENTAGE_SCALE;
    }

    /**
     * categorizes the water quality into good, moderate or poor
     *
     * @return string representing the category
     */
    public String getQualityCategory() {
        double quality = getQuality();
        if (quality >= GOOD_QUALITY_THRESHOLD) {
            return "good";
        } else if (quality >= MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        } else {
            return "poor";
        }
    }

}
