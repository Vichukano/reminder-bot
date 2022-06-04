package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.UUID;

class CancelMessageHandlerTest {
    private final CancelMessageHandler testTarget = new CancelMessageHandler();

    @Test
    void shouldRemoveUser() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.INPUT_DATE)
            .build();
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(user)
            .message(BotCommand.CANCEL.getVal())
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage()).isEqualTo(CancelMessageHandler.MESSAGE);
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.FINISH);
    }
}
