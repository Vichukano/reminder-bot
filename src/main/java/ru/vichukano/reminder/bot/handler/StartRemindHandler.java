package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;

@Slf4j
@Component("start")
class StartRemindHandler extends SkeletonHandler {
    static final String MESSAGE = "Please, input message what you want to remind and send it to me";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        final BotUser user = in.getUser();
        return SimpleAnswerContext.builder()
            .uid(in.getUid())
            .message(MESSAGE)
            .user(
                BotUser.builder()
                    .id(user.getId())
                    .state(UserState.INPUT_MESSAGE)
                    .build()
            )
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
