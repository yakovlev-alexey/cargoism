package org.cargoism.generator;

import org.cargoism.models.Cargo;
import org.cargoism.models.CargoType;
import org.cargoism.models.Ship;
import org.cargoism.utils.Utils;

import java.util.Random;
import java.util.stream.IntStream;

public class Generator {
    private final Random random = new Random();

    private final int daysCount;

    public Generator(int daysCount) {
        this.daysCount = daysCount;
    }

    public Ship[] generateSchedule(int shipCount) {
        return IntStream.range(1, shipCount + 1).mapToObj((i) -> {
            Cargo cargo = new Cargo(
                    CargoType.values()[random.nextInt(3)],
                    random.nextInt(1000) + 1
            );

            String name = "Ship " + Utils.padLeftZeroes(String.valueOf(i), 3);

            return new Ship(cargo, name, random.nextInt(Utils.MINUTES_IN_DAY * daysCount), 0);
        }).toArray(Ship[]::new);
    }

}
