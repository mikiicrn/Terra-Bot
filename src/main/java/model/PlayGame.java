package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class PlayGame {
    private Cell[][] grid;
    private int width;
    private int height;
    private int currentTime = 0;

    private boolean activeWeather = false;
    private int endTimeWeather = 0;

    private static final int WEATHER_DURATION = 2;
    private static final int ANIMAL_UPDATE_INTERVAL = 2;
    private static final double ORGANIC_MATTER_BONUS = 0.5;
    private static final int DIRECTIONS_COUNT = 4;

    public PlayGame(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * sets the current simulation time and checks for weather expiration
     *
     * @param time the new time value
     */
    public void setCurrentTime(final int time) {
        this.currentTime = time;
        checkWeatherExpiration();
    }

    private void checkWeatherExpiration() {
        if (this.activeWeather && this.currentTime > this.endTimeWeather) {
            applyWeather(false);
            this.activeWeather = false;
            this.endTimeWeather = 0;
            Weather.setType(null);
        }
    }

    // --- Weather Logic ---

    private String getEffectedAirType(final String eventType) {
        if (eventType == null) {
            return null;
        }

        return switch (eventType) {
            case "rainfall" -> "TropicalAir";
            case "polarStorm" -> "PolarAir";
            case "newSeason" -> "TemperateAir";
            case "desertStorm" -> "DesertAir";
            case "peopleHiking" -> "MountainAir";
            default -> null;
        };
    }

    /**
     * applies or removes weather effects on the grid based on the active event
     *
     * @param isActive true to enable weather effects, false to disable
     * @return true if any cells were affected, false otherwise
     */
    public boolean applyWeather(final boolean isActive) {
        String eventType = Weather.getType();
        if (eventType == null) {
            return false;
        }

        this.endTimeWeather = this.currentTime + WEATHER_DURATION;

        String targetAirType = getEffectedAirType(eventType);
        boolean anyCellAffected = false;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                entities.air.Air air = grid[i][j].getAir();
                if (air != null && air.getType().equals(targetAirType)) {
                    air.setWeatherAffected(isActive);
                    anyCellAffected = true;
                }
            }
        }

        if (anyCellAffected) {
            this.activeWeather = true;
        }
        return anyCellAffected;
    }

    // --- Grid setters & getters ---

    /**
     * checks if the given coordinates are within the grid boundaries
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if valid, false otherwise
     */
    public boolean isValidPosition(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * retrieves the cell at the specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the cell object or null if out of bounds
     */
    public Cell getCell(final int x, final int y) {
        if (!isValidPosition(x, y)) {
            return null;
        }

        return grid[x][y];
    }

    /**
     * places an air entity at the specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param air the air entity to place
     */
    public void setAir(final int x, final int y, final entities.air.Air air) {
        grid[x][y].setAir(air);
    }

    /**
     * places a water entity at the specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param water the water entity to place
     */
    public void setWater(final int x, final int y, final entities.water.Water water) {
        grid[x][y].setWater(water);
    }

    /**
     * places a soil entity at the specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param soil the soil entity to place
     */
    public void setSoil(final int x, final int y, final entities.soil.Soil soil) {
        grid[x][y].setSoil(soil);
    }

    /**
     * places a plant entity at the specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param plant the plant entity to place
     */
    public void setPlant(final int x, final int y, final entities.plant.Plant plant) {
        grid[x][y].setPlant(plant);
    }

    /**
     * places an animal entity at the specified coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param animal the animal entity to place
     */
    public void setAnimal(final int x, final int y, final entities.animal.Animal animal) {
        grid[x][y].setAnimal(animal);
    }

    /**
     * updates all scanned entities (plants, water, animals) in the grid
     */
    public void updateScanned() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];

                processPlant(cell);
                processWater(cell);
                processAnimal(cell, x, y);
            }
        }
    }

    private void processPlant(final Cell cell) {
        entities.plant.Plant plant = cell.getPlant();
        if (plant == null || !plant.isScanned()) {
            return;
        }

        if (plant.getAge() == entities.plant.Plant.Age.Dead) {
            cell.setPlant(null);
        } else {
            plant.updatePlant(cell.getSoil(), cell.getWater(), cell.getAir());
        }
    }

    private void processWater(final Cell cell) {
        entities.water.Water water = cell.getWater();
        if (water == null || !water.isScanned()) {
            return;
        }

        int timeSinceScan = this.currentTime - water.getScanTime();
        if (timeSinceScan > 0 && timeSinceScan % 2 == 0) {
            water.updateWater(cell.getSoil(), cell.getAir());
        }
    }

    private void processAnimal(final Cell currentCell, final int x, final int y) {
        entities.animal.Animal animal = currentCell.getAnimal();
        if (animal == null || !animal.isScanned()) {
            return;
        }

        entities.air.Air air = currentCell.getAir();
        animal.updateState(air != null && air.isToxic());

        int timeSinceScan = this.currentTime - animal.getScanTime();

        if (timeSinceScan > 0 && timeSinceScan % ANIMAL_UPDATE_INTERVAL == 0) {
            Cell nextCell = getNextCellForAnimal(x, y);

            if (animal.isCarnivoreOrParasite() && nextCell.getAnimal() != null) {
                animal.setMass(animal.getMass() + nextCell.getAnimal().getMass());
                if (nextCell.getSoil() != null) {
                    nextCell.getSoil().updateOrganicMatter(ORGANIC_MATTER_BONUS);
                }
                animal.feed();
                nextCell.setAnimal(null);
            } else {
                animal.updateAnimal(nextCell);
            }

            // update positions
            animal.setX(nextCell.getX());
            animal.setY(nextCell.getY());
            nextCell.setAnimal(animal);
            currentCell.setAnimal(null);

        } else {
            animal.updateAnimal(currentCell);
        }
    }

    private Cell getNextCellForAnimal(final int x, final int y) {
        List<Cell> neighbors = getNeighbors(x, y);

        Cell bestOption = findBestWaterQuality(neighbors, true);
        if (bestOption != null) {
            return bestOption;
        }

        for (Cell cell : neighbors) {
            if (cell.getPlant() != null) {
                return cell;
            }
        }

        bestOption = findBestWaterQuality(neighbors, false);
        if (bestOption != null) {
            return bestOption;
        }

        if (!neighbors.isEmpty()) {
            return neighbors.get(0);
        }

        return null;
    }

    /**
     * requirePlant = true -> seach only in cells with plants
     * requirePlant = false -> search in all cells with water
     */
    private Cell findBestWaterQuality(final List<Cell> neighbors,
                                      final boolean requirePlant) {
        Cell bestCell = null;
        double maxQuality = 0;

        for (Cell cell : neighbors) {
            if (cell.getWater() != null) {
                if (requirePlant && cell.getPlant() == null) {
                    continue;
                }

                if (cell.getWater().getQuality() > maxQuality) {
                    maxQuality = cell.getWater().getQuality();
                    bestCell = cell;
                }
            }
        }
        return bestCell;
    }

    private List<Cell> getNeighbors(final int x, final int y) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < DIRECTIONS_COUNT; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValidPosition(nx, ny)) {
                neighbors.add(grid[nx][ny]);
            }
        }
        return neighbors;
    }
}
