package ru.vichukano.reminder.bot.dao;

import java.util.Optional;

public interface Dao<T> {

    Optional<T> find(String id);

    void add(T t);

    void remove(T t);

}
