package ru.vichukano.reminder.bot.domain;

import lombok.Getter;

@Getter
public enum BotCommand {
    HELP("/help"),
    REMIND("/remind"),
    CONFIRM("/confirm"),
    CANCEL("/cancel");
    private final String val;

    BotCommand(String val) {
        this.val = val;
    }
}
