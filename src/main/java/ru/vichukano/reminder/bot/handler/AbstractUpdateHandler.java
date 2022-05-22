package ru.vichukano.reminder.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.Optional;

public abstract class AbstractUpdateHandler implements Handler<Update, SendMessage> {

    protected boolean isUpdateValid(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    protected String chatId(Update update) {
        return Optional.ofNullable(update)
            .map(Update::getMessage)
            .map(Message::getChatId)
            .map(String::valueOf)
            .orElseThrow(() -> new IllegalStateException("Can't find chat id for update: " + update));
    }

    protected String userId(Update update) {
        return Optional.ofNullable(update)
            .map(Update::getMessage)
            .map(Message::getFrom)
            .map(User::getId)
            .map(String::valueOf)
            .orElseThrow(() -> new IllegalStateException("Can't find user id for update: " + update));
    }

    protected String text(Update update) {
        return Optional.ofNullable(update)
            .map(Update::getMessage)
            .map(Message::getText)
            .orElseThrow(() -> new IllegalStateException("Can't find text for update: " + update));
    }

}
