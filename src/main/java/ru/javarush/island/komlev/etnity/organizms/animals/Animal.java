package ru.javarush.island.komlev.etnity.organizms.animals;

import ru.javarush.island.komlev.abstraction.interfaces.Movable;
import ru.javarush.island.komlev.abstraction.interfaces.Reproducible;
import ru.javarush.island.komlev.etnity.map.Cell;
import ru.javarush.island.komlev.etnity.organizms.Limit;
import ru.javarush.island.komlev.etnity.organizms.Organism;
import ru.javarush.island.komlev.abstraction.interfaces.Eating;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public abstract class Animal extends Organism implements Eating, Reproducible, Movable {


    public Animal(String name, String icon, double weight, Limit limit) {
        super(name, icon, weight, limit);
    }

    @Override
    public void eat(Cell currentCell) {

    }

    @Override
    public void spawn(Cell currentCell) {
        Type type = this.getClass();
        Map<String, Set<Organism>> residents = currentCell.getResidents();
        Set<Organism> organisms = residents.get(type);
        if (Objects.nonNull(organisms) && organisms.contains(this) && organisms.size()>2) {
            bornClone(currentCell);
        }

    }

    private void bornClone(Cell cell){
     safeModification(cell, c->c.getResidents().get(this.getClass()).add(this.clone()));
    }

    @Override
    public Cell move(Cell startCell) {
        int countStep = this.getLimit().getMaxSpeed();
        Cell last = startCell.getNextCell(countStep);
        removeMe(startCell);
        addMe(last);
        return last;
    }

    private void removeMe(Cell cell) {
        safeModification(cell, c -> c.getResidents().get(this.getClass()).add(this.clone()));
           }

    private void addMe(Cell cell){
        safeModification(cell, c-> c.getResidents().get(this.getClass()).add(this));
    }

    private void safeModification(Cell cell, Consumer<Cell> operation) {
        cell.getLock().lock();
        try {
            operation.accept(cell);
        }finally {
            cell.getLock().unlock();
        }
    }
}
