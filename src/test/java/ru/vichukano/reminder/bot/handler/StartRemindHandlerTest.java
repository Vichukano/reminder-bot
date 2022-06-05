package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.UUID;

class StartRemindHandlerTest {
    private final StartRemindHandler testTarget = new StartRemindHandler();

    @Test
    void shouldUpdateUserWithCorrectState() {
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(BotUser.builder()
                .state(UserState.START)
                .build())
            .message("bla bla bla")
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage()).isEqualTo(StartRemindHandler.MESSAGE);
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.INPUT_MESSAGE);
    }
}