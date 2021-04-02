package org.cargoism.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.cargoism.utils.Utils;

@Data
@AllArgsConstructor
public class Ship {
    @NonNull
    private Cargo cargo;

    @NonNull
    private String name;

    /**
     * Minutes from simulation started to scheduled arrival (smallest simulated time unit)
     */
    private int arrivalMinutes;

    private int unloadOvertime;

    @Override
    public String toString() {
        return name + " with " + cargo + " scheduled " + Utils.formatTime(arrivalMinutes);
    }

}
