package ru.vichukano.reminder.bot.handler;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.vichukano.reminder.bot.domain.BotUser;
import java.util.UUID;

@Value
@Builder
class InMessageContext implements Context {
    @NonNull
    UUID uid;
    @NonNull
    BotUser user;
    String message;
}
