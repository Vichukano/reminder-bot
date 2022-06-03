package ru.vichukano.reminder.bot.handler;

import lombok.Builder;
import lombok.Value;
import ru.vichukano.reminder.bot.domain.BotUser;

@Value
@Builder
class OutMessageContext {
    String message;
    BotUser user;
    KeybordType keybordType;
}
