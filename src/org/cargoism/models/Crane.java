package org.cargoism.models;

import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Crane implements Callable<List<Unload>> {

    @Setter
    private ConcurrentLinkedQueue<Ship> queuedShips;

    private int lastUnload = 0;

    private final double efficiency;

    public Crane(double efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public List<Unload> call() {
        final List<Unload> unloads = new LinkedList<>();

        while (!queuedShips.isEmpty()) {
            Ship ship = queuedShips.poll();

            if (ship == null) {
                continue;
            }

            int arrival = ship.getArrivalMinutes();

            int unloadStart = Math.max(lastUnload, arrival);

            int plannedDuration = ((int) Math.ceil(ship.getCargo().getCount() / efficiency));

            int unloadFinish = unloadStart + plannedDuration  + ship.getUnloadOvertime();

            lastUnload = unloadFinish;

            int unloadOvertime = ship.getUnloadOvertime() + unloadStart - arrival;

            Unload unload = new Unload(ship.getName(), arrival, unloadStart, unloadFinish, unloadOvertime);

            unloads.add(unload);
        }

        return unloads;
    }
}
