package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

@Slf4j
@Component("unknown")
class UnknownMessageHandler extends SkeletonHandler {
    static final String MESSAGE = "Can not handle message: %s, please type: %s";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        return SimpleAnswerContext.builder()
            .uid(in.getUid())
            .user(in.getUser())
            .message(String.format(MESSAGE, in.getMessage(), BotCommand.HELP.getVal()))
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
