package org.cargoism;

import org.cargoism.generator.Generator;
import org.cargoism.simulator.Simulator;
import org.cargoism.storage.Storage;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Generator generator = new Generator(30);

        var ships = generator.generateSchedule(40);

        Storage storage = new Storage();

        var filename = "json/ships.json";

        try {
            storage.storeJson(ships, filename);
        } catch (IOException e) {
            System.out.println("Unable to write JSON schedule to file " + filename);
            return;
        }

        Simulator simulator = new Simulator(ships);

        simulator.simulate(2, 1, 1);

        simulator.printUnloads();
        simulator.printStats(30000, 100);
    }
}
