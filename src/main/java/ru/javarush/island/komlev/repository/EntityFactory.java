package ru.javarush.island.komlev.repository;

import ru.javarush.island.komlev.etnity.map.Cell;
import ru.javarush.island.komlev.etnity.organizms.Organism;
import ru.javarush.island.komlev.etnity.organizms.animals.herbivores.*;
import ru.javarush.island.komlev.etnity.organizms.animals.predators.*;
import ru.javarush.island.komlev.etnity.organizms.plants.Plant;
import ru.javarush.island.komlev.util.EntityFactoryData;
import ru.javarush.island.komlev.util.Randomizer;


import java.util.*;

public class EntityFactory implements Factory  {
    private static final Class<?>[] TYPES = {Plant.class, Wolf.class, Bear.class,
            Boa.class, Buffalo.class, Boar.class, Caterpillar.class, Deer.class, Duck.class,
            Goat.class, Horse.class, Mouse.class, Rabbit.class, Sheep.class,
            Eagle.class, Fox.class};
    private static final Organism[] GERMS = EntityFactoryData.createPrototypes(TYPES);

    public EntityFactory(){}

    @Override
    public Cell createRandomCell(boolean empty) {
        Cell cell = new Cell();
        Map<String, Set<Organism>> residents = cell.getResidents();
        boolean fill = Randomizer.get(25);
        if(fill&&!empty) {
            for (Organism germ : GERMS) {

                String type = germ.getType();
                boolean born = Randomizer.get(30);
                if (born) {
                    residents.putIfAbsent(type,new HashSet<>());
                    Set<Organism> organisms = residents.get(germ.getClass());
                    int currentCount = organisms.size();
                    int maxCount = germ.getLimit().getMaxCount()-currentCount;
                    int count = Randomizer.random(0, maxCount);
                    for (int i = 0; i < count; i++) {
                        organisms.add(germ.clone());

                    }
                }
            }
        }

        return cell;
    }

    @Override
    public List<Organism> getAllPrototypes() {
        return Arrays.asList(GERMS);
    }
}
