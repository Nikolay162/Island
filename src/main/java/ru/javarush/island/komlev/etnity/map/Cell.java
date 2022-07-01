package ru.javarush.island.komlev.etnity.map;

import ru.javarush.island.komlev.etnity.organizms.Organism;
import ru.javarush.island.komlev.util.Randomizer;


import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class Cell {
    private final Map<String, Set<Organism>> residents;
    private final List<Cell> nextCell = new ArrayList<>();
    private final Lock lock = new ReentrantLock(true);
    //разобраться с резидентами

    {
        residents = new HashMap<>() {
            private void checkNull(Object key) {

                this.putIfAbsent(key.toString(), new HashSet<>());
            }

            @Override
            public Set<Organism> get(Object key) {
                checkNull(key);
                return super.get(key);
            }

            @Override
            public Set<Organism> put(String key, Set<Organism> value) {
                checkNull(key);
                return super.put(key, value);
            }
        };
    }

    public Map<String, Set<Organism>> getResidents() {
        return residents;
    }

    public Lock getLock() {
        return lock;
    }

    public void updateNextCell(GameMap map, int row, int col) {
        Cell[][] cells = map.getCells();
        if (row>0) nextCell.add(cells[row-1][col]);
        if (col>0) nextCell.add(cells[row][col-1]);
        if(row< map.getRows()-1) nextCell.add(cells[row+1][col]);
        if (col< map.getCols()-1) nextCell.add(cells[row][col+1]);
        }

    public Cell getNextCell(int countStep) {
        Set<Cell> visitedCells = new HashSet<>();
        Cell currentCell;
        currentCell = this;
        while (visitedCells.size()<countStep) {
            List<Cell> nextCells = currentCell
                    .nextCell
                    .stream()
                    .filter(cell -> !visitedCells.contains(cell))
                    .toList();
            int countDirections = nextCells.size();
            if(countDirections>0) {
                int index = Randomizer.random(0, countDirections);
                currentCell = nextCells.get(index);
                visitedCells.add(currentCell);
            }else {
                break;
            }
        }
     return currentCell;
        }


    @Override
    public String toString() {
        return getResidents().values().stream()
                //выкинули пустые элементы карты
                .filter((list) -> list.size() > 0)
                //отсортировали оставшихся животных по размеру
                .sorted((o1, o2) -> o2.size() - o1.size())
                .limit(3)
                //среди всех волков нашли первого попавшегося (Any),
                // вытащили как он называется, вытащили простое имя этого класса,
                // и вытащили первую букву этого класса
                .map(set -> set.stream().findAny().get().getClass().getSimpleName().substring(0, 1))
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
