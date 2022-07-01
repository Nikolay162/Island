package ru.javarush.island.komlev.repository;

import ru.javarush.island.komlev.etnity.map.Cell;
import ru.javarush.island.komlev.etnity.map.GameMap;

public class GameMapFactory {
    private final Factory entityFactory;

    public GameMapFactory(Factory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public GameMap createFilledGameMap(int rows, int cols) {
        return createFilledGameMap(rows,cols,false);
    }

    public GameMap createFilledGameMap(int rows, int cols, boolean empty) {
        GameMap gameMap = new GameMap(rows, cols);
        Cell[][] cells = gameMap.getCells();
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                cells[row][col] = entityFactory.createRandomCell(empty);
            }
        }
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                cells[row][col].updateNextCell(gameMap,row,col);
            }
        }
        return gameMap;
    }
}
