package ru.vichukano.reminder.bot.telegram;

public interface Factory<T> {

    T construct(Factory.Item item);

    enum Item {
        DATE, TIME
    }

}
