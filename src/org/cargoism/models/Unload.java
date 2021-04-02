package org.cargoism.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cargoism.utils.Utils;

import java.util.Arrays;
import java.util.Comparator;

@Data
@AllArgsConstructor
public class Unload {

    private String shipName;

    private Cargo cargo;

    private int arrivalMinutes;

    private int unloadStartMinutes;

    private int unloadFinishMinutes;

    private int unloadOvertimeMinutes;

    @Override
    public String toString() {
        return shipName + " with " + cargo
                + " arrived " + Utils.formatTime(arrivalMinutes)
                + ", unload started " + Utils.formatTime(unloadStartMinutes)
                + " and finished " + Utils.formatTime(unloadFinishMinutes)
                + ", total overtime is " + Utils.formatTime(unloadOvertimeMinutes);
    }

}
