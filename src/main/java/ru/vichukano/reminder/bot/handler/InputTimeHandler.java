package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalTime;

@Slf4j
@Component("time")
class InputTimeHandler extends SkeletonHandler {
    static final String MESSAGE = "Time accepted. To confirm choose: %s, or choose: %s for cancel message.\n"
        + "Message: %s, remind date-time: %sT%s";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        final BotUser user = in.getUser();
        final var time = LocalTime.parse(in.getMessage());
        final BotUser updated = BotUser.builder()
            .id(user.getId())
            .state(UserState.CONFIRM)
            .context(
                BotUser.RemindContext.builder()
                    .text(user.getContext().getText())
                    .date(user.getContext().getDate())
                    .time(time)
                    .build()
            )
            .build();
        return SimpleAnswerContext.builder()
            .uid(in.getUid())
            .user(updated)
            .message(
                String.format(
                    MESSAGE,
                    BotCommand.CONFIRM.getVal(),
                    BotCommand.CANCEL.getVal(),
                    updated.getContext().getText(),
                    updated.getContext().getDate(),
                    updated.getContext().getTime()
                )
            )
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
