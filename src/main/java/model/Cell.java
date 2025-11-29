package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

import entities.air.Air;
import entities.animal.Animal;
import entities.plant.Plant;
import entities.soil.Soil;
import entities.water.Water;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Cell {
    private int x, y;
    private Air air;
    private Soil soil;
    private Water water;
    private Plant plant;
    private Animal animal;
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasPlant() {
        return plant != null;
    }
    public boolean hasAnimal() {
        return animal != null;
    }
    public boolean hasWater() {
        return water != null;
    }
}
