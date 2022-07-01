package ru.javarush.island.komlev.util;

import ru.javarush.island.komlev.abstraction.annotation.TypeData;

import ru.javarush.island.komlev.etnity.organizms.Limit;
import ru.javarush.island.komlev.etnity.organizms.Organism;
import ru.javarush.island.komlev.exception.MyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EntityFactoryData {
    //класс для создания прототипов животных


    public EntityFactoryData() {
    }

    public static Organism[] createPrototypes(Class<?>[] TYPES) {
        Organism[] organisms = new Organism[TYPES.length];
        int index = 0;
        for (Class<?> type : TYPES) {
            if (type.isAnnotationPresent(TypeData.class)) {
                TypeData typeData = type.getAnnotation(TypeData.class);
                String name = typeData.name();
                String icon = typeData.icon();
                double maxWeight = typeData.maxWeight();
                double weight = Randomizer.random(maxWeight / 2, maxWeight);
                Limit limit = new Limit(
                        typeData.maxWeight(),
                        typeData.maxCount(),
                        typeData.maxSpeed(),
                        typeData.maxFood()
                );
                // здесь можно еще добавить для уточнения запрос из конфигурации
                organisms[index++] = generatePrototype(type, name, icon, weight, limit);

            }
        }
        return organisms;
    }

    private static Organism generatePrototype(Class<?> type, String name, String icon, double weight, Limit limit) {

        try {
            //ищем конструктор с такими же параметрами как у организма
            Constructor<?> constructor = type.getConstructor(String.class, String.class, double.class, Limit.class);
            // создаем новый экземпляр нашего организма и приводим его к типу (Organism)
            return (Organism) constructor.newInstance(name, icon, weight, limit);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            throw new MyException("no entity constructor", e);
        }
    }

}


