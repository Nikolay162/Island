package ru.javarush.island.komlev.services;

import ru.javarush.island.komlev.etnity.Game;
import ru.javarush.island.komlev.view.View;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameWorker extends Thread{
    public static final int PERIOD = 1000;
    private final Game game;

    public GameWorker(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        View view = game.getView();
        view.showMap();
        view.showStatistics();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        List<OrganismWorker> workers = game
                .getEntityFactory()
                .getAllPrototypes()
                .stream()
                .map(o -> new OrganismWorker(o,game.getGameMap()))
                .toList();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ExecutorService service = Executors.newFixedThreadPool(8);
                workers.forEach(service::submit);
                service.shutdown();
                try {
                    if (service.awaitTermination(PERIOD, TimeUnit.MILLISECONDS)) {
                    view.showMap();
                    view.showStatistics();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },
                PERIOD,
                PERIOD,
                TimeUnit.MILLISECONDS);
    }
}
