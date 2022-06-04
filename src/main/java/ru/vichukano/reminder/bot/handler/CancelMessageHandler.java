package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;

@Slf4j
@Component("cancel")
class CancelMessageHandler extends SkeletonHandler {
    static final String MESSAGE = "Remind aborted";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        final BotUser user = in.getUser();
        return SimpleAnswerContext.builder()
            .uid(in.getUid())
            .user(
                BotUser.builder()
                    .id(user.getId())
                    .state(UserState.FINISH)
                    .build()
            )
            .message(MESSAGE)
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
