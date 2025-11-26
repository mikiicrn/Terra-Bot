package Entities.Soil;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import fileio.SoilInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import Entities.Entity;

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

    public Soil addWaterRetention(double water) {
        this.waterRetention += water;
        return this;
    }

    public Soil addOrganicMatter(double organicMatter) {
        this.organicMatter += organicMatter;
        return this;
    }

    public abstract double getQuality();

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
