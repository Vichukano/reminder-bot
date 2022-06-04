package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;
import java.time.LocalDate;

@Slf4j
@Component("date")
class InputDateHandler extends SkeletonHandler {
    static final String MESSAGE = "Date [%s] accepted. Now choose remind time from current values:\n";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        final BotUser user = in.getUser();
        final var date = LocalDate.parse(in.getMessage());
        final BotUser updated = BotUser.builder()
            .id(user.getId())
            .state(UserState.INPUT_TIME)
            .context(
                BotUser.RemindContext.builder()
                    .text(user.getContext().getText())
                    .date(date)
                    .build()
            )
            .build();
        return KeyboardAnswerContext.builder()
            .uid(in.getUid())
            .message(String.format(MESSAGE, date))
            .keyboardType(Factory.Item.TIME)
            .user(updated)
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
