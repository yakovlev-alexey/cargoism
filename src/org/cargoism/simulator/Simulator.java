package org.cargoism.simulator;

import lombok.Getter;
import lombok.Setter;
import org.cargoism.models.*;
import org.cargoism.utils.Utils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Simulator {

    private final List<Ship> ships;

    private static final int FLUCTUATION_DAYS = 7;

    private static final int SIMULATION_DAYS = 30;

    private static final int UNLOAD_OVERTIME_BOUND = 1440;

    @Getter
    private List<Unload> unloads;

    @Getter
    private int cranesCount;

    public Simulator(Ship[] ships) {
        this.ships = randomizeSchedule(ships);
    }

    @Getter
    @Setter
    private int containerCraneEfficiency = 20;

    private Crane getContainerCrane() {
        return new Crane(containerCraneEfficiency);
    }

    @Getter
    @Setter
    private int bulkCraneEfficiency = 25;

    private Crane getBulkCrane() {
        return new Crane(bulkCraneEfficiency);
    }

    @Getter
    @Setter
    private int liquidCraneEfficiency = 30;

    private Crane getLiquidCrane() {
        return new Crane(liquidCraneEfficiency);
    }

    private List<Ship> randomizeSchedule(Ship[] ships) {
        Random random = new Random();

        return Arrays.stream(ships)
                .peek((ship) -> {
                    var arrivalMinutes = ship.getArrivalMinutes();
                    int fluctuation = Utils.HOURS_IN_DAY * Utils.MINUTES_IN_HOUR * FLUCTUATION_DAYS;

                    ship.setArrivalMinutes(arrivalMinutes - fluctuation + random.nextInt(fluctuation * 2));

                    ship.setUnloadOvertime(random.nextInt(Simulator.UNLOAD_OVERTIME_BOUND));
                })
                .sorted(Comparator.comparingInt(Ship::getArrivalMinutes))
                .filter((ship -> {
                    var arrivalMinutes = ship.getArrivalMinutes();

                    return arrivalMinutes >= 0 && arrivalMinutes <= SIMULATION_DAYS * Utils.MINUTES_IN_DAY;
                }))
                .collect(Collectors.toList());
    }

    private ConcurrentLinkedQueue<Ship> getQueueOfType(CargoType type) {
        return ships
                .stream()
                .filter((ship) -> ship.getCargo().getType() == type)
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }


    public void simulate(int containerCranes, int bulkCranes, int liquidCranes) {
        List<Crane> cranes = new LinkedList<>();

        var containerQueue = getQueueOfType(CargoType.CONTAINERS);

        for (int i = 0; i < containerCranes; ++i) {
            var crane = getContainerCrane();
            crane.setQueuedShips(containerQueue);
            cranes.add(crane);
        }

        var bulkQueue = getQueueOfType(CargoType.BULK);

        for (int i = 0; i < bulkCranes; ++i) {
            var crane = getBulkCrane();
            crane.setQueuedShips(bulkQueue);
            cranes.add(crane);
        }

        var liquidQueue = getQueueOfType(CargoType.LIQUID);

        for (int i = 0; i < liquidCranes; ++i) {
            var crane = getLiquidCrane();
            crane.setQueuedShips(liquidQueue);
            cranes.add(crane);
        }

        cranesCount = cranes.size();

        ExecutorService executorService = Executors.newFixedThreadPool(cranesCount);

        try {
            this.unloads = executorService
                    .invokeAll(cranes)
                    .stream()
                    .map((future) -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .sorted(Comparator.comparingInt(Unload::getUnloadStartMinutes))
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    public void printUnloads() {
        unloads.forEach(System.out::println);
    }

    public void printStats(int craneCost, int overtimePenalty) {
        var totalOvertime = unloads
                .stream()
                .reduce(0, (acc, unload) -> acc + unload.getUnloadOvertimeMinutes(), Integer::sum);

        var maxOvertime = unloads
                .stream()
                .reduce(
                        0,
                        (max, unload) -> Math.max(unload.getUnloadOvertimeMinutes(), max),
                        Integer::max
                );

        var minOvertime = unloads
                .stream()
                .reduce(
                        -1,
                        (min, unload) -> min == -1 ? unload.getUnloadOvertimeMinutes() : Math.min(min, unload.getUnloadOvertimeMinutes()),
                        Integer::min
                );

        System.out.println("\nTotal unloaded ships: " + unloads.size());
        System.out.println("Total cranes used: " + cranesCount);
        System.out.println("Total cranes cost: " + cranesCount * craneCost);
        System.out.println("Total overtime: " + totalOvertime + " minutes or " + Utils.formatTime(totalOvertime));
        System.out.println("Total overtime penalty: " + totalOvertime / Utils.MINUTES_IN_HOUR * overtimePenalty);

        System.out.println("\nMax overtime: " + maxOvertime + " minutes or " + Utils.formatTime(maxOvertime));
        System.out.println("Min overtime: " + minOvertime + " minutes or " + Utils.formatTime(minOvertime));
    }

}
