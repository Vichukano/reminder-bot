package ru.vichukano.reminder.bot.handler;

public class HandlerException extends RuntimeException {

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(Throwable cause) {
        super(cause);
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }

}
