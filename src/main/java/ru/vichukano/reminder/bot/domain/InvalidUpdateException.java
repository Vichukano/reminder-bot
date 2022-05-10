package ru.vichukano.reminder.bot.domain;

public class InvalidUpdateException extends RuntimeException {

    public InvalidUpdateException(String text) {
        super(text);
    }

}
