package ru.javarush.island.komlev.services;

import ru.javarush.island.komlev.etnity.map.Cell;
import ru.javarush.island.komlev.etnity.map.GameMap;
import ru.javarush.island.komlev.etnity.organizms.Organism;
import ru.javarush.island.komlev.etnity.organizms.animals.Animal;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

public class OrganismWorker implements Runnable {
    private final Organism germ;
    private final GameMap gameMap;
    private final Queue<Task> tasks = new ConcurrentLinkedDeque<>();

    public OrganismWorker(Organism germ, GameMap gameMap) {
        this.germ = germ;
        this.gameMap = gameMap;
    }

    @Override
    public void run() {
        Cell[][] cells = gameMap.getCells();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                Type type = germ.getClass();
                Set<Organism> organisms = cell.getResidents().get(type);
                if (Objects.nonNull(organisms)) {
                    cell.getLock().lock();
                    try {
                        for (Organism organism : organisms) {
                            Task task = new Task(organism, o -> {
                                o.spawn(cell);
                                if (organism instanceof Animal animal) {
                                    animal.eat(cell);
                                    animal.move(cell);
                                }
                            });
                            tasks.add(task);
                        }
                    } finally {
                        cell.getLock().unlock();
                    }
                    for (Task task : tasks) {
                        task.run();
                    }
                    tasks.clear();
                }

            }
        }

    }
}
