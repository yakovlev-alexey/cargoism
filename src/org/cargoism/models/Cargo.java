package org.cargoism.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cargo {
    /**
     * Type of cargo: bulk, liquid or containers
     */
    private CargoType type;

    /**
     * Weight in tons for bulk and liquid cargo, units for containers
     */
    private int count;

    @Override
    public String toString() {
        return type.toString() + " cargo, " + count + (type == CargoType.CONTAINERS ? " containers" : " tons");
    }

}
