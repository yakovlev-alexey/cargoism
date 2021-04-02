package org.cargoism.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cargoism.utils.Utils;

@Data
@AllArgsConstructor
public class Unload {

    private String shipName;

    private int arrivalMinutes;

    private int unloadStartMinutes;

    private int unloadFinishMinutes;

    private int unloadOvertimeMinutes;

    @Override
    public String toString() {
        return shipName + " arrived " + Utils.formatTime(arrivalMinutes)
                + ", unload started " + Utils.formatTime(unloadStartMinutes)
                + " and finished " + Utils.formatTime(unloadFinishMinutes)
                + ", total overtime is " + Utils.formatTime(unloadOvertimeMinutes);
    }

}
