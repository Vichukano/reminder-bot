package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;

@Slf4j
@Component("message")
class InputMessageHandler extends SkeletonHandler {
    static final String MESSAGE = "Message accepted. Now choose remind date from values"
        + " or type custom date in yyyy-mm-dd format"
        + " or type "
        + BotCommand.CANCEL.getVal()
        + " for cancel remind process";

    @Override
    protected KeyboardAnswerContext handleContext(Context in) {
        final BotUser user = in.getUser();
        return KeyboardAnswerContext.builder()
            .uid(in.getUid())
            .message(MESSAGE)
            .keyboardType(Factory.Item.DATE)
            .user(
                BotUser.builder()
                    .id(user.getId())
                    .state(UserState.INPUT_DATE)
                    .context(
                        BotUser.RemindContext.builder()
                            .text(in.getMessage())
                            .build()
                    )
                    .build()
            )
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
