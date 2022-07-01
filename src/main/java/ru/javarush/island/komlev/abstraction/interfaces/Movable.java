package ru.javarush.island.komlev.abstraction.interfaces;


import ru.javarush.island.komlev.etnity.map.Cell;

@FunctionalInterface
public interface Movable {
    /*@FunctionalInterface
    interface Finder {
        int getRating ();
    }*/
    //тот кто будет вызывать у животного метод движения обязательно должен
    // прислать ему ячейку из которой это животное стартует
    Cell move (Cell startCell);
}
