package ru.vichukano.reminder.bot.handler;

interface VisibleContext<T> extends Context {

    T visit(Visitor<T> visitor);

}
