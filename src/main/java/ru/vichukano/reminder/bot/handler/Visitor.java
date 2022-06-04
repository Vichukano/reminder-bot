package ru.vichukano.reminder.bot.handler;

interface Visitor<T> {

    T visit(SimpleAnswerContext sCtx);

    T visit(KeyboardAnswerContext kCtx);

}
