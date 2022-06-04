package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.UUID;

class InputMessageHandlerTest {
    private final InputMessageHandler testTarget = new InputMessageHandler();

    @Test
    void shouldUpdateUserAndReturnMessage() {
        final var user = BotUser.builder()
            .id("1")
            .build();
        final var message = "Message to remind!!!";
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(user)
            .message(message)
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage()).isEqualTo(InputMessageHandler.MESSAGE);
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.INPUT_DATE);
    }
}
