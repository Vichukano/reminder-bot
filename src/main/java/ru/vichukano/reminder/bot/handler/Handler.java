package ru.vichukano.reminder.bot.handler;

public interface Handler<T, V> {

    V handle(T t);

}
