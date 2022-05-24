package ru.vichukano.reminder.bot.handler;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class MessageContext {
    String userId;
    String chatId;
    String message;
}
