package ru.vichukano.reminder.bot.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Objects;

@Slf4j
@Getter
@ToString
public class TelegramMessage {
    private final String text;
    private final String chatId;
    private final String userId;

    public TelegramMessage(Update update) {
        if (Objects.isNull(update)) {
            throw new InvalidUpdateException("Receive null update");
        }
        if (!update.hasMessage()) {
            throw new InvalidUpdateException("Message is absent in update: " + update);
        }
        final Message message = update.getMessage();
        if (!message.hasText()) {
            throw new InvalidUpdateException("Text is absent in message: " + message);
        }
        this.text = message.getText();
        this.chatId = String.valueOf(message.getChatId());
        this.userId = String.valueOf(message.getFrom().getId());
        log.debug("Create new telegram message: {}", this);
    }
}
