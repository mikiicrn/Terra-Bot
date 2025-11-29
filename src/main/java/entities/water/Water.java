package entities.water;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.WaterInput;
import lombok.EqualsAndHashCode;
import entities.air.Air;
import entities.Entity;
import entities.soil.Soil;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Water extends Entity {
    protected double salinity;
    protected double pH;
    protected double purity;
    protected double turbidity;
    protected double contaminantIndex;
    protected boolean isFrozen;
    public Water(WaterInput water) {
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

    public void updateWater(Soil soil, Air air) {
        if (!scanned)
            return;
        soil.updateWaterRetention(0.1);
        air.updateHumidity(0.1);
    }

    public void toJson(ObjectNode node) {
        node.put("type", this.type);
        node.put("name", this.name);
        node.put("mass", this.mass);
    }

    public double getQuality() {
        double purity_score        = purity / 100;
        double pH_score            = 1 - Math.abs(pH - 7.5) / 7.5;
        double salinity_score      = 1 - (salinity / 350);
        double turbidity_score     = 1 - (turbidity / 100);
        double contaminant_score   = 1 - (contaminantIndex / 100);
        double frozen_score        = isFrozen ? 0 : 1;
        return (0.3 * purity_score
                + 0.2 * pH_score
                + 0.15 * salinity_score
                + 0.1 * turbidity_score
                + 0.15 * contaminant_score
                + 0.2 * frozen_score) * 100;
    }

    public String getQualityCategory() {
        double quality = getQuality();
        if (quality >= 70) {
            return "good";
        } else if (quality >= 40) {
            return "moderate";
        } else {
            return "poor";
        }
    }

}
