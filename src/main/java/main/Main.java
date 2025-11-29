package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import entities.air.Air;
import entities.air.DesertAir;
import entities.air.MountainAir;
import entities.air.PolarAir;
import entities.air.Temperate;
import entities.air.TropicalAir;
import entities.animal.Animal;
import entities.plant.Plant;
import entities.soil.DesertSoil;
import entities.soil.ForestSoil;
import entities.soil.GrasslandSoil;
import entities.soil.Soil;
import entities.soil.SwampSoil;
import entities.soil.TundraSoil;
import entities.water.Water;
import fileio.AirInput;
import fileio.AnimalInput;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.PairInput;
import fileio.PlantInput;
import fileio.SimulationInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import fileio.WaterInput;
import model.PlayGame;
import model.Simulation;
import model.TerraBot;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath  input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        List<SimulationInput> simulations = inputLoader.getSimulations();
        List<CommandInput> commands = inputLoader.getCommands();

        Simulation simulationInstance = new Simulation();
        PlayGame playGame = null;
        TerraBot terraBot = null;
        int simulationIndex = 0;
        int lastTime = 0;

        for (CommandInput command : commands) {
            String type = command.getCommand();

            if (type.equals("startSimulation")) {
                if (!simulationInstance.isRunning()) {
                    if (simulationIndex < simulations.size()) {
                        SimulationInput simulationInput = simulations.get(simulationIndex);
                        TerritorySectionParamsInput sections =
                                simulationInput.getTerritorySectionParams();
                        System.out.println("Map dimensions: "
                                + simulationInput.getTerritoryDim());

                        terraBot = new TerraBot(simulationInput.getEnergyPoints());
                        playGame = getGameWorld(simulationInput, sections);
                        simulationIndex++;
                        lastTime = 0; // reset time for new simulation
                    }
                }
            }

            int currentTime = command.getTimestamp();
            // update for all timestamps between last command and current
            boolean isRunning = simulationInstance.isRunning();
            if (playGame != null && (isRunning || type.equals("startSimulation"))) {
                for (int t = lastTime + 1; t <= currentTime; t++) {
                    playGame.setCurrentTime(t);
                    playGame.updateScanned();
                }
            }
            lastTime = currentTime;

            simulationInstance.handleCommand(command, playGame, terraBot, output);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }

    private static PlayGame getGameWorld(final SimulationInput simulation,
                                         final TerritorySectionParamsInput sections) {
        int dimX = Integer.parseInt(simulation.getTerritoryDim().split("x")[0]);
        int dimY = Integer.parseInt(simulation.getTerritoryDim().split("x")[1]);
        PlayGame playGame = new PlayGame(dimX, dimY);

        if (sections.getAir() != null) {
            for (AirInput airInput : sections.getAir()) {
                for (PairInput coord : airInput.getSections()) {
                    int x = coord.getX();
                    int y = coord.getY();
                    Air air = switch (airInput.getType()) {
                        case "MountainAir" -> new MountainAir(airInput);
                        case "TemperateAir" -> new Temperate(airInput);
                        case "TropicalAir" -> new TropicalAir(airInput);
                        case "PolarAir" -> new PolarAir(airInput);
                        case "DesertAir" -> new DesertAir(airInput);
                        default -> null;
                    };
                    playGame.setAir(x, y, air);
                }
            }
        }

        if (sections.getSoil() != null) {
            for (SoilInput soilInput : sections.getSoil()) {
                for (PairInput coord : soilInput.getSections()) {
                    int x = coord.getX();
                    int y = coord.getY();
                    Soil soil = switch (soilInput.getType()) {
                        case "DesertSoil" -> new DesertSoil(soilInput);
                        case "ForestSoil" -> new ForestSoil(soilInput);
                        case "GrasslandSoil" -> new GrasslandSoil(soilInput);
                        case "SwampSoil" -> new SwampSoil(soilInput);
                        case "TundraSoil" -> new TundraSoil(soilInput);
                        default -> null;
                    };
                    playGame.setSoil(x, y, soil);
                }
            }
        }

        if (sections.getWater() != null) {
            for (WaterInput waterInput : sections.getWater()) {
                for (PairInput coord : waterInput.getSections()) {
                    int x = coord.getX();
                    int y = coord.getY();
                    Water water = new Water(waterInput);
                    playGame.setWater(x, y, water);
                }
            }
        }
        if (sections.getPlants() != null) {
            for (PlantInput plantInput : sections.getPlants()) {
                for (PairInput coord : plantInput.getSections()) {
                    int x = coord.getX();
                    int y = coord.getY();
                    Plant plant = new Plant(plantInput);
                    playGame.setPlant(x, y, plant);
                }
            }
        }

        if (sections.getAnimals() != null) {
            for (AnimalInput animalInput : sections.getAnimals()) {
                for (PairInput coord : animalInput.getSections()) {
                    int x = coord.getX();
                    int y = coord.getY();
                    Animal animal = new Animal(animalInput);
                    playGame.setAnimal(x, y, animal);
                }
            }
        }
        return playGame;
    }
}
