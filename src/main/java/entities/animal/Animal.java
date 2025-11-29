package entities.animal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Entity;
import entities.plant.Plant;
import entities.soil.Soil;
import entities.water.Water;
import fileio.AnimalInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.Cell;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Animal extends Entity {

    public enum State {
        HUNGRY,
        FULL,
        SICK
    }

    protected State state;

    private static final double ORGANIC_MATTER_FULL = 0.8;
    private static final double ORGANIC_MATTER_PARTIAL = 0.5;
    private static final double WATER_CONSUMPTION_RATE = 0.08;
    private static final double MAX_PROBABILITY = 100.0;
    private static final double RISK_DIVISOR = 10.0;

    private static final double PROB_HERBIVORES = 85.0;
    private static final double PROB_CARNIVORES = 30.0;
    private static final double PROB_OMNIVORES = 60.0;
    private static final double PROB_DETRITIVORES = 90.0;
    private static final double PROB_PARASITES = 10.0;

    public Animal() {
        super();
        this.state = State.FULL;
    }

    public Animal(final AnimalInput animalInput) {
        super();
        this.name = animalInput.getName();
        this.type = animalInput.getType();
        this.mass = animalInput.getMass();
        this.state = State.HUNGRY;
    }

    /**
     * exports the animal data to a JSON object
     *
     * @param node the JSON node to populate
     */
    public void toJson(final ObjectNode node) {
        node.put("type", this.type);
        node.put("name", this.name);
        node.put("mass", this.mass);
    }

    private void eatPlant(final Cell cell) {
        Plant plant = cell.getPlant();
        if (plant != null && plant.isScanned()) {
            this.mass = round(this.mass + plant.getMass());
            cell.setPlant(null);
        }
    }

    private void drinkWater(final Cell cell) {
        Water water = cell.getWater();
        if (water != null && water.isScanned()) {
            double waterConsumed = calculateWaterConsumption(water.getMass());
            this.mass = round(this.mass + waterConsumed);
            double remainingWaterMass = round(water.getMass() - waterConsumed);
            if (remainingWaterMass <= 0) {
                cell.setWater(null);
            } else {
                water.setMass(remainingWaterMass);
            }
        }
    }

    /**
     * updates the animal state by interacting with the new cell environment
     * eats plants and drinks water if available
     *
     * @param newCell the cell the animal is currently in
     */
    public void updateAnimal(final Cell newCell) {
        if (!scanned) {
            return;
        }

        Water water = newCell.getWater();
        Plant plant = newCell.getPlant();
        Soil soil = newCell.getSoil();

        boolean fed = false;
        double organicMatter = 0.0;

        if (plant != null && plant.isScanned() && water != null && water.isScanned()) {
            eatPlant(newCell);
            drinkWater(newCell);
            fed = true;
            organicMatter = ORGANIC_MATTER_FULL;
        } else if (plant != null && plant.isScanned()) {
            eatPlant(newCell);
            fed = true;
            organicMatter = ORGANIC_MATTER_PARTIAL;
        } else if (water != null && water.isScanned()) {
            drinkWater(newCell);
            fed = true;
            organicMatter = ORGANIC_MATTER_PARTIAL;
        }

        if (fed) {
            feed();
        } else {
            makeHungry();
        }

        if (organicMatter > 0 && soil != null) {
            soil.updateOrganicMatter(organicMatter);
        }
    }

    /**
     * calculates the probability of the animal attacking
     *
     * @return the attack probability percentage
     */
    public double getAttackProbability() {
        return switch (type.toUpperCase()) {
            case "HERBIVORES" -> PROB_HERBIVORES;
            case "CARNIVORES" -> PROB_CARNIVORES;
            case "OMNIVORES" -> PROB_OMNIVORES;
            case "DETRITIVORES" -> PROB_DETRITIVORES;
            case "PARASITES" -> PROB_PARASITES;
            default -> 0.0;
        };
    }

    /**
     * calculates the risk factor associated with this animal
     *
     * @return the calculated risk value
     */
    public double getAttackRisk() {
        return (MAX_PROBABILITY - getAttackProbability()) / RISK_DIVISOR;
    }

    /**
     * calculates how much water the animal consumes
     *
     * @param availableWaterMass the mass of water available in the cell
     * @return the amount of water consumed
     */
    public double calculateWaterConsumption(final double availableWaterMass) {
        return Math.min(mass * WATER_CONSUMPTION_RATE, availableWaterMass);
    }

    /**
     * determines the amount of organic matter produced by the animal
     *
     * @return the organic matter value
     */
    public double getOrganicMatterProduction() {
        if (state == State.FULL) {
            return ORGANIC_MATTER_FULL;
        } else {
            return 0.0;
        }
    }

    /**
     * updates the health state of the animal based on air toxicity
     *
     * @param isAirToxic true if the air is toxic, false otherwise
     */
    public void updateState(final boolean isAirToxic) {
        if (isAirToxic) {
            state = State.SICK;
        } else if (state == State.SICK) {
            state = State.HUNGRY;
        }
    }

    /**
     * sets the animal state to full if not sick
     */
    public void feed() {
        if (state != State.SICK) {
            state = State.FULL;
        }
    }

    /**
     * sets the animal state to hungry if not sick
     */
    public void makeHungry() {
        if (state != State.SICK) {
            state = State.HUNGRY;
        }
    }

    /**
     * checks if the animal is a carnivore or parasite
     *
     * @return true if the animal is predatory, false otherwise
     */
    public boolean isCarnivoreOrParasite() {
        String typeUpper = type.toUpperCase();
        return typeUpper.equals("CARNIVORES") || typeUpper.equals("PARASITES");
    }
}
