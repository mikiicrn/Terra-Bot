package entities.plant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Entity;
import entities.air.Air;
import entities.soil.Soil;
import entities.water.Water;
import fileio.PlantInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Plant extends Entity {

    public enum Age {
        Young,
        Mature,
        Old,
        Dead
    }

    protected Age age;
    protected double growthRate = 0;
    protected double oxygenFromPlant = 0.0;

    private static final double OXYGEN_FLOWERING = 6.0;
    private static final double OXYGEN_MOSSES = 0.8;
    private static final double OXYGEN_ALGAE = 0.5;

    private static final double BLOCK_PROB_FLOWERING = 90.0;
    private static final double BLOCK_PROB_GYMNOSPERMS = 60.0;
    private static final double BLOCK_PROB_FERNS = 30.0;
    private static final double BLOCK_PROB_MOSSES = 40.0;
    private static final double BLOCK_PROB_ALGAE = 20.0;
    private static final double RISK_DIVISOR = 100.0;

    private static final double GROWTH_INCREMENT = 0.2;
    private static final double GROWTH_THRESHOLD = 1.0;

    private static final double BONUS_YOUNG = 0.2;
    private static final double BONUS_MATURE = 0.7;
    private static final double BONUS_OLD = 0.4;

    public Plant(final PlantInput plantInput) {
        super();
        this.name = plantInput.getName();
        this.type = plantInput.getType();
        this.mass = plantInput.getMass();
        this.age = Age.Young;

        switch (type) {
            case "FloweringPlants" -> oxygenFromPlant = OXYGEN_FLOWERING;
            case "Mosses" -> oxygenFromPlant = OXYGEN_MOSSES;
            case "Algae" -> oxygenFromPlant = OXYGEN_ALGAE;
            default -> oxygenFromPlant = 0.0;
        }
    }

    /**
     * updates the plant state based on environmental conditions
     * grows the plant and releases oxygen into the air
     *
     * @param soil the soil where the plant grows
     * @param water the water available to the plant
     * @param air the surrounding air
     */
    public void updatePlant(final Soil soil, final Water water, final Air air) {
        if (!scanned) {
            return;
        }

        grow(GROWTH_INCREMENT);
        if (water != null) {
            grow(GROWTH_INCREMENT);
        }

        if (age == Age.Dead) {
            return;
        }

        double oxygenLevel = air.getOxygenLevel() + this.getOxygenProduction();
        oxygenLevel = round(oxygenLevel);
        air.setOxygenLevel(oxygenLevel);
    }

    /**
     * exports the plant data to a JSON object
     *
     * @param node the JSON node to populate
     */
    public void toJson(final ObjectNode node) {
        node.put("type", this.type);
        node.put("name", this.name);
        node.put("mass", this.mass);
    }

    /**
     * calculates the probability that the plant will block robot movement
     *
     * @return the blocking probability percentage
     */
    public double getBlockingProbability() {
        return switch (type) {
            case "FloweringPlants" -> BLOCK_PROB_FLOWERING;
            case "GymnospermsPlants" -> BLOCK_PROB_GYMNOSPERMS;
            case "Ferns" -> BLOCK_PROB_FERNS;
            case "Mosses" -> BLOCK_PROB_MOSSES;
            case "Algae" -> BLOCK_PROB_ALGAE;
            default -> 0.0;
        };
    }

    /**
     * calculates the risk of getting stuck in the plant vegetation
     *
     * @return the stuck risk value
     */
    public double getStuckRisk() {
        return getBlockingProbability() / RISK_DIVISOR;
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

    /**
     * increases the growth rate of the plant
     * advances the plant age if growth threshold is met
     *
     * @param growthAmount the amount to grow
     */
    public void grow(final double growthAmount) {
        growthRate += growthAmount;
        if (growthRate >= GROWTH_THRESHOLD) {
            advanceAge();
            growthRate = 0.0;
        }
    }

    private double getMaturityOxygenBonus() {
        return switch (age) {
            case Young -> BONUS_YOUNG;
            case Mature -> BONUS_MATURE;
            case Old -> BONUS_OLD;
            case Dead -> 0.0;
        };
    }

    /**
     * calculates the total oxygen production of the plant
     * combines base production with maturity bonus
     *
     * @return the total oxygen produced
     */
    public double getOxygenProduction() {
        return oxygenFromPlant + getMaturityOxygenBonus();
    }
}
