package ru.vichukano.reminder.bot.dao;

import java.util.Optional;
import java.util.stream.Stream;

public interface Dao<T> {

    Optional<T> find(String id);

    void add(T t);

    void remove(T t);

    Stream<T> findAll();

}
