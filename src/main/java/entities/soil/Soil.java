package entities.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import fileio.SoilInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import entities.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
abstract public class Soil extends Entity{
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;

    public Soil(SoilInput soilInput) {
        super(); // Call to the superclass constructor
        this.type = soilInput.getType();
        this.name = soilInput.getName();
        this.mass = soilInput.getMass();
        this.nitrogen = soilInput.getNitrogen();
        this.waterRetention = soilInput.getWaterRetention();
        this.soilpH = soilInput.getSoilpH();
        this.organicMatter = soilInput.getOrganicMatter();
    }

    public void updateWaterRetention(double water) {
        this.waterRetention += water;
        this.waterRetention = round(this.waterRetention);
    }

    public void updateOrganicMatter(double organicMatter) {
        this.organicMatter += organicMatter;
        this.organicMatter = round(this.organicMatter);
    }

    public abstract double getQuality();
    public abstract double calculateProbability();

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
