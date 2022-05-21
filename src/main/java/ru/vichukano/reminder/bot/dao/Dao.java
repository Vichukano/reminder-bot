package ru.vichukano.reminder.bot.dao;

import java.util.Optional;

public interface Dao<T> {

    Optional<T> find(String id) throws Exception;

    void add(T t) throws Exception;

    void remove(T t) throws Exception;

}
