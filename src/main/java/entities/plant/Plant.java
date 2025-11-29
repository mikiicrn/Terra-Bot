package entities.plant;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import fileio.PlantInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import entities.Entity;

import entities.soil.Soil;
import entities.water.Water;
import entities.air.Air;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Plant extends Entity {
    public enum Age {
        Young,
        Mature,
        Old,
        Dead
    }

    protected Age age;
    protected double growthRate = 0;
    protected double oxygen_from_plant = 0.0;

    public Plant(PlantInput plantInput) {
        super();
        this.name = plantInput.getName();
        this.type = plantInput.getType();
        this.mass = plantInput.getMass();
        age = Age.Young;

        switch (type) {
            case "FloweringPlants" -> oxygen_from_plant = 6;
            case "Mosses" -> oxygen_from_plant = 0.8;
            case "Algae" -> oxygen_from_plant = 0.5;
            default -> oxygen_from_plant = 0.0;
        }
    }

    public void updatePlant(Soil soil, Water water, Air air) {
        if (!scanned)
            return;

        grow(0.2);
        if (water != null) {
            grow(0.2);
        }

        if (age == Age.Dead)
            return;

        double oxygenLevel = air.getOxygenLevel() + this.getOxygenProduction();
        oxygenLevel = round(oxygenLevel);
        air.setOxygenLevel(oxygenLevel);
    }

    public void toJson(ObjectNode node) {
        node.put("type", this.type);
        node.put("name", this.name);
        node.put("mass", this.mass);
    }

    public double getBlockingProbability() {
        return switch (type) {
            case "FloweringPlants" -> 90.0;
            case "GymnospermsPlants" -> 60.0;
            case "Ferns" -> 30.0;
            case "Mosses" -> 40.0;
            case "Algae" -> 20.0;
            default -> 0.0;
        };
    }

    public double getStuckRisk() {
        return getBlockingProbability() / 100.0;
    }

    private void advanceAge() {
        if (age == Age.Young) {
            age = Age.Mature;
        } else if (age == Age.Mature) {
            age = Age.Old;
        } else if (age == Age.Old) {
            age = Age.Dead;
        }
    }

    public void grow(double growthAmount) {
        growthRate += growthAmount;
        if (growthRate >= 1.0) {
            advanceAge();
            growthRate = 0.0;
        }
    }

    private double getMaturityOxygenBonus() {
        return switch (age) {
            case Young -> 0.2;
            case Mature -> 0.7;
            case Old -> 0.4;
            case Dead -> 0.0;
        };
    }

    public double getOxygenProduction() {
        return oxygen_from_plant + getMaturityOxygenBonus();
    }
}
