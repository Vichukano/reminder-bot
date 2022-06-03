package ru.vichukano.reminder.bot.handler;

import lombok.Builder;
import lombok.Value;
import ru.vichukano.reminder.bot.domain.BotUser;

@Value
@Builder
class InMessageContext {
    String message;
    BotUser user;
}
