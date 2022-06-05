package ru.vichukano.reminder.bot.handler;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import java.util.UUID;

@Value
@Builder
class SimpleAnswerContext implements VisibleContext<SendMessage> {
    @NonNull
    String message;
    @NonNull
    BotUser user;
    @NonNull UUID uid;

    @Override
    public SendMessage visit(Visitor<SendMessage> visitor) {
        return visitor.visit(this);
    }
}
