package ru.vichukano.reminder.bot.web;

public interface Sender<T> {

    void send(T message);

}
