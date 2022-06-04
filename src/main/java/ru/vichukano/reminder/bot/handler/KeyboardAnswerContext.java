package ru.vichukano.reminder.bot.handler;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.telegram.Factory;
import java.util.UUID;

@Value
@Builder
class KeyboardAnswerContext implements VisibleContext<SendMessage> {
    @NonNull
    UUID uid;
    @NonNull
    BotUser user;
    @NonNull
    String message;
    @NonNull
    Factory.Item keyboardType;

    @Override
    public SendMessage visit(Visitor<SendMessage> visitor) {
        return visitor.visit(this);
    }
}
