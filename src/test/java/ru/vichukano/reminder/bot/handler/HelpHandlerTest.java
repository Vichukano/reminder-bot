package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.UUID;

class HelpHandlerTest {
    private final HelpHandler testTarget = new HelpHandler();

    @Test
    void shouldAnswerWithHelpMessage() {
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(BotUser.builder()
                .state(UserState.START)
                .build())
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage())
            .isEqualTo(String.format(HelpHandler.MESSAGE, BotCommand.REMIND.getVal()));
        Assertions.assertThat(result.getUser().getState())
            .isEqualTo(UserState.START);
    }
}