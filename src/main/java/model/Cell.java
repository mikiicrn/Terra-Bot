package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import entities.air.Air;
import entities.animal.Animal;
import entities.plant.Plant;
import entities.soil.Soil;
import entities.water.Water;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Cell {
    private int x, y;
    private Air air;
    private Soil soil;
    private Water water;
    private Plant plant;
    private Animal animal;

    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * checks if the cell contains a plant entity
     *
     * @return true if a plant is present, false otherwise
     */
    public boolean hasPlant() {
        return plant != null;
    }

    /**
     * checks if the cell contains an animal entity
     *
     * @return true if an animal is present, false otherwise
     */
    public boolean hasAnimal() {
        return animal != null;
    }

    /**
     * checks if the cell contains a water entity
     *
     * @return true if water is present, false otherwise
     */
    public boolean hasWater() {
        return water != null;
    }
}
