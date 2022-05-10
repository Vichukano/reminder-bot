package ru.vichukano.reminder.bot;

public interface Handler<T, V> {

    V handle(T t);

}
