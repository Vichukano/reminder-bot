package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@Component("date")
class InputDateHandler extends SkeletonHandler {
    static final String MESSAGE = "Date [%s] accepted. Now choose remind time from values or input custom time"
        + " in format hh:mm or choose "
        + BotCommand.CANCEL.getVal()
        + " to cancel remind process";
    static final String WRONG_DATE_MESSAGE = "Wrong input date format!\n"
        + "Try input date in yyyy-mm-dd format\n"
        + "You can type "
        + BotCommand.CANCEL.getVal()
        + " for abort remind process";

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        final BotUser user = in.getUser();
        try {
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
        } catch (DateTimeParseException e) {
            return KeyboardAnswerContext.builder()
                .uid(in.getUid())
                .message(WRONG_DATE_MESSAGE)
                .keyboardType(Factory.Item.DATE)
                .user(user)
                .build();
        }
    }

    @Override
    protected Logger log() {
        return log;
    }
}
