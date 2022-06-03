package ru.vichukano.reminder.bot.domain;

public enum UserState {
    START,
    INPUT_MESSAGE,
    INPUT_DATE,
    INPUT_TIME,
    CONFIRM,
    FINISH
}
