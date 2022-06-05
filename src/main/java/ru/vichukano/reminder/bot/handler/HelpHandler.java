package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

@Slf4j
@Component("help")
class HelpHandler extends SkeletonHandler {
    static final String MESSAGE = "Hello! I am reminder bot and I help you to remind something important for you\n"
        + "Type remind command: "
        + BotCommand.REMIND.getVal()
        + " and input your remind message, choose remind date and time.\n"
        + " Message to remind will send to your notification source.";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        return SimpleAnswerContext.builder()
            .uid(in.getUid())
            .message(MESSAGE)
            .user(in.getUser())
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
